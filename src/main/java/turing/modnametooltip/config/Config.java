package turing.modnametooltip.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.net.command.TextFormatting;
import turing.modnametooltip.ModnameTooltip;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
	private static final Gson GSON = new GsonBuilder()
		.setPrettyPrinting()
		.create();

	private final File file;
	public TextFormatting[] FORMATS = new TextFormatting[]{TextFormatting.BLUE};
	public boolean useID = false;
	public boolean ignoreDiscovered = false;

	public Config() {
		file = new File(FabricLoader.getInstance().getConfigDir().toFile(), ModnameTooltip.MOD_ID + ".json");
	}

	public void load() {
		if (file.exists()) {
			try {
				JsonObject json = GSON.fromJson(new FileReader(file), JsonObject.class);
				useID = json.get("UseModID").getAsBoolean();
				ignoreDiscovered = json.get("IgnoreDiscovery").getAsBoolean();
				String formattingList = json.get("Formatting").getAsString().replace("[", "").replace("]", "").replace(" ", "");
				List<TextFormatting> list = new ArrayList<>();
				String[] split = formattingList.split(",");
				for (String s : split) {
					TextFormatting formatting = TextFormatting.fromChar(s.charAt(1));
					if (formatting != null) {
						list.add(formatting);
					}
				}
				FORMATS = list.toArray(TextFormatting[]::new);
			} catch (IOException ioException) {
				ModnameTooltip.LOGGER.error("Failed to load config file!");
			}
		} else {
			save();
		}
	}

	public void save() {
		try (JsonWriter writer = GSON.newJsonWriter(new FileWriter(file))) {
			writer.beginObject()
				.name("UseModID")
				.value(useID)
				.name("IgnoreDiscovery")
				.value(ignoreDiscovered)
				.name("Formatting")
				.value(Arrays.toString(FORMATS))
				.endObject();
		} catch (IOException ioException) {
			ModnameTooltip.LOGGER.error("Failed to save config file!");
		}
	}
}
