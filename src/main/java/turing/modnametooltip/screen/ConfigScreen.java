package turing.modnametooltip.screen;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turing.modnametooltip.ModnameTooltip;

public class ConfigScreen extends Screen {
	private ButtonElement modidButton;
	private ButtonElement discoveryButton;
	private ButtonElement formattingButton;
	private boolean configChanged;

	public ConfigScreen(@Nullable Screen parent) {
		super(parent);
	}

	@Override
	public void init() {
		this.buttons.add(new ButtonElement(1, this.width / 2 - 75, this.height - 52, 150, 20, "Back"));
		this.buttons.add(this.modidButton = new ButtonElement(2, this.width - 75, 25, 40, 20, "false"));
		this.buttons.add(this.discoveryButton = new ButtonElement(3, this.modidButton.xPosition, this.modidButton.yPosition + 25, this.modidButton.width, this.modidButton.height, "false"));
		this.buttons.add(this.formattingButton = new ButtonElement(4, this.width - 85, this.discoveryButton.yPosition + 25, 65, this.modidButton.height, "Minecraft"));
	}

	@Override
	protected void buttonClicked(@NotNull ButtonElement button) {
		if (button.enabled) {
			switch (button.id) {
				case 1 -> {
					if (configChanged) {
						configChanged = false;
						ModnameTooltip.config.save();
					}
					mc.displayScreen(parentScreen);
				}
				case 2 -> toggleBoolean("modid");
				case 3 -> toggleBoolean("discovery");
				case 4 -> toggleFormatting();
			}
		}
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		renderBackground();
		super.render(mx, my, partialTick);

		this.modidButton.displayString = String.valueOf(ModnameTooltip.config.useID);
		this.discoveryButton.displayString = String.valueOf(ModnameTooltip.config.ignoreDiscovered);
		this.formattingButton.displayString = TextFormatting.formatted("Minecraft", ModnameTooltip.config.FORMATS);

		String modidText = "Use mod ID instead of name";
		String discoveryText = "Display mod name regardless of item discovery";
		String formattingText = "Tooltip text formatting";

		drawStringCenteredShadow(fontRenderer, modidText, this.modidButton.xPosition - (this.modidButton.width / 2) - (fontRenderer.stringWidth(modidText) / 2), this.modidButton.yPosition + (this.modidButton.height / 2) - 4, 0xFFFFFFFF);
		drawStringCenteredShadow(fontRenderer, discoveryText, this.discoveryButton.xPosition - (this.discoveryButton.width / 2) - (fontRenderer.stringWidth(discoveryText) / 2), this.discoveryButton.yPosition + (this.discoveryButton.height / 2) - 4, 0xFFFFFFFF);
		drawStringCenteredShadow(fontRenderer, formattingText, this.formattingButton.xPosition - (this.formattingButton.width / 2) - (fontRenderer.stringWidth(formattingText) / 2), this.formattingButton.yPosition + (this.formattingButton.height / 2) - 4, 0xFFFFFFFF);
	}

	private void toggleFormatting() {
		configChanged = true;
		int id = ModnameTooltip.config.FORMATS[ModnameTooltip.config.FORMATS.length - 1].id + 1;
		if (id >= TextFormatting.FORMATTINGS.length) {
			id = 0;
		}
		ModnameTooltip.config.FORMATS[ModnameTooltip.config.FORMATS.length - 1] = TextFormatting.get(id);
	}

	private void toggleBoolean(String name) {
		configChanged = true;
		switch (name) {
			case "modid" -> ModnameTooltip.config.useID = !ModnameTooltip.config.useID;
			case "discovery" -> ModnameTooltip.config.ignoreDiscovered = !ModnameTooltip.config.ignoreDiscovered;
		}
	}
}
