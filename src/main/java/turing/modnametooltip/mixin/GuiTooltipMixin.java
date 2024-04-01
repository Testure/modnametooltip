package turing.modnametooltip.mixin;

import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import turing.modnametooltip.ModnameTooltip;

@Mixin(value = GuiTooltip.class, remap = false)
public class GuiTooltipMixin {
	@Inject(method = "getTooltipText(Lnet/minecraft/core/item/ItemStack;ZLnet/minecraft/core/player/inventory/slot/Slot;)Ljava/lang/String;", at = @At(value = "TAIL", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	public void injectTooltip(ItemStack itemStack, boolean showDescription, Slot slot, CallbackInfoReturnable<String> cir, I18n trans, StringBuilder text) {
		boolean discovered = slot == null || slot.discovered;
		if (discovered) {
			String modName = ModnameTooltip.getModnameForItem(itemStack);
			modName = TextFormatting.formatted(modName, TextFormatting.BLUE, TextFormatting.ITALIC);
			if (text.indexOf(modName) == -1) {
				if (!text.substring(text.length() - 1, text.length()).equals("\n")) text.append("\n");
				text.append(modName);
			}
		}
	}
}
