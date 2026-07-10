/*
 * Copyright (c) 2020, Jordan Zomerlei <https://github.com/JZomerlei>
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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.MenuAction;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class EquipmentCheckOverlay extends OverlayPanel
{
	private final EquipmentCheckPlugin plugin;
	private final EquipmentCheckConfig config;
	private final Map<EquipmentInventorySlot, Supplier<Color>> slotColors = new EnumMap<>(EquipmentInventorySlot.class);

	@Inject
	private EquipmentCheckOverlay(EquipmentCheckPlugin plugin, EquipmentCheckConfig config)
	{
		super(plugin);
		setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
		this.plugin = plugin;
		this.config = config;
		setupColors();
		addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Equipment Check overlay");
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Map<EquipmentInventorySlot, Boolean> enabledSlots = plugin.getEnabledSlots();
		Map<EquipmentInventorySlot, String> slotNames = plugin.getSlotNames();
		for (EquipmentInventorySlot slot : enabledSlots.keySet())
		{
			String name = slotNames.get(slot);
			if (plugin.isSlotEmpty(slot))
			{
				panelComponent.getChildren().add(TitleComponent.builder()
					.text("NOT wearing a " + name)
					.color(slotColors.get(slot).get())
					.build());
			}
		}
		panelComponent.setBackgroundColor(config.overlayColor());
		return super.render(graphics);
	}

	private void setupColors()
	{
		slotColors.put(EquipmentInventorySlot.HEAD, config::headColor);
		slotColors.put(EquipmentInventorySlot.BODY, config::bodyColor);
		slotColors.put(EquipmentInventorySlot.LEGS, config::legsColor);
		slotColors.put(EquipmentInventorySlot.BOOTS, config::bootsColor);
		slotColors.put(EquipmentInventorySlot.GLOVES, config::glovesColor);
	}
}
