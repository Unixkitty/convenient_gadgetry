package com.unixkitty.convenient_gadgetry.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.TrashcanContainer;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.gemspork.lib.HelperUtil;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TrashcanScreen extends ContainerScreen<TrashcanContainer>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = HelperUtil.prefixResource(ConvenientGadgetry.MODID, "textures/gui/container/trashcan.png");

    public TrashcanScreen(final TrashcanContainer container, final PlayerInventory inventory, final ITextComponent title)
    {
        super(container, inventory, title);
    }

    @Override
    public void render(MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY); //renderHoveredToolTip
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        this.blit(matrixStack, (width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY)
    {
//        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);

        String s = I18n.format(ModBlocks.TRASHCAN.get().getTranslationKey());
        this.font.drawString(matrixStack, s, (float) (this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 0x404040);
        this.font.drawString(matrixStack, I18n.format("container.inventory"), 8.0F, (float) (this.ySize - 96 + 2), 0x404040);
    }
}
