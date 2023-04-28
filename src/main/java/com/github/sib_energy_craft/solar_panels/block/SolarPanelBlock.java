package com.github.sib_energy_craft.solar_panels.block;

import com.github.sib_energy_craft.solar_panels.block.entity.SolarPanelBlockEntity;
import com.github.sib_energy_craft.solar_panels.load.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class SolarPanelBlock extends AbstractSolarPanelBlock {

    public SolarPanelBlock(@NotNull Settings settings, int energyPacketSize) {
        super(settings, energyPacketSize);
    }

    @NotNull
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SolarPanelBlockEntity(pos, state, this);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world,
                                                                  @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return checkType(world, type, Entities.SOLAR_PANEL);
    }

    @Override
    protected void openScreen(@NotNull World world,
                              @NotNull BlockPos blockPos,
                              @NotNull PlayerEntity playerEntity) {
        var blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof SolarPanelBlockEntity solarPanelBlockEntity) {
            playerEntity.openHandledScreen(solarPanelBlockEntity);
        }
    }
}
