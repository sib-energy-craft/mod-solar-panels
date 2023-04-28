package com.github.sib_energy_craft.solar_panels.screen;

import com.github.sib_energy_craft.energy_api.screen.ChargeSlot;
import com.github.sib_energy_craft.energy_api.tags.CoreTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

import static com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelBlockEntity.CHARGE_SLOT;
import static com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelProperties.*;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public abstract class AbstractSolarPanelScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    protected AbstractSolarPanelScreenHandler(@NotNull ScreenHandlerType<?> type,
                                              int syncId,
                                              @NotNull PlayerInventory playerInventory) {
        this(type, syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(2));
    }

    protected AbstractSolarPanelScreenHandler(@NotNull ScreenHandlerType<?> type,
                                              int syncId,
                                              @NotNull PlayerInventory playerInventory,
                                              @NotNull Inventory inventory,
                                              @NotNull PropertyDelegate propertyDelegate) {
        super(type, syncId);
        AbstractSolarPanelScreenHandler.checkSize(inventory, 1);
        AbstractSolarPanelScreenHandler.checkDataCount(propertyDelegate, 2);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.addSlot(new ChargeSlot(inventory, CHARGE_SLOT, 80, 26, true));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addProperties(propertyDelegate);
    }

    @Override
    public boolean canUse(@NotNull PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(@NotNull PlayerEntity player,
                               int index) {
        var itemStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (slot.hasStack()) {
            var slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (index == CHARGE_SLOT) {
                if(!insertItem(slotStack, 1, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(CoreTags.isChargeable(slotStack)) {
                if(!insertItem(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(index >= 1 && index < 27) {
                if(!insertItem(slotStack, 27, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(index >= 27 && index < 37) {
                if(!insertItem(slotStack, 1, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, slotStack);
        }
        return itemStack;
    }

    /**
     * Get current block state
     *
     * @return true - block is working, false - otherwise
     */
    public boolean isWorking() {
        return this.propertyDelegate.get(WORKING.ordinal()) == 1;
    }

    /**
     * Get extractor packet size
     *
     * @return packet size
     */
    public int getEnergyPacketSize() {
        return this.propertyDelegate.get(ENERGY_PACKET_SIZE.ordinal());
    }

}

