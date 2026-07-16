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

import com.equipmentcheck.config.SlotCheckMode;
import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Notification;
import net.runelite.client.ui.overlay.components.ComponentConstants;

@ConfigGroup("equipmentCheck")
public interface EquipmentCheckConfig extends Config
{
	@ConfigSection(
		name = "Equipment Slots",
		description = "Configuration for each equipment slot.",
		position = 10
	)
	String equipmentSlotsSection = "equipmentSlots";

	@ConfigSection(
		name = "Warning Colors",
		description = "Configuration for warning colors.",
		position = 20
	)
	String warningColorsSection = "warningColors";

	@ConfigSection(
		name = "Misc.",
		description = "General plugin configurations.",
		position = 30
	)
	String miscSection = "misc";

	@ConfigItem(
		keyName = "headCheck",
		name = "Head",
		description = "Warn unless the head slot holds a specific item, warn when it is empty, or off.",
		position = 1,
		section = equipmentSlotsSection
	)
	default SlotCheckMode headCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "headItem",
		name = "Head item",
		description = "Item required in the head slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 2,
		section = equipmentSlotsSection
	)
	default String getHeadItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "capeCheck",
		name = "Cape",
		description = "Warn unless the cape slot holds a specific item, warn when it is empty, or off.",
		position = 3,
		section = equipmentSlotsSection
	)
	default SlotCheckMode capeCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "capeItem",
		name = "Cape item",
		description = "Item required in the cape slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 4,
		section = equipmentSlotsSection
	)
	default String getCapeItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "amuletCheck",
		name = "Amulet",
		description = "Warn unless the amulet slot holds a specific item, warn when it is empty, or off.",
		position = 5,
		section = equipmentSlotsSection
	)
	default SlotCheckMode amuletCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "amuletItem",
		name = "Amulet item",
		description = "Item required in the amulet slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 6,
		section = equipmentSlotsSection
	)
	default String getAmuletItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "ammoCheck",
		name = "Ammo",
		description = "Warn unless the ammo slot holds a specific item, warn when it is empty, or off.",
		position = 7,
		section = equipmentSlotsSection
	)
	default SlotCheckMode ammoCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "ammoItem",
		name = "Ammo item",
		description = "Item required in the ammo slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 8,
		section = equipmentSlotsSection
	)
	default String getAmmoItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "weaponCheck",
		name = "Weapon",
		description = "Warn unless the weapon slot holds a specific item, warn when it is empty, or off.",
		position = 9,
		section = equipmentSlotsSection
	)
	default SlotCheckMode weaponCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "weaponItem",
		name = "Weapon item",
		description = "Item required in the weapon slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 10,
		section = equipmentSlotsSection
	)
	default String getWeaponItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "shieldCheck",
		name = "Shield",
		description = "Warn unless the shield slot holds a specific item, warn when it is empty, or off."
			+ " Suppressed automatically while a two-handed weapon is equipped.",
		position = 11,
		section = equipmentSlotsSection
	)
	default SlotCheckMode shieldCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "shieldItem",
		name = "Shield item",
		description = "Item required in the shield slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 12,
		section = equipmentSlotsSection
	)
	default String getShieldItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "bodyCheck",
		name = "Body",
		description = "Warn unless the body slot holds a specific item, warn when it is empty, or off.",
		position = 13,
		section = equipmentSlotsSection
	)
	default SlotCheckMode bodyCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "bodyItem",
		name = "Body item",
		description = "Item required in the body slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 14,
		section = equipmentSlotsSection
	)
	default String getBodyItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "legsCheck",
		name = "Legs",
		description = "Warn unless the legs slot holds a specific item, warn when it is empty, or off.",
		position = 15,
		section = equipmentSlotsSection
	)
	default SlotCheckMode legsCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "legsItem",
		name = "Legs item",
		description = "Item required in the legs slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 16,
		section = equipmentSlotsSection
	)
	default String getLegsItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "glovesCheck",
		name = "Gloves",
		description = "Warn unless the gloves slot holds a specific item, warn when it is empty, or off.",
		position = 17,
		section = equipmentSlotsSection
	)
	default SlotCheckMode glovesCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "glovesItem",
		name = "Gloves item",
		description = "Item required in the gloves slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 18,
		section = equipmentSlotsSection
	)
	default String getGlovesItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "bootsCheck",
		name = "Boots",
		description = "Warn unless the boots slot holds a specific item, warn when it is empty, or off.",
		position = 19,
		section = equipmentSlotsSection
	)
	default SlotCheckMode bootsCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "bootsItem",
		name = "Boots item",
		description = "Item required in the boots slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 20,
		section = equipmentSlotsSection
	)
	default String getBootsItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "ringCheck",
		name = "Ring",
		description = "Warn unless the ring slot holds a specific item, warn when it is empty, or off.",
		position = 21,
		section = equipmentSlotsSection
	)
	default SlotCheckMode ringCheckMode()
	{
		return SlotCheckMode.EMPTY;
	}

	@ConfigItem(
		keyName = "ringItem",
		name = "Ring item",
		description = "Item required in the ring slot when the mode is Item-specific. Partial names match."
			+ " Leave blank to just check the slot is filled.",
		position = 22,
		section = equipmentSlotsSection
	)
	default String getRingItem()
	{
		return "";
	}

	@ConfigItem(
		keyName = "headColor",
		name = "Head Warning",
		description = "Color of the head slot's overlay warning.",
		position = 23,
		section = warningColorsSection
	)
	default Color headColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "capeColor",
		name = "Cape Warning",
		description = "Color of the cape slot's overlay warning.",
		position = 24,
		section = warningColorsSection
	)
	default Color capeColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "amuletColor",
		name = "Amulet Warning",
		description = "Color of the amulet slot's overlay warning.",
		position = 25,
		section = warningColorsSection
	)
	default Color amuletColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "ammoColor",
		name = "Ammo Warning",
		description = "Color of the ammo slot's overlay warning.",
		position = 26,
		section = warningColorsSection
	)
	default Color ammoColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "weaponColor",
		name = "Weapon Warning",
		description = "Color of the weapon slot's overlay warning.",
		position = 27,
		section = warningColorsSection
	)
	default Color weaponColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "shieldColor",
		name = "Shield Warning",
		description = "Color of the shield slot's overlay warning.",
		position = 28,
		section = warningColorsSection
	)
	default Color shieldColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "bodyColor",
		name = "Body Warning",
		description = "Color of the body slot's overlay warning.",
		position = 29,
		section = warningColorsSection
	)
	default Color bodyColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "legsColor",
		name = "Legs Warning",
		description = "Color of the legs slot's overlay warning.",
		position = 30,
		section = warningColorsSection
	)
	default Color legsColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "glovesColor",
		name = "Gloves Warning",
		description = "Color of the gloves slot's overlay warning.",
		position = 31,
		section = warningColorsSection
	)
	default Color glovesColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "bootsColor",
		name = "Boots Warning",
		description = "Color of the boots slot's overlay warning.",
		position = 32,
		section = warningColorsSection
	)
	default Color bootsColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "ringColor",
		name = "Ring Warning",
		description = "Color of the ring slot's overlay warning.",
		position = 33,
		section = warningColorsSection
	)
	default Color ringColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "emptyNotifier",
		name = "Warning Notifier",
		description = "Fires a notification (sound, screen flash or tray) the first time any watched slot"
			+ " fails its check. Re-arms once every watched slot passes again.",
		position = 34,
		section = miscSection
	)
	default Notification getEmptyNotification()
	{
		return Notification.OFF;
	}

	@Alpha
	@ConfigItem(
		keyName = "overlayColor",
		name = "Overlay Color",
		description = "Configures the color for overlay.",
		position = 35,
		section = miscSection
	)
	default Color overlayColor()
	{
		return ComponentConstants.STANDARD_BACKGROUND_COLOR;
	}

}
