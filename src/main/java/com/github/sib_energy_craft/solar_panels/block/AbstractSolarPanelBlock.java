package com.github.sib_energy_craft.solar_panels.block;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelBlockEntity;
import lombok.Getter;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public abstract class AbstractSolarPanelBlock extends BlockWithEntity {

    @Getter
    private final Energy energyPerTick;

    protected AbstractSolarPanelBlock(@NotNull Settings settings, int energyPerTick) {
        super(settings);
        this.energyPerTick = Energy.of(energyPerTick);
    }

    @NotNull
    @Override
    public ActionResult onUse(@NotNull BlockState state,
                              @NotNull World world,
                              @NotNull BlockPos pos,
                              @NotNull PlayerEntity player,
                              @NotNull Hand hand,
                              @NotNull BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        this.openScreen(world, pos, player);
        return ActionResult.CONSUME;
    }

    protected void openScreen(@NotNull World world,
                              @NotNull BlockPos blockPos,
                              @NotNull PlayerEntity playerEntity) {
        var blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof AbstractSolarPanelBlockEntity solarPanelBlockEntity) {
            playerEntity.openHandledScreen(solarPanelBlockEntity);
        }
    }

    @Override
    public void onPlaced(@NotNull World world,
                         @NotNull BlockPos pos,
                         @NotNull BlockState state,
                         @Nullable LivingEntity placer,
                         @NotNull ItemStack itemStack) {
        if (!(world.getBlockEntity(pos) instanceof AbstractSolarPanelBlockEntity entity)) {
            return;
        }
        if(itemStack.hasCustomName()) {
            entity.setCustomName(itemStack.getName());
        }
    }

    @Override
    public void onStateReplaced(@NotNull BlockState state,
                                @NotNull World world,
                                @NotNull BlockPos pos,
                                @NotNull BlockState newState,
                                boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AbstractSolarPanelBlockEntity solarPanelBlockEntity) {
            if (world instanceof ServerWorld) {
                ItemScatterer.spawn(world, pos, solarPanelBlockEntity);
            }
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public boolean hasComparatorOutput(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(@NotNull BlockState state,
                                   @NotNull World world,
                                   @NotNull BlockPos pos) {
        var blockEntity = world.getBlockEntity(pos);
        return ScreenHandler.calculateComparatorOutput(blockEntity);
    }

    @NotNull
    @Override
    public BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    protected static <T extends BlockEntity, E extends AbstractSolarPanelBlockEntity> BlockEntityTicker<T> checkType(
            @NotNull World world,
            @NotNull BlockEntityType<T> givenType,
            @NotNull BlockEntityType<E> expectedType) {
        return world.isClient ? null : BlockWithEntity.checkType(givenType, expectedType,
                AbstractSolarPanelBlockEntity::tick);
    }
}
