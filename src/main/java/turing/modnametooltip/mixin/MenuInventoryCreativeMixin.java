package turing.modnametooltip.mixin;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turing.modnametooltip.ModnameTooltip;

import java.util.List;

@Mixin(value = MenuInventoryCreative.class, remap = false)
public abstract class MenuInventoryCreativeMixin {
	@Shadow
	public static List<ItemStack> creativeContents;

	@Shadow
	protected List<ItemStack> searchedItems;

	@Shadow
	public String searchText;

	@Inject(method = "refillSearchedItemsMatchingCurrentQuery", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/lang/I18n;getInstance()Lnet/minecraft/core/lang/I18n;", shift = At.Shift.AFTER))
	public void injectModnameSearch(CallbackInfo ci) {
		if (searchText.contains("@") && !searchText.substring(searchText.indexOf('@') + 1).isEmpty()) {
			String modSearch = searchText.substring(searchText.indexOf('@') + 1).toLowerCase();
			if (modSearch.contains(" ")) modSearch = modSearch.substring(0, modSearch.indexOf(' '));
			String search = searchText.replace(" @" + modSearch, "").replace("@" + modSearch, "");
			if (!search.isEmpty() && search.charAt(0) == ' ') search = search.substring(1);
			for (ItemStack stack : creativeContents) {
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
