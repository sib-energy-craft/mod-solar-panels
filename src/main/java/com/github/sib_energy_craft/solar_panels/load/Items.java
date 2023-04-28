package com.github.sib_energy_craft.solar_panels.load;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.sec_utils.load.ModRegistrar;
import com.github.sib_energy_craft.solar_panels.item.SolarPanelBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

import static com.github.sib_energy_craft.sec_utils.utils.ItemUtils.register;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public final class Items implements ModRegistrar {
    public static final SolarPanelBlockItem SOLAR_PANEL;
    public static final SolarPanelBlockItem LOW_VOLTAGE_SOLAR_PANEL;

    static {
        var settings = new Item.Settings();

        SOLAR_PANEL = register(ItemGroups.FUNCTIONAL,
                Blocks.SOLAR_PANEL,
                it -> new SolarPanelBlockItem(it, settings, Energy.of(1)));

        LOW_VOLTAGE_SOLAR_PANEL = register(ItemGroups.FUNCTIONAL,
                Blocks.LOW_VOLTAGE_SOLAR_PANEL,
                it -> new SolarPanelBlockItem(it, settings, Energy.of(8)));
    }
}
