package com.unixkitty.convenient_gadgetry.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.GrinderContainer;
import com.unixkitty.gemspork.lib.HelperUtil;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
    public void render(final int mouseX, final int mouseY, final float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        this.blit((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);

        //Draw crank progress arrow
        if (this.getCranksLeft() > 0)
        {
            this.blit(this.guiLeft + ARROW_X, this.guiTop + ARROW_Y, ARROW_START_X, ARROW_START_Y, getCranksScaled(), ARROW_HEIGHT);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String title = this.title.getFormattedText();
        this.font.drawString(title, (float) (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 0x404040);

        /*String cranks = String.format("Cranks: %d / %d", this.container.getCranksDone(), this.container.getCranksNeeded());
        this.font.drawString(cranks, this.xSize - (this.xSize - 6.0F), 18.0f, 0x404040);*/

        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 0x404040);
    }

    private int getCranksScaled()
    {
        if (getCranksLeft() <= 0 || container.getCranksNeeded() <= 0)
        {
            return 0;
        }

        return (container.getCranksNeeded() - getCranksLeft()) * ARROW_WIDTH / container.getCranksNeeded();
    }

    private int getCranksLeft()
    {
        return container.getCranksNeeded() - container.getCranksDone();
    }
}
