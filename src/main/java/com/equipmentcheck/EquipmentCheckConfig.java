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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("equipmentCheck")
public interface EquipmentCheckConfig extends Config
{
	@ConfigItem(
		keyName = "headCheck",
		name = "Head Check",
		description = "Checks when player's head is equipped",
		position = 1
	)
	default boolean isHeadEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bodyCheck",
		name = "Body Check",
		description = "Checks if player's body is equipped",
		position = 2
	)
	default boolean isBodyEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "legsCheck",
		name = "Legs Check",
		description = "Checks if player's legs are equipped",
		position = 3
	)
	default boolean areLegsEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bootsCheck",
		name = "Boots Check",
		description = "Checks if player's boots are equipped",
		position = 4
	)
	default boolean areBootsEquipped()
	{
		return true;
	}

	@ConfigItem(
		keyName = "glovesCheck",
		name = "Gloves Check",
		description = "Checks if player's gloves are equipped",
		position = 5
	)
	default boolean areGlovesEquipped()
	{
		return true;
	}
}
