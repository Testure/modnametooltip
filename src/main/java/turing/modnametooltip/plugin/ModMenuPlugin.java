package turing.modnametooltip.plugin;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.Screen;
import turing.modnametooltip.ModnameTooltip;
import turing.modnametooltip.screen.ConfigScreen;

import java.util.function.Function;

public class ModMenuPlugin implements ModMenuApi {
	@Override
	public String getModId() {
		return ModnameTooltip.MOD_ID;
	}

	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return screen -> new ConfigScreen(screen);
	}
}
