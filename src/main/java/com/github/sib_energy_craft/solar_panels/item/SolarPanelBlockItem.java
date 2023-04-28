package com.github.sib_energy_craft.solar_panels.item;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.solar_panels.block.AbstractSolarPanelBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class SolarPanelBlockItem extends BlockItem {
    private final int output;

    public SolarPanelBlockItem(@NotNull AbstractSolarPanelBlock block,
                               @NotNull Settings settings,
                               @NotNull Energy output) {
        super(block, settings);
        this.output = output.intValue();
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack,
                              @Nullable World world,
                              @NotNull List<Text> tooltip,
                              @NotNull TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        int textColor = Color.GRAY.getRGB();
        var textStyle = Style.EMPTY.withColor(textColor);
        tooltip.add(Text.translatable("attribute.name.sib_energy_craft.output_eu", output).setStyle(textStyle));
    }
}
