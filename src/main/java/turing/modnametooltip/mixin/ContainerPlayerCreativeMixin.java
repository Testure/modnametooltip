package turing.modnametooltip.mixin;

import net.fabricmc.loader.api.ModContainer;
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
		if (search.contains("@") && !search.substring(search.indexOf('@') + 1).isEmpty()) {
			String modSearch = search.substring(search.indexOf('@') + 1).toLowerCase();
			if (modSearch.contains(" ")) modSearch = modSearch.substring(0, modSearch.indexOf(' '));
			search = search.replace(" @" + modSearch, "").replace("@" + modSearch, "");
			if (!search.isEmpty() && search.charAt(0) == ' ') search = search.substring(1);
			ModnameTooltip.LOGGER.info(search);
			for (int i = 0; i < creativeItemsCount; ++i) {
				ItemStack stack = creativeItems.get(i);
				ModContainer mod = ModnameTooltip.getModForItem(stack);
				String modName = mod != null ? mod.getMetadata().getName() : "Minecraft";
				String modId = mod != null ? mod.getMetadata().getId() : "minecraft";
				if ((modName.toLowerCase().contains(modSearch) || modId.toLowerCase().contains(modSearch)) && (search.isEmpty() || stack.getItem().getTranslatedName(stack).toLowerCase().contains(search))) {
					this.searchedItems.add(stack);
				}
			}
		}
	}
}
