/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2026, Andy Li
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.equipmentcheck;

import com.equipmentcheck.config.SlotCheckMode;
import com.google.inject.Provides;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Equipment Check",
	description = "Alerts you when you have nothing equipped in your gear slots",
	tags = {"hint", "gear", "head", "overlay"}
)
public class EquipmentCheckPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private EquipmentCheckOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EquipmentCheckConfig config;

	@Inject
	private Notifier notifier;

	@Inject
	private ItemManager itemManager;

	private boolean has2hEquipped;

	private boolean hasNotified;

	// Maps each enabled slot to its runtime state (alert debounce flag plus live config accessors)
	private final Map<EquipmentInventorySlot, SlotState> enabledSlots = new EnumMap<>(EquipmentInventorySlot.class);

	private final Map<EquipmentInventorySlot, String> slotNames = new EnumMap<>(EquipmentInventorySlot.class);

	@Provides
	EquipmentCheckConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EquipmentCheckConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		for (EquipmentInventorySlot slot : EquipmentInventorySlot.values())
		{
			slotNames.put(slot, slot.name().toLowerCase());
		}
		has2hEquipped = false;
		hasNotified = false;
		clientThread.invokeLater(() ->
		{
			setupReminders();
			final ItemContainer current = client.getItemContainer(InventoryID.WORN);
			if (current != null)
			{
				updateWeaponState(current);
				for (EquipmentInventorySlot slot : enabledSlots.keySet())
				{
					alertIfNeeded(slot, current);
				}
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
	{
		if (itemContainerChanged.getContainerId() == InventoryID.WORN)
		{
			final ItemContainer worn = itemContainerChanged.getItemContainer();
			updateWeaponState(worn);

			for (EquipmentInventorySlot slot : enabledSlots.keySet())
			{
				if (isSlotUnsatisfied(worn, slot))
				{
					if (enabledSlots.get(slot).isShouldAlert())
					{
						alert(slot);
					}
				}
				else
				{
					enabledSlots.get(slot).setShouldAlert(true);
				}
			}
			if (enabledSlots.isEmpty() || enabledSlots.values().stream().allMatch(SlotState::isShouldAlert))
			{
				hasNotified = false;
			}
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("equipmentCheck") && (event.getKey().endsWith("Check") || event.getKey().endsWith("Item")))
		{
			clientThread.invokeLater(() ->
			{
				setupReminders();
				final ItemContainer current = client.getItemContainer(InventoryID.WORN);
				if (current != null)
				{
					for (EquipmentInventorySlot slot : enabledSlots.keySet())
					{
						alertIfNeeded(slot, current);
					}
				}
			});
		}
	}

	Map<EquipmentInventorySlot, SlotState> getEnabledSlots()
	{
		return Collections.unmodifiableMap(enabledSlots);
	}

	boolean isSlotUnsatisfied(EquipmentInventorySlot slot)
	{
		final ItemContainer worn = client.getItemContainer(InventoryID.WORN);
		if (worn != null)
		{
			return isSlotUnsatisfied(worn, slot);
		}
		return false;
	}

	boolean isSlotCompatible(EquipmentInventorySlot slot)
	{
		return !(slot.equals(EquipmentInventorySlot.SHIELD) && has2hEquipped);
	}

	String getSlotLabel(EquipmentInventorySlot slot)
	{
		return isSlotEmpty(slot) ? slotNames.get(slot) : getSpecifiedItem(slot);
	}

	private boolean isSlotUnsatisfied(ItemContainer equipment, EquipmentInventorySlot slot)
	{
		if (isSlotEmpty(slot))
		{
			return isUnequipped(equipment, slot);
		}
		else
		{
			return !isMatchingItemEquipped(equipment, slot, getSpecifiedItem(slot));
		}
	}

	// Unspecified item (i.e. empty) is equivalent to an empty item slot check
	private boolean isSlotEmpty(EquipmentInventorySlot slot)
	{
		return enabledSlots.get(slot).getMode().get() == SlotCheckMode.EMPTY || getSpecifiedItem(slot).isEmpty();
	}

	private String getSpecifiedItem(EquipmentInventorySlot slot)
	{
		return enabledSlots.get(slot).getItem().get().strip();
	}

	private boolean isUnequipped(ItemContainer equipment, EquipmentInventorySlot slot)
	{
		return equipment.getItem(slot.getSlotIdx()) == null;
	}

	private boolean isMatchingItemEquipped(ItemContainer equipment, EquipmentInventorySlot slot, String gearName)
	{
		final Item item = equipment.getItem(slot.getSlotIdx());
		if (item != null)
		{
			final int id = item.getId();
			final ItemComposition itemComposition = itemManager.getItemComposition(id);
			final String equippedGearName = itemComposition.getName();

			return equippedGearName.toLowerCase().contains(gearName.toLowerCase());
		}
		return false;
	}

	private void setupReminders()
	{
		addIfEnabled(EquipmentInventorySlot.HEAD, config.headCheckMode() != SlotCheckMode.OFF,
			config::headCheckMode, config::getHeadItem);
		addIfEnabled(EquipmentInventorySlot.CAPE, config.capeCheckMode() != SlotCheckMode.OFF,
			config::capeCheckMode, config::getCapeItem);
		addIfEnabled(EquipmentInventorySlot.AMULET, config.amuletCheckMode() != SlotCheckMode.OFF,
			config::amuletCheckMode, config::getAmuletItem);
		addIfEnabled(EquipmentInventorySlot.AMMO, config.ammoCheckMode() != SlotCheckMode.OFF,
			config::ammoCheckMode, config::getAmmoItem);
		addIfEnabled(EquipmentInventorySlot.WEAPON, config.weaponCheckMode() != SlotCheckMode.OFF,
			config::weaponCheckMode, config::getWeaponItem);
		addIfEnabled(EquipmentInventorySlot.BODY, config.bodyCheckMode() != SlotCheckMode.OFF,
			config::bodyCheckMode, config::getBodyItem);
		addIfEnabled(EquipmentInventorySlot.SHIELD, config.shieldCheckMode() != SlotCheckMode.OFF,
			config::shieldCheckMode, config::getShieldItem);
		addIfEnabled(EquipmentInventorySlot.LEGS, config.legsCheckMode() != SlotCheckMode.OFF,
			config::legsCheckMode, config::getLegsItem);
		addIfEnabled(EquipmentInventorySlot.GLOVES, config.glovesCheckMode() != SlotCheckMode.OFF,
			config::glovesCheckMode, config::getGlovesItem);
		addIfEnabled(EquipmentInventorySlot.BOOTS, config.bootsCheckMode() != SlotCheckMode.OFF,
			config::bootsCheckMode, config::getBootsItem);
		addIfEnabled(EquipmentInventorySlot.RING, config.ringCheckMode() != SlotCheckMode.OFF,
			config::ringCheckMode, config::getRingItem);
	}

	private void addIfEnabled(EquipmentInventorySlot slot, boolean shouldAlert, Supplier<SlotCheckMode> mode, Supplier<String> item)
	{
		if (shouldAlert)
		{
			final boolean prev = !enabledSlots.containsKey(slot) || enabledSlots.get(slot).isShouldAlert();
			final SlotState state = new SlotState(prev, mode, item);
			enabledSlots.put(slot, state);
		}
		else
		{
			enabledSlots.remove(slot);
		}
	}

	// Checks a slot against the given equipment on demand, for moments no equipment event covers.
	private void alertIfNeeded(EquipmentInventorySlot slot, ItemContainer container)
	{
		if (isSlotUnsatisfied(container, slot) && enabledSlots.get(slot).isShouldAlert())
		{
			alert(slot);
		}
	}

	private void alert(EquipmentInventorySlot slot)
	{
		if (!isSlotCompatible(slot))
		{
			return;
		}
		enabledSlots.get(slot).setShouldAlert(false);
		printReminder(slot);
		if (!hasNotified)
		{
			final String message = isSlotEmpty(slot)
				? "You have an empty slot!"
				: "You are not wearing one of your listed items!";
			notifier.notify(config.getEmptyNotification(), message);
			hasNotified = true;
		}
	}

	private void printReminder(EquipmentInventorySlot slot)
	{
		final String reminder = isSlotEmpty(slot)
			? "Your " + slotNames.get(slot) + " slot is empty!"
			: "You do not have " + getSpecifiedItem(slot) + " equipped!";
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", reminder, null);
	}

	private void updateWeaponState(ItemContainer equipment)
	{
		has2hEquipped = false;
		final Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (weapon != null)
		{
			final int id = weapon.getId();
			ItemStats stats;
			ItemEquipmentStats equipmentStats;
			if ((stats = itemManager.getItemStats(id)) != null &&
				(equipmentStats = stats.getEquipment()) != null)
			{
				has2hEquipped = equipmentStats.isTwoHanded();
			}
		}
	}
}
