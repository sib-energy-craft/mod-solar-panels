package com.github.sib_energy_craft.solar_panels.load.client;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.load.DefaultClientModInitializer;
import com.github.sib_energy_craft.solar_panels.screen.SolarPanelScreen;
import com.github.sib_energy_craft.solar_panels.screen.SolarPanelScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.register;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public final class Screens implements DefaultClientModInitializer {
    public static ScreenHandlerType<SolarPanelScreenHandler> SOLAR_PANEL;

    static {
        SOLAR_PANEL = register(Identifiers.of("solar_panel"), SolarPanelScreenHandler::new, SolarPanelScreen::new);
    }
}
