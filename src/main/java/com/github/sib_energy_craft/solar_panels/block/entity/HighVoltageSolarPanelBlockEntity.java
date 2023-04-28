package com.github.sib_energy_craft.solar_panels.block.entity;

import com.github.sib_energy_craft.solar_panels.block.AbstractSolarPanelBlock;
import com.github.sib_energy_craft.solar_panels.load.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class HighVoltageSolarPanelBlockEntity extends AbstractSolarPanelBlockEntity {
    public HighVoltageSolarPanelBlockEntity(@NotNull BlockPos pos,
                                            @NotNull BlockState state,
                                            @NotNull AbstractSolarPanelBlock block) {
        super(Entities.HIGH_VOLTAGE_SOLAR_PANEL, pos, state, block);
    }

    @Override
    protected @NotNull Text getContainerName() {
        return Text.translatable("container.high_voltage_solar_panel");
    }

}
