package com.github.sib_energy_craft.solar_panels.block.entity;

import com.github.sib_energy_craft.solar_panels.block.AbstractSolarPanelBlock;
import com.github.sib_energy_craft.solar_panels.load.Entities;
import com.github.sib_energy_craft.solar_panels.screen.SolarPanelScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class SolarPanelBlockEntity extends AbstractSolarPanelBlockEntity {
    public SolarPanelBlockEntity(BlockPos pos, BlockState state, AbstractSolarPanelBlock block) {
        super(Entities.SOLAR_PANEL, pos, state, block);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.solar_panel");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new SolarPanelScreenHandler(syncId, playerInventory, this, this.propertyMap);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }
}

