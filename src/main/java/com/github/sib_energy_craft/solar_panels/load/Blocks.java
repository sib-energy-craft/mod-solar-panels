package com.github.sib_energy_craft.solar_panels.load;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.common.Identified;
import com.github.sib_energy_craft.sec_utils.load.ModRegistrar;
import com.github.sib_energy_craft.solar_panels.block.SolarPanelBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import static com.github.sib_energy_craft.sec_utils.utils.BlockUtils.register;


/**
 * @since 0.0.1
 * @author sibmaks
 */
public final class Blocks implements ModRegistrar {
    public static final Identified<SolarPanelBlock> SOLAR_PANEL;

    static {
        var solarPanelSettings = FabricBlockSettings.of(Material.METAL)
                .sounds(BlockSoundGroup.METAL)
                .strength(5, 6)
                .requiresTool();

        var solarPanelBlock = new SolarPanelBlock(solarPanelSettings, 1);
        SOLAR_PANEL = register(Identifiers.of("solar_panel"), solarPanelBlock);
    }
}
