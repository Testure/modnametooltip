package turing.modnametooltip;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigUpdater;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModnameTooltip implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "modnametooltip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final ConfigUpdater updater = ConfigUpdater.fromProperties();
	private static final TomlConfigHandler cfg;
	private static final Toml properties = new Toml("Mod Name Tooltip Config");
	private static final Map<String, ModContainer> MOD_ID_MAP = new HashMap<>();
	public static TextFormatting[] FORMATS;
	public static boolean useID;
	public static boolean ignoreDiscovered;

	static {
		properties.addEntry("Formatting", "A list of text formatting to be applied to the mod name tooltip.\nThere are 21 formats in total with ids 16 to 20 being text styles and all others being colors.\nThe default of 11,20 represents text that is blue and italic.", "[11,20]");
		properties.addEntry("UseModID", "If true, the tooltip will display the mod id instead of the mod name.", false);
		properties.addEntry("IgnoreDiscovery", "If true, the tooltip will display even if the item hasn't been discovered yet.\nThis mainly applies to items in the guide book.", false);

		cfg = new TomlConfigHandler(updater, MOD_ID, properties);
	}

    @Override
    public void onInitialize() {
        LOGGER.info("ModnameTooltip initialized.");
    }

	@Override
	public void beforeGameStart() {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			MOD_ID_MAP.put(mod.getMetadata().getId(), mod);
		}
		useID = cfg.getBoolean("UseModID");
		ignoreDiscovered = cfg.getBoolean("IgnoreDiscovery");
		String listString = cfg.getString("Formatting").replace(" ", "").replace("[", "").replace("]", "");
		String[] split = listString.split(",");
		List<TextFormatting> list = new ArrayList<>();
        for (String s : split) {
            int id = Integer.parseInt(s);
            if (id < TextFormatting.FORMATTINGS.length) {
                list.add(TextFormatting.get(id));
            }
        }
		FORMATS = list.toArray(new TextFormatting[0]);
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

	public static String getModIdForItem(ItemStack stack) {
		ModContainer mod = getModForItem(stack);
		return mod != null ? mod.getMetadata().getId() : "minecraft";
	}
}
