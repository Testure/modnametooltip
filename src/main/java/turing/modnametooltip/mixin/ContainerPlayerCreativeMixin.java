package turing.modnametooltip.mixin;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turing.modnametooltip.ModnameTooltip;

import java.util.List;

@Mixin(value = ContainerPlayerCreative.class, remap = false)
public abstract class ContainerPlayerCreativeMixin {
	@Shadow
	public static int creativeItemsCount;

	@Shadow
	public static List<ItemStack> creativeItems;

	@Shadow
	protected List<ItemStack> searchedItems;

	@Inject(method = "searchPage", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/lang/I18n;getInstance()Lnet/minecraft/core/lang/I18n;", shift = At.Shift.AFTER))
	public void injectModnameSearch(String search, CallbackInfo ci) {
		if (!search.isEmpty() && search.charAt(0) == '@' && !search.substring(1).isEmpty()) {
			for (int i = 0; i < creativeItemsCount; ++i) {
				ItemStack stack = creativeItems.get(i);
				String modName = ModnameTooltip.getModnameForItem(stack);
				if (modName.toLowerCase().contains(search.substring(1).toLowerCase())) {
					this.searchedItems.add(stack);
				}
			}
		}
	}
}
