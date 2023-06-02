package com.github.sib_energy_craft.solar_panels.screen;

import com.github.sib_energy_craft.energy_api.screen.ChargeSlot;
import com.github.sib_energy_craft.energy_api.tags.CoreTags;
import com.github.sib_energy_craft.sec_utils.screen.SlotsScreenHandler;
import com.github.sib_energy_craft.sec_utils.screen.slot.SlotGroupMetaBuilder;
import com.github.sib_energy_craft.sec_utils.screen.slot.SlotGroupsMeta;
import com.github.sib_energy_craft.sec_utils.screen.slot.SlotGroupsMetaBuilder;
import com.github.sib_energy_craft.sec_utils.screen.slot.SlotTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

import static com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelBlockEntity.CHARGE_SLOT;
import static com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelProperties.ENERGY_PACKET_SIZE;
import static com.github.sib_energy_craft.solar_panels.block.entity.AbstractSolarPanelProperties.WORKING;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public abstract class AbstractSolarPanelScreenHandler extends SlotsScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final SlotGroupsMeta slotGroupsMeta;

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
        this.slotGroupsMeta = buildSlots(playerInventory, inventory);
        this.addProperties(propertyDelegate);
    }

    private @NotNull SlotGroupsMeta buildSlots(@NotNull PlayerInventory playerInventory,
                                               @NotNull Inventory blockInventory) {
        int globalSlotIndex = 0;
        var slotGroupsBuilder = SlotGroupsMetaBuilder.builder();

        int quickAccessSlots = 9;
        {
            var slotQuickAccessGroupBuilder = SlotGroupMetaBuilder.builder(SlotTypes.QUICK_ACCESS);
            for (int i = 0; i < quickAccessSlots; ++i) {
                slotQuickAccessGroupBuilder.addSlot(globalSlotIndex++, i);
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
            }
            var quickAccessSlotGroup = slotQuickAccessGroupBuilder.build();
            slotGroupsBuilder.add(quickAccessSlotGroup);
        }

        {
            var slotPlayerGroupBuilder = SlotGroupMetaBuilder.builder(SlotTypes.PLAYER_INVENTORY);
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    int index = j + i * 9 + quickAccessSlots;
                    slotPlayerGroupBuilder.addSlot(globalSlotIndex++, index);
                    this.addSlot(new Slot(playerInventory, index, 8 + j * 18, 84 + i * 18));
                }
            }
            var playerSlotGroup = slotPlayerGroupBuilder.build();
            slotGroupsBuilder.add(playerSlotGroup);
        }

        {
            var slotToolsGroupBuilder = SlotGroupMetaBuilder.builder(SolarPanelSlotTypes.SOLAR_PANEL);

            slotToolsGroupBuilder.addSlot(globalSlotIndex, CHARGE_SLOT);
            var chargeSlot = new ChargeSlot(blockInventory, CHARGE_SLOT, 80, 26, true);
            this.addSlot(chargeSlot);

            var blockSlotGroup = slotToolsGroupBuilder.build();
            slotGroupsBuilder.add(blockSlotGroup);
        }

        return slotGroupsBuilder.build();
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

            var slotMeta = this.slotGroupsMeta.getByGlobalSlotIndex(index);
            if(slotMeta != null) {
                var slotType = slotMeta.getSlotType();
                if (slotType == SolarPanelSlotTypes.SOLAR_PANEL) {
                    if (!insertItem(slotGroupsMeta, slotStack, SlotTypes.PLAYER_INVENTORY, SlotTypes.QUICK_ACCESS)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    var inserted = false;
                    if (CoreTags.isChargeable(itemStack)) {
                        var chargeGlobalIndex = this.slotGroupsMeta.getGlobalIndexByLocal(SolarPanelSlotTypes.SOLAR_PANEL,
                                CHARGE_SLOT);
                        if (chargeGlobalIndex != null &&
                                insertItem(slotStack, chargeGlobalIndex, chargeGlobalIndex + 1, false)) {
                            inserted = true;
                        }
                    }
                    if (!inserted) {
                        if (slotType == SlotTypes.QUICK_ACCESS) {
                            if (!insertItem(slotGroupsMeta, slotStack, SlotTypes.PLAYER_INVENTORY)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (slotType == SlotTypes.PLAYER_INVENTORY) {
                            if (!insertItem(slotGroupsMeta, slotStack, SlotTypes.QUICK_ACCESS)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                }
            }
            slot.onQuickTransfer(slotStack, itemStack);
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

