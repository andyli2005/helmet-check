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
		description = "Checks when player's head is equipped",
		position = 1,
		section = equipmentSlotsSection
	)
	default boolean isHeadEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "capeCheck",
		name = "Cape",
		description = "Checks if player's cape is equipped",
		position = 2,
		section = equipmentSlotsSection
	)
	default boolean isCapeEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "amuletCheck",
		name = "Amulet",
		description = "Checks if player's amulet is equipped",
		position = 3,
		section = equipmentSlotsSection
	)
	default boolean isAmuletEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "ammoCheck",
		name = "Ammo",
		description = "Checks if player's ammo is equipped",
		position = 4,
		section = equipmentSlotsSection
	)
	default boolean isAmmoEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "weaponCheck",
		name = "Weapon",
		description = "Checks if player's weapon is equipped",
		position = 5,
		section = equipmentSlotsSection
	)
	default boolean isWeaponEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bodyCheck",
		name = "Body",
		description = "Checks if player's body is equipped",
		position = 6,
		section = equipmentSlotsSection
	)
	default boolean isBodyEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "shieldCheck",
		name = "Shield",
		description = "Checks if player's shield is equipped",
		position = 7,
		section = equipmentSlotsSection
	)
	default boolean isShieldEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "legsCheck",
		name = "Legs",
		description = "Checks if player's legs are equipped",
		position = 8,
		section = equipmentSlotsSection
	)
	default boolean areLegsEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "glovesCheck",
		name = "Gloves",
		description = "Checks if player's gloves are equipped",
		position = 9,
		section = equipmentSlotsSection
	)
	default boolean areGlovesEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bootsCheck",
		name = "Boots",
		description = "Checks if player's boots are equipped",
		position = 10,
		section = equipmentSlotsSection
	)
	default boolean areBootsEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "ringCheck",
		name = "Ring",
		description = "Checks if player's ring is equipped",
		position = 11,
		section = equipmentSlotsSection
	)
	default boolean isRingEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "headColor",
		name = "Empty Head Warning",
		description = "Configures the color for the warning for empty head slot.",
		position = 12,
		section = warningColorsSection
	)
	default Color headColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "capeColor",
		name = "Empty Cape Warning",
		description = "Configures the color for the warning for empty cape slot.",
		position = 13,
		section = warningColorsSection
	)
	default Color capeColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "amuletColor",
		name = "Empty Amulet Warning",
		description = "Configures the color for the warning for empty amulet slot.",
		position = 14,
		section = warningColorsSection
	)
	default Color amuletColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "ammoColor",
		name = "Empty Ammo Warning",
		description = "Configures the color for the warning for empty ammo slot.",
		position = 15,
		section = warningColorsSection
	)
	default Color ammoColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "weaponColor",
		name = "Empty Weapon Warning",
		description = "Configures the color for the warning for empty weapon slot.",
		position = 16,
		section = warningColorsSection
	)
	default Color weaponColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "bodyColor",
		name = "Empty Body Warning",
		description = "Configures the color for the warning for empty body slot.",
		position = 17,
		section = warningColorsSection
	)
	default Color bodyColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "shieldColor",
		name = "Empty Shield Warning",
		description = "Configures the color for the warning for empty shield slot.",
		position = 18,
		section = warningColorsSection
	)
	default Color shieldColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "legsColor",
		name = "Empty Legs Warning",
		description = "Configures the color for the warning for empty legs slot.",
		position = 19,
		section = warningColorsSection
	)
	default Color legsColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "glovesColor",
		name = "Empty Gloves Warning",
		description = "Configures the color for the warning for empty gloves slot.",
		position = 20,
		section = warningColorsSection
	)
	default Color glovesColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "bootsColor",
		name = "Empty Boots Warning",
		description = "Configures the color for the warning for empty boots slot.",
		position = 21,
		section = warningColorsSection
	)
	default Color bootsColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "ringColor",
		name = "Empty Ring Warning",
		description = "Configures the color for the warning for empty ring slot.",
		position = 22,
		section = warningColorsSection
	)
	default Color ringColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "emptyNotifier",
		name = "Empty Slot Notifier",
		description = "Configures if empty equipment slot(s) notifications are enabled.",
		position = 23,
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
		position = 24,
		section = miscSection
	)
	default Color overlayColor()
	{
		return ComponentConstants.STANDARD_BACKGROUND_COLOR;
	}

}
