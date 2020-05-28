package com.unixkitty.convenient_gadgetry.api.recipe;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

public interface IGrinderRecipe extends IRecipe<IInventory>
{
    ResourceLocation TYPE_ID = new ResourceLocation(ConvenientGadgetry.MODID, "grinding");

    int CRANKS_DEFAULT = 4;

    default int getCranksRequired()
    {
        return CRANKS_DEFAULT;
    }

    List<Pair<ItemStack, Float>> getPossibleOutputsWithChances();

    @Nonnull
    @Override
    default IRecipeType<?> getType()
    {
        return Registry.RECIPE_TYPE.getValue(TYPE_ID).get();
    }

    @Deprecated
    @Override
    default ItemStack getCraftingResult(IInventory inv)
    {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canFit(int width, int height)
    {
        return true;
    }
}
