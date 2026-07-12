/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
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

import com.google.inject.Provides;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
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

	private boolean hasNotified = false;

	// Maps enabled slots to boolean flags, which prevents empty slots from being printed multiple times
	private final Map<EquipmentInventorySlot, Boolean> enabledSlots = new EnumMap<>(EquipmentInventorySlot.class);

	private final Map<EquipmentInventorySlot, String> slotNames = new EnumMap<>(EquipmentInventorySlot.class);

	Map<EquipmentInventorySlot, Boolean> getEnabledSlots()
	{
		return Collections.unmodifiableMap(enabledSlots);
	}

	Map<EquipmentInventorySlot, String> getSlotNames()
	{
		return Collections.unmodifiableMap(slotNames);
	}

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
		hasNotified = false;
		clientThread.invokeLater(() ->
		{
			setupReminders();
			final ItemContainer current = client.getItemContainer(InventoryID.WORN);
			if (current != null)
			{
				for (EquipmentInventorySlot slot : enabledSlots.keySet())
				{
					reconcile(slot, current);
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

			for (EquipmentInventorySlot slot : enabledSlots.keySet())
			{
				if (isUnequipped(worn, slot))
				{
					if (enabledSlots.get(slot))
					{
						alert(slot);
					}
				}
				else
				{
					enabledSlots.put(slot, true);
				}
			}
			if (enabledSlots.isEmpty() || !enabledSlots.containsValue(false))
			{
				hasNotified = false;
			}
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("equipmentCheck"))
		{
			boolean enabled = Boolean.TRUE.toString().equals(event.getNewValue());
			clientThread.invokeLater(() ->
			{
				EquipmentInventorySlot slot = null;
				switch (event.getKey())
				{
					case "headCheck":
						slot = EquipmentInventorySlot.HEAD;
						break;
					case "amuletCheck":
						slot =  EquipmentInventorySlot.AMULET;
						break;
					case "bodyCheck":
						slot = EquipmentInventorySlot.BODY;
						break;
					case "legsCheck":
						slot = EquipmentInventorySlot.LEGS;
						break;
					case "bootsCheck":
						slot = EquipmentInventorySlot.BOOTS;
						break;
					case "glovesCheck":
						slot =  EquipmentInventorySlot.GLOVES;
						break;
					case "ringCheck":
						slot = EquipmentInventorySlot.RING;
						break;
					case "capeCheck":
						slot = EquipmentInventorySlot.CAPE;
						break;
					case "weaponCheck":
						slot = EquipmentInventorySlot.WEAPON;
						break;
					case "shieldCheck":
						slot = EquipmentInventorySlot.SHIELD;
						break;
					case "ammoCheck":
						slot = EquipmentInventorySlot.AMMO;
						break;
				}
				if (slot != null)
				{
					addIfEnabled(enabled, slot);

					final ItemContainer current = client.getItemContainer(InventoryID.WORN);
					if (enabled && current != null)
					{
						reconcile(slot, current);
					}
				}
			});
		}
	}

	boolean isSlotEmpty(EquipmentInventorySlot slot)
	{
		ItemContainer worn = client.getItemContainer(InventoryID.WORN);
		if (worn != null)
		{
			return isUnequipped(worn, slot);
		}
		return false;
	}

	private boolean isUnequipped(ItemContainer equipment, EquipmentInventorySlot slot)
	{
		return equipment.getItem(slot.getSlotIdx()) == null;
	}

	private void addIfEnabled(boolean isEnabled, EquipmentInventorySlot slot)
	{
		if (isEnabled)
		{
			enabledSlots.put(slot, true);
		}
		else
		{
			enabledSlots.remove(slot);
		}
	}

	private void setupReminders()
	{
		addIfEnabled(config.isHeadEquipped(), EquipmentInventorySlot.HEAD);
		addIfEnabled(config.isCapeEquipped(), EquipmentInventorySlot.CAPE);
		addIfEnabled(config.isAmuletEquipped(), EquipmentInventorySlot.AMULET);
		addIfEnabled(config.isAmmoEquipped(), EquipmentInventorySlot.AMMO);
		addIfEnabled(config.isWeaponEquipped(), EquipmentInventorySlot.WEAPON);
		addIfEnabled(config.isBodyEquipped(), EquipmentInventorySlot.BODY);
		addIfEnabled(config.isShieldEquipped(), EquipmentInventorySlot.SHIELD);
		addIfEnabled(config.areLegsEquipped(), EquipmentInventorySlot.LEGS);
		addIfEnabled(config.areGlovesEquipped(), EquipmentInventorySlot.GLOVES);
		addIfEnabled(config.areBootsEquipped(), EquipmentInventorySlot.BOOTS);
		addIfEnabled(config.isRingEquipped(), EquipmentInventorySlot.RING);
	}

	private void printReminder(EquipmentInventorySlot slot)
	{
		String reminder = "Your " + slotNames.get(slot) + " slot is empty!";
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", reminder, null);
	}

	private void alert(EquipmentInventorySlot slot)
	{
		enabledSlots.put(slot, false);
		printReminder(slot);
		if (!hasNotified)
		{
			notifier.notify(config.getEmptyNotification(), "You have an empty slot!");
			hasNotified = true;
		}
	}

	private void reconcile(EquipmentInventorySlot slot, ItemContainer container)
	{
		if (isUnequipped(container, slot))
		{
			alert(slot);
		}
	}
}
