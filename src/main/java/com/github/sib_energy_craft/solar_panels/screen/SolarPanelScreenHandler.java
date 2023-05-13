package com.github.sib_energy_craft.solar_panels.screen;

import com.github.sib_energy_craft.solar_panels.load.client.Screens;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class SolarPanelScreenHandler extends AbstractSolarPanelScreenHandler {

    public SolarPanelScreenHandler(int syncId,
                                   @NotNull PlayerInventory playerInventory,
                                   @NotNull Inventory inventory,
                                   @NotNull PropertyDelegate propertyDelegate) {
        super(Screens.SOLAR_PANEL, syncId, playerInventory, inventory, propertyDelegate);
    }

    public SolarPanelScreenHandler(int syncId,
                                   @NotNull PlayerInventory playerInventory,
                                   @NotNull PacketByteBuf packetByteBuf) {
        super(Screens.SOLAR_PANEL, syncId, playerInventory);
    }
}
