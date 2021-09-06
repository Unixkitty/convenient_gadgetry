package com.unixkitty.convenient_gadgetry.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.GrinderContainer;
import com.unixkitty.gemspork.lib.HelperUtil;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class GrinderScreen extends ContainerScreen<GrinderContainer>
{
    public static final ResourceLocation BACKGROUND_TEXTURE = HelperUtil.prefixResource(ConvenientGadgetry.MODID, "textures/gui/container/grinder.png");

    public static final int ARROW_X = 87;
    public static final int ARROW_Y = 35;
    public static final int ARROW_WIDTH = 24;
    public static final int ARROW_HEIGHT = 17;

    public static final int ARROW_START_X = 176;
    public static final int ARROW_START_Y = 0;

    public GrinderScreen(final GrinderContainer container, final PlayerInventory inventory, final ITextComponent title)
    {
        super(container, inventory, title);
    }

    @Override
    protected void init()
    {
        super.init();

        //font is only set during init()
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
        this.getMinecraft().getTextureManager().bind(BACKGROUND_TEXTURE);

        this.blit(matrixStack, (width - imageWidth) / 2, (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight);

        //Draw crank progress arrow
        if (this.getCranksLeft() > 0)
        {
            this.blit(matrixStack, this.leftPos + ARROW_X, this.topPos + ARROW_Y, ARROW_START_X, ARROW_START_Y, getCranksScaled(), ARROW_HEIGHT);
        }
    }

/*    //This is temp
    @Override
    protected void renderLabels(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY)
    {
        super.renderLabels(matrixStack, mouseX, mouseY);

        final String cranks = String.format("Cranks: %d / %d", this.getMenu().getCranksDone(), this.getMenu().getCranksNeeded());
        this.font.draw(matrixStack, cranks, this.imageWidth - (this.imageWidth - 6.0F), 18.0f, ModGuiHandler.GUI_HELPER_TEXT_COLOR);
    }*/

    private int getCranksScaled()
    {
        if (getCranksLeft() <= 0 || menu.getCranksNeeded() <= 0)
        {
            return 0;
        }

        return (menu.getCranksNeeded() - getCranksLeft()) * ARROW_WIDTH / menu.getCranksNeeded();
    }

    private int getCranksLeft()
    {
        return menu.getCranksNeeded() - menu.getCranksDone();
    }
}
