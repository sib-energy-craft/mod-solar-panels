package com.github.sib_energy_craft.solar_panels.block;

import com.github.sib_energy_craft.solar_panels.block.entity.LowVoltageSolarPanelBlockEntity;
import com.github.sib_energy_craft.solar_panels.load.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class LowVoltageSolarPanelBlock extends AbstractSolarPanelBlock {

    public LowVoltageSolarPanelBlock(@NotNull Settings settings, int energyPacketSize) {
        super(settings, energyPacketSize);
    }

    @NotNull
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new LowVoltageSolarPanelBlockEntity(pos, state, this);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world,
                                                                  @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return checkType(world, type, Entities.LOW_VOLTAGE_SOLAR_PANEL);
    }
}
