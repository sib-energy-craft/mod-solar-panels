package com.github.sib_energy_craft.solar_panels.load;

import com.github.sib_energy_craft.sec_utils.load.ModRegistrar;
import com.github.sib_energy_craft.solar_panels.block.entity.LowVoltageSolarPanelBlockEntity;
import com.github.sib_energy_craft.solar_panels.block.entity.MiddleVoltageSolarPanelBlockEntity;
import com.github.sib_energy_craft.solar_panels.block.entity.SolarPanelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import static com.github.sib_energy_craft.sec_utils.utils.EntityUtils.register;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public final class Entities implements ModRegistrar {
    public static BlockEntityType<SolarPanelBlockEntity> SOLAR_PANEL;
    public static BlockEntityType<LowVoltageSolarPanelBlockEntity> LOW_VOLTAGE_SOLAR_PANEL;
    public static BlockEntityType<MiddleVoltageSolarPanelBlockEntity> MIDDLE_VOLTAGE_SOLAR_PANEL;

    static {
        SOLAR_PANEL = register(Blocks.SOLAR_PANEL, SolarPanelBlockEntity::new);
        LOW_VOLTAGE_SOLAR_PANEL = register(Blocks.LOW_VOLTAGE_SOLAR_PANEL, LowVoltageSolarPanelBlockEntity::new);
        MIDDLE_VOLTAGE_SOLAR_PANEL = register(Blocks.MIDDLE_VOLTAGE_SOLAR_PANEL, MiddleVoltageSolarPanelBlockEntity::new);
    }
}
