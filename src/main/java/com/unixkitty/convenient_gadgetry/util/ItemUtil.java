package com.unixkitty.convenient_gadgetry.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemUtil
{
    public static boolean canItemStack(ItemStack a, ItemStack b)
    {
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    public static ItemStack getStackIfPlayerHas(final ItemStack referenceStack, final PlayerEntity player)
    {
        for (final ItemStack stack : player.inventory.items)
        {
            if (stack.sameItem(referenceStack))
            {
                return stack;
            }
        }

        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public static int getFirstSlotIfPlayerHas(final ItemStack itemStack, final PlayerEntity player)
    {
        ItemStack stack = getStackIfPlayerHas(itemStack, player);

        if (stack != null)
        {
            return player.inventory.findSlotMatchingItem(stack);
        }

        return -1;
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

        entity.setDeltaMovement(finalVector.multiply(modifier).toVec3D());
    }
}
