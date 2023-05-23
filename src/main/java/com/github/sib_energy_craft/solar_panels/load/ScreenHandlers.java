package com.github.sib_energy_craft.solar_panels.load;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import com.github.sib_energy_craft.solar_panels.screen.SolarPanelScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.registerHandler;

/**
 * @since 0.0.9
 * @author sibmaks
 */
public final class ScreenHandlers implements DefaultModInitializer {
    public static ScreenHandlerType<SolarPanelScreenHandler> SOLAR_PANEL;

    static {
        SOLAR_PANEL = registerHandler(Identifiers.of("solar_panel"), SolarPanelScreenHandler::new);
    }
}
