package com.github.sib_energy_craft.solar_panels.screen;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class SolarPanelScreen extends HandledScreen<SolarPanelScreenHandler> {
    private static final Identifier TEXTURE = Identifiers.of("textures/gui/container/solar_panel.png");

    public SolarPanelScreen(@NotNull SolarPanelScreenHandler handler,
                            @NotNull PlayerInventory inventory,
                            @NotNull Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(@NotNull MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        int i = this.x;
        int j = this.y;
        if (this.handler.isWorking()) {
            drawTexture(matrices, i + 80, j + 45,  176, 0, 14, 14);
        }
        if(mouseX >= i + 80 && mouseX <= i + 80 + 14 && mouseY >= j + 45 && mouseY <= j + 45 + 14) {
            var output = Text.translatable("energy.out.text", this.handler.getEnergyPacketSize());
            this.renderTooltip(matrices, output, mouseX, mouseY);
        }
    }

    @Override
    public void render(@NotNull MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}