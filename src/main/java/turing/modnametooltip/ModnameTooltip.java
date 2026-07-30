package turing.modnametooltip;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.dependency.Key;
import turniplabs.halplibe.util.toml.Toml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModnameTooltip implements ClientModInitializer {
    public static final String MOD_ID = HalpLibe.registerMod("modnametooltip");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final TomlConfigHandler cfg;
	private static final Toml properties = new Toml("Mod Name Tooltip Config");
	private static final Map<String, ModContainer> MOD_ID_MAP = new HashMap<>();
	public static TextFormatting[] FORMATS;
	public static boolean useID;
	public static boolean ignoreDiscovered;

	static {
		properties.addEntry("Formatting", "A list of text formatting to be applied to the mod name tooltip.\nThere are 21 formats in total with ids 16 to 20 being text styles and all others being colors.\nThe default of 11 represents text that is blue.", "[11]");
		properties.addEntry("UseModID", "If true, the tooltip will display the mod id instead of the mod name.", false);
		properties.addEntry("IgnoreDiscovery", "If true, the tooltip will display even if the item hasn't been discovered yet.\nThis mainly applies to items in the guide book.", false);

		cfg = new TomlConfigHandler(MOD_ID, properties);
	}

    @Override
    public void onInitializeClient() {
		CommonEvents.BEFORE_GAME_START.listen(Key.of(MOD_ID), this::beforeGameStart);
        LOGGER.info("ModnameTooltip initialized.");
    }

	public void beforeGameStart() {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			MOD_ID_MAP.put(mod.getMetadata().getId(), mod);
		}
		useID = cfg.getBoolean("UseModID");
		ignoreDiscovered = cfg.getBoolean("IgnoreDiscovery");
		String listString = cfg.getString("Formatting").replace(" ", "").replace("[", "").replace("]", "");
		String[] split = listString.split(",");
		List<TextFormatting> list = new ArrayList<>(split.length);
        for (String s : split) {
            int id = Integer.parseInt(s);
            if (id < TextFormatting.FORMATTINGS.length) {
                list.add(TextFormatting.get(id));
            }
        }
		FORMATS = list.toArray(TextFormatting[]::new);
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
