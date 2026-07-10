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
import net.runelite.client.config.Notification;
import net.runelite.client.ui.overlay.components.ComponentConstants;

@ConfigGroup("equipmentCheck")
public interface EquipmentCheckConfig extends Config
{
	@ConfigItem(
		keyName = "headCheck",
		name = "Head",
		description = "Checks when player's head is equipped",
		position = 1
	)
	default boolean isHeadEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "headColor",
		name = "Empty Head Warning",
		description = "Configures the color for the warning for empty head slot.",
		position = 2
	)
	default Color headColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "bodyCheck",
		name = "Body",
		description = "Checks if player's body is equipped",
		position = 3
	)
	default boolean isBodyEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bodyColor",
		name = "Empty Body Warning",
		description = "Configures the color for the warning for empty body slot.",
		position = 4
	)
	default Color bodyColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "legsCheck",
		name = "Legs",
		description = "Checks if player's legs are equipped",
		position = 5
	)
	default boolean areLegsEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "legsColor",
		name = "Empty Legs Warning",
		description = "Configures the color for the warning for empty legs slot.",
		position = 6
	)
	default Color legsColor()
	{
		return Color.RED;
	}


	@ConfigItem(
		keyName = "bootsCheck",
		name = "Boots",
		description = "Checks if player's boots are equipped",
		position = 7
	)
	default boolean areBootsEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bootsColor",
		name = "Empty Boots Warning",
		description = "Configures the color for the warning for empty boots slot.",
		position = 8
	)
	default Color bootsColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "glovesCheck",
		name = "Gloves",
		description = "Checks if player's gloves are equipped",
		position = 9
	)
	default boolean areGlovesEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "glovesColor",
		name = "Empty Gloves Warning",
		description = "Configures the color for the warning for empty gloves slot.",
		position = 10
	)
	default Color glovesColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "emptyNotifier",
		name = "Empty Slot Notifier",
		description = "Configures if empty equipment slot(s) notifications are enabled.",
		position = 11
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
		position = 12
	)
	default Color overlayColor()
	{
		return ComponentConstants.STANDARD_BACKGROUND_COLOR;
	}

}
