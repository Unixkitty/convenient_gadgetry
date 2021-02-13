package com.unixkitty.convenient_gadgetry.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.DevNullContainer;
import com.unixkitty.convenient_gadgetry.container.GhostSlot;
import com.unixkitty.convenient_gadgetry.network.MessageHandler;
import com.unixkitty.convenient_gadgetry.network.message.MessageGhostSlot;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class DevNullScreen extends ContainerScreen<DevNullContainer>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    private final int inventoryRows = 6;

    public DevNullScreen(DevNullContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);

        this.ySize = 114 + 6 * 18;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        /*int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.blit(matrixStack, x, y, 0, 0, this.xSize, 6 * 18 + 17);
        this.blit(matrixStack, x, y + 6 * 18 + 17, 0, 125, this.xSize, 96);*/
        this.blit(matrixStack, (width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY)
    {
        String s = I18n.format("text." + ConvenientGadgetry.MODID + ".dev_null_gui");
        this.font.drawString(matrixStack, s, (float) (this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 0x404040);
        this.font.drawString(matrixStack, I18n.format("container.inventory"), 8.0F, (float) (this.ySize - 96 + 2), 0x404040);
    }

    //Code from com.direwolf20.mininggadgets.client.screens.FilterScreen
    @Override
    public boolean mouseClicked(double x, double y, int btn)
    {
        if (hoveredSlot == null || !(hoveredSlot instanceof GhostSlot))
        {
            return super.mouseClicked(x, y, btn);
        }

        // By splitting the stack we can get air easily :) perfect removal basically
        ItemStack stack = getMinecraft().player.inventory.getItemStack();
        stack = stack.copy().split(hoveredSlot.getSlotStackLimit()); // Limit to slot limit
        hoveredSlot.putStack(stack); // Temporarily update the client for continuity purposes

        MessageHandler.INSTANCE.sendToServer(new MessageGhostSlot(hoveredSlot.slotNumber, stack));
        return true;
    }

    @Override
    public boolean mouseReleased(double x, double y, int btn)
    {
        if (hoveredSlot == null || !(hoveredSlot instanceof GhostSlot))
        {
            return super.mouseReleased(x, y, btn);
        }

        return true;
    }

    @Override
    public boolean mouseScrolled(double x, double y, double amt)
    {
        if (hoveredSlot == null || !(hoveredSlot instanceof GhostSlot))
        {
            return super.mouseScrolled(x, y, amt);
        }

        return true;
    }
}
