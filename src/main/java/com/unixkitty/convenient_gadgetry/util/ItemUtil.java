package com.unixkitty.convenient_gadgetry.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemUtil
{
    public static boolean canItemStack(ItemStack a, ItemStack b)
    {
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected)
    {
        return verifyTag(stack, tag) ? stack.getOrCreateTag().getBoolean(tag) : defaultExpected;
    }

    public static void setBoolean(ItemStack stack, String tag, boolean value)
    {
        stack.getOrCreateTag().putBoolean(tag, value);
    }

    public static int getInt(ItemStack stack, String tag, int fallback)
    {
        return verifyTag(stack, tag) ? stack.getOrCreateTag().getInt(tag) : fallback;
    }

    public static void setInt(ItemStack stack, String tag, int i)
    {
        stack.getOrCreateTag().putInt(tag, i);
    }

    public static boolean verifyTag(ItemStack stack, String tag)
    {
        return !stack.isEmpty() && stack.getOrCreateTag().contains(tag);
    }

    public static void setEntityMotionFromVector(Entity entity, Vector3 originalPosVector, float modifier)
    {
        Vector3 entityVector = Vector3.fromEntityCenter(entity);
        Vector3 finalVector = originalPosVector.subtract(entityVector);

        if (finalVector.mag() > 1)
        {
            finalVector = finalVector.normalize();
        }

        entity.setMotion(finalVector.multiply(modifier).toVec3D());
    }
}
