package turing.modnametooltip;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turing.modnametooltip.config.Config;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.dependency.Key;

import java.util.HashMap;
import java.util.Map;

public class ModnameTooltip implements ClientModInitializer {
    public static final String MOD_ID = HalpLibe.registerMod("modnametooltip");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Config config = new Config();
	private static final Map<String, ModContainer> MOD_ID_MAP = new HashMap<>();

    @Override
    public void onInitializeClient() {
		CommonEvents.BEFORE_GAME_START.listen(Key.of(MOD_ID), this::beforeGameStart);
        LOGGER.info("ModnameTooltip initialized.");
    }

	public void beforeGameStart() {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			MOD_ID_MAP.put(mod.getMetadata().getId(), mod);
		}
		config.load();
	}

	@Nullable
	public static ModContainer getModForItem(ItemStack stack) {
		if (stack == null || stack.getItemKey() == null) {
			//LOGGER.error("Attempted to get the mod for an invalid ItemStack. Defaulting to Minecraft.");
			return null;
		}
		String name = stack.getItemKey();
		if (name.length() < 5) return null;
		name = name.substring(5);
		if (name.contains(".")) name = name.substring(0, name.indexOf('.'));

		return MOD_ID_MAP.get(name);
	}

	public static String getModnameForItem(ItemStack stack) {
		ModContainer mod = getModForItem(stack);
		return mod != null ? mod.getMetadata().getName() : "Minecraft";
	}

	public static String getModIdForItem(ItemStack stack) {
		ModContainer mod = getModForItem(stack);
		return mod != null ? mod.getMetadata().getId() : "minecraft";
	}
}
