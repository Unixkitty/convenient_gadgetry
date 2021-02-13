package com.unixkitty.convenient_gadgetry.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class GhostSlot extends SlotItemHandler
{
    public GhostSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public void putStack(@Nonnull ItemStack stack)
    {
        super.putStack(stack);
    }

    @Override
    public boolean canTakeStack(PlayerEntity player)
    {
        return false;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return true;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack)
    {
        return 1;
    }
}
