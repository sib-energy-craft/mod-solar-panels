package com.github.sib_energy_craft.solar_panels.load.client;

import com.github.sib_energy_craft.sec_utils.load.DefaultClientModInitializer;
import com.github.sib_energy_craft.solar_panels.load.ScreenHandlers;
import com.github.sib_energy_craft.solar_panels.screen.SolarPanelScreen;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.registerScreen;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public final class Screens implements DefaultClientModInitializer {
    static {
        registerScreen(ScreenHandlers.SOLAR_PANEL, SolarPanelScreen::new);
    }
}
