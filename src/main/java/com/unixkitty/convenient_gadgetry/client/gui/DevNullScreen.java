package com.unixkitty.convenient_gadgetry.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unixkitty.convenient_gadgetry.container.DevNullContainer;
import com.unixkitty.convenient_gadgetry.container.GhostSlot;
import com.unixkitty.convenient_gadgetry.network.MessageHandler;
import com.unixkitty.convenient_gadgetry.network.message.MessageGhostSlot;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;

public class DevNullScreen extends ContainerScreen<DevNullContainer>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    public DevNullScreen(DevNullContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);

        this.imageHeight = 114 + 6 * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
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
        ItemStack stack = Objects.requireNonNull(getMinecraft().player).inventory.getCarried();
        stack = stack.copy().split(hoveredSlot.getMaxStackSize()); // Limit to slot limit
        hoveredSlot.set(stack); // Temporarily update the client for continuity purposes

        MessageHandler.INSTANCE.sendToServer(new MessageGhostSlot(hoveredSlot.index, stack));
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
