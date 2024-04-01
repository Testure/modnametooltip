package turing.modnametooltip;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModnameTooltip implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "modnametooltip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Map<String, ModContainer> MOD_ID_MAP = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.info("ModnameTooltip initialized.");
    }

	@Override
	public void beforeGameStart() {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			MOD_ID_MAP.put(mod.getMetadata().getId(), mod);
		}
	}

	@Override
	public void afterGameStart() {}

	@Nullable
	public static ModContainer getModForItem(ItemStack stack) {
		String name = stack.getItemName();
		if (name.length() < 5) return null;
		name = name.substring(5);
		if (name.contains(".")) name = name.substring(0, name.indexOf('.'));

		return MOD_ID_MAP.get(name);
	}

	public static String getModnameForItem(ItemStack stack) {
		ModContainer mod = getModForItem(stack);
		return mod != null ? mod.getMetadata().getName() : "Minecraft";
	}
}
