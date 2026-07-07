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
package com.helmetcheck;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Helmet Check",
	description = "Alerts you when you have nothing equipped in your head slot",
	tags = {"hint", "gear", "head", "overlay"}
)
public class HelmetCheckPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HelmetCheckOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClientThread clientThread;

	@Inject
	private HelmetCheckConfig config;

	@Getter
	private boolean flag = true;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		clientThread.invokeLater(() ->
		{
			final ItemContainer current = client.getItemContainer(InventoryID.WORN);
			if (current != null && isHelmetOff(current))
			{
				printReminder();
				flag = false;
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	private void printReminder()
	{
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", config.reminder(), null);
	}

	private boolean isHelmetOff(ItemContainer equipment)
	{
		return equipment.getItem(EquipmentInventorySlot.HEAD.getSlotIdx()) == null;
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
	{
		if (itemContainerChanged.getContainerId() == InventoryID.WORN)
		{
			final ItemContainer worn = itemContainerChanged.getItemContainer();
			if (isHelmetOff(worn))
			{
				if (flag)
				{
					printReminder();
					flag = !flag;
				}
			}
			else
			{
				flag = true;
			}
		}
	}

	@Provides
	HelmetCheckConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HelmetCheckConfig.class);
	}
}
