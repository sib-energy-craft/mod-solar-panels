package com.github.sib_energy_craft.solar_panels.block.entity;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.EnergyOffer;
import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.energy_api.supplier.EnergySupplier;
import com.github.sib_energy_craft.energy_api.tags.CoreTags;
import com.github.sib_energy_craft.sec_utils.screen.PropertyMap;
import com.github.sib_energy_craft.sec_utils.utils.BlockEntityUtils;
import com.github.sib_energy_craft.solar_panels.block.AbstractSolarPanelBlock;
import com.github.sib_energy_craft.solar_panels.screen.SolarPanelScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelProperties.ENERGY_PACKET_SIZE;
import static com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelProperties.WORKING;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public abstract class AbstractSolarPanelBlockEntity extends LockableContainerBlockEntity
        implements SidedInventory, ExtendedScreenHandlerFactory, EnergySupplier {
    private static final Set<Direction> SUPPLYING_DIRECTIONS = Set.of(
            Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH, Direction.DOWN
    );

    public static final int CHARGE_SLOT = 0;

    public static final int[] ACTIVE_SLOTS = {
            CHARGE_SLOT
    };

    protected DefaultedList<ItemStack> inventory;

    private volatile boolean working;
    private volatile Energy energy;
    private final AbstractSolarPanelBlock block;

    protected final PropertyMap<AbstractSolarPanelProperties> propertyMap;

    protected AbstractSolarPanelBlockEntity(@NotNull BlockEntityType<?> blockEntityType,
                                            @NotNull BlockPos pos,
                                            @NotNull BlockState state,
                                            @NotNull AbstractSolarPanelBlock block) {
        super(blockEntityType, pos, state);
        this.block = block;
        this.propertyMap = new PropertyMap<>(AbstractSolarPanelProperties.class);
        this.propertyMap.add(ENERGY_PACKET_SIZE, () -> block.getEnergyPerTick().intValue());
        this.propertyMap.add(WORKING, () -> working ? 1 : 0);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public void writeScreenOpeningData(@NotNull ServerPlayerEntity player,
                                       @NotNull PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    protected @NotNull ScreenHandler createScreenHandler(int syncId,
                                                         @NotNull PlayerInventory playerInventory) {
        return new SolarPanelScreenHandler(syncId, playerInventory, this, this.propertyMap);
    }

    public static void tick(@NotNull World world,
                            @NotNull BlockPos pos,
                            @NotNull BlockState state,
                            @NotNull AbstractSolarPanelBlockEntity blockEntity) {
        blockEntity.energy = Energy.ZERO;
        if (world.isClient || !world.getDimension().hasSkyLight()) {
            blockEntity.working = false;
            return;
        }

        long timeOfDay = world.getTimeOfDay();
        int light = world.getLightLevel(blockEntity.getPos().up(), 0);

        blockEntity.working = timeOfDay >= 0 && timeOfDay <= 12000 && light >= 15;

        if(blockEntity.working) {
            blockEntity.energy = Energy.of(blockEntity.block.getEnergyPerTick());

            var chargingStack = blockEntity.inventory.get(CHARGE_SLOT);
            var chargingStackItem = chargingStack.getItem();
            if(!chargingStack.isEmpty() &&
                    (chargingStackItem instanceof ChargeableItem chargeableItem) &&
                    chargeableItem.hasFreeSpace(chargingStack)) {
                var lastEnergy = chargeableItem.charge(chargingStack, blockEntity.energy.intValue());
                blockEntity.energy = Energy.of(lastEnergy);
            }
            blockEntity.tick(blockEntity);
        }

        AbstractSolarPanelBlockEntity.markDirty(world, pos, state);
    }

    @Override
    public int[] getAvailableSlots(@NotNull Direction side) {
        return ACTIVE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot,
                             @NotNull ItemStack stack,
                             @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot,
                              @NotNull ItemStack stack,
                              @NotNull Direction dir) {
        return true;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @NotNull
    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @NotNull
    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @NotNull
    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, @NotNull ItemStack stack) {
        var itemStack = this.inventory.get(slot);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        boolean bl = !stack.isEmpty() && stack.isItemEqual(itemStack) && ItemStack.areNbtEqual(stack, itemStack);
        if (slot == CHARGE_SLOT && !bl) {
            this.markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(@NotNull PlayerEntity player) {
        return BlockEntityUtils.canPlayerUse(this, player);
    }

    @Override
    public boolean isValid(int slot, @NotNull ItemStack stack) {
        return CoreTags.isChargeable(stack);
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @NotNull
    @Override
    public Set<Direction> getSupplyingDirections() {
        return SUPPLYING_DIRECTIONS;
    }

    @NotNull
    @Override
    public EnergyOffer createOffer() {
        var energyPerTick = block.getEnergyPerTick();
        return new EnergyOffer(this, Energy.of(energyPerTick));
    }

    @Override
    public synchronized boolean supplyEnergy(@NotNull Energy energy) {
        if(energy.compareTo(this.energy) <= 0) {
            this.energy = energy.subtract(energy);
            return true;
        }
        return false;
    }

}

