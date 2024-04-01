package turing.modnametooltip;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class ModnameTooltip implements ModInitializer {
    public static final String MOD_ID = "modnametooltip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("ExampleMod initialized.");
    }

	@Nullable
	public static ModContainer getModForItem(ItemStack stack) {
		String name = stack.getItemName();
		if (name.length() < 5) return null;
		name = name.substring(5);
		if (name.contains(".")) name = name.substring(0, name.indexOf('.'));

		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			if (mod.getMetadata().getId().equals(name)) {
				return mod;
			}
		}

		return null;
	}

	public static String getModnameForItem(ItemStack stack) {
		ModContainer mod = getModForItem(stack);
		return mod != null ? mod.getMetadata().getName() : "Minecraft";
	}
}
