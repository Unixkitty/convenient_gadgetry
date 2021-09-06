package com.unixkitty.convenient_gadgetry.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.TrashcanContainer;
import com.unixkitty.gemspork.lib.HelperUtil;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class TrashcanScreen extends ContainerScreen<TrashcanContainer>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = HelperUtil.prefixResource(ConvenientGadgetry.MODID, "textures/gui/container/trashcan.png");

    public TrashcanScreen(final TrashcanContainer container, final PlayerInventory inventory, final ITextComponent title)
    {
        super(container, inventory, title);
    }

    @Override
    protected void init()
    {
        super.init();

        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bind(BACKGROUND_TEXTURE);

        this.blit(matrixStack, (width - imageWidth) / 2, (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight);
    }
}
