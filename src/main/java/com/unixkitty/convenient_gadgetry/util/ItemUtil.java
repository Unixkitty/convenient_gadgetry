package com.unixkitty.convenient_gadgetry.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemUtil
{
    public static boolean canItemStack(ItemStack a, ItemStack b)
    {
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }
}
