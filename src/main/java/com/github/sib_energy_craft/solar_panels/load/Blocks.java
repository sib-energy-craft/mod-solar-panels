package com.github.sib_energy_craft.solar_panels.load;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.common.Identified;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import com.github.sib_energy_craft.solar_panels.block.HighVoltageSolarPanelBlock;
import com.github.sib_energy_craft.solar_panels.block.LowVoltageSolarPanelBlock;
import com.github.sib_energy_craft.solar_panels.block.MiddleVoltageSolarPanelBlock;
import com.github.sib_energy_craft.solar_panels.block.SolarPanelBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import static com.github.sib_energy_craft.sec_utils.utils.BlockUtils.register;


/**
 * @since 0.0.1
 * @author sibmaks
 */
public final class Blocks implements DefaultModInitializer {
    public static final Identified<SolarPanelBlock> SOLAR_PANEL;
    public static final Identified<LowVoltageSolarPanelBlock> LOW_VOLTAGE_SOLAR_PANEL;
    public static final Identified<MiddleVoltageSolarPanelBlock> MIDDLE_VOLTAGE_SOLAR_PANEL;
    public static final Identified<HighVoltageSolarPanelBlock> HIGH_VOLTAGE_SOLAR_PANEL;

    static {
        var solarPanelSettings = FabricBlockSettings.of(Material.METAL)
                .sounds(BlockSoundGroup.METAL)
                .strength(5, 6)
                .requiresTool();

        var solarPanelBlock = new SolarPanelBlock(solarPanelSettings, 1);
        SOLAR_PANEL = register(Identifiers.of("solar_panel"), solarPanelBlock);

        var lowVoltageSolarPanelBlock = new LowVoltageSolarPanelBlock(solarPanelSettings, 8);
        LOW_VOLTAGE_SOLAR_PANEL = register(Identifiers.of("low_voltage_solar_panel"), lowVoltageSolarPanelBlock);

        var middleVoltageSolarPanel = new MiddleVoltageSolarPanelBlock(solarPanelSettings, 64);
        MIDDLE_VOLTAGE_SOLAR_PANEL = register(Identifiers.of("middle_voltage_solar_panel"), middleVoltageSolarPanel);

        var highVoltageSolarPanel = new HighVoltageSolarPanelBlock(solarPanelSettings, 512);
        HIGH_VOLTAGE_SOLAR_PANEL = register(Identifiers.of("high_voltage_solar_panel"), highVoltageSolarPanel);
    }
}
