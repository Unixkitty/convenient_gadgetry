package com.unixkitty.convenient_gadgetry.datagen.recipe;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModItems;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.gemspork.lib.datagen.recipe.SmeltingRecipeProvider;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class SmeltingRecipes extends SmeltingRecipeProvider
{
    public SmeltingRecipes(DataGenerator generatorIn)
    {
        super(ConvenientGadgetry.MODID, generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        basicCooking(consumer, Dust.IRON.asTag(), Items.IRON_INGOT, "iron_ingot_from_dust");
        basicBlasting(consumer, Dust.IRON.asTag(), Items.IRON_INGOT, "iron_ingot_from_dust");

        basicCooking(consumer, Dust.GOLD.asTag(), Items.GOLD_INGOT, "gold_ingot_from_dust");
        basicBlasting(consumer, Dust.GOLD.asTag(), Items.GOLD_INGOT, "gold_ingot_from_dust");

        basicCooking(consumer, Dust.SULFUR.asTag(), Items.GUNPOWDER, "gunpowder_from_sulfur");
        basicCooking(consumer, Dust.FLOUR.asTag(), Items.BREAD, "bread_from_flour");
        basicSmoking(consumer, Dust.FLOUR.asTag(), Items.BREAD, "bread_from_flour");

        basicCooking(consumer, Dust.MAGNETIC.asTag(), ModItems.INGOT_MAGNETIC.get(), "magnetic_ingot_from_dust");
        basicBlasting(consumer, Dust.MAGNETIC.asTag(), ModItems.INGOT_MAGNETIC.get(), "magnetic_ingot_from_dust");
    }

    private void basicCooking(Consumer<IFinishedRecipe> consumer, ITag<Item> input, IItemProvider result, String name)
    {
        CookingRecipeBuilder.smeltingRecipe(
                Ingredient.fromTag(input),
                result,
                0,
                200)
                .addCriterion("has_item", hasItem(input))
                .build(consumer, new ResourceLocation(ConvenientGadgetry.MODID, "smelting/" + name));
    }

    private void basicBlasting(Consumer<IFinishedRecipe> consumer, ITag<Item> input, IItemProvider result, String name)
    {
        CookingRecipeBuilder.blastingRecipe(
                Ingredient.fromTag(input),
                result,
                0,
                100)
                .addCriterion("has_item", hasItem(input))
                .build(consumer, new ResourceLocation(ConvenientGadgetry.MODID, "blasting/" + name));
    }

    private void basicSmoking(Consumer<IFinishedRecipe> consumer, ITag<Item> input, IItemProvider result, String name)
    {
        CookingRecipeBuilder.cookingRecipe(
                Ingredient.fromTag(input),
                result,
                0.35F,
                100,
                IRecipeSerializer.SMOKING).
                addCriterion("has_item", hasItem(input))
                .build(consumer, new ResourceLocation(ConvenientGadgetry.MODID, "smoking/" + name));
    }
}
