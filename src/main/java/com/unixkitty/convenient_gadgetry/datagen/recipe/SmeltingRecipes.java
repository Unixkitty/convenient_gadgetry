package com.unixkitty.convenient_gadgetry.datagen.recipe;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.convenient_gadgetry.item.Ingot;
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

import java.util.Arrays;
import java.util.function.Consumer;

public class SmeltingRecipes extends SmeltingRecipeProvider
{
    public SmeltingRecipes(DataGenerator generatorIn)
    {
        super(ConvenientGadgetry.MODID, generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
    {
        ingot(consumer, Dust.IRON.asTag(), Items.IRON_INGOT, "iron_ingot_from_dust");
        ingot(consumer, Dust.GOLD.asTag(), Items.GOLD_INGOT, "gold_ingot_from_dust");

        Arrays.stream(Ingot.values()).forEach(ingot -> ingot(consumer, Dust.valueOf(ingot.name()).asTag(), ingot, ingot.toString() + "_from_dust"));

        basicCooking(consumer, Dust.SULFUR.asTag(), Items.GUNPOWDER, "gunpowder_from_sulfur");
        basicCooking(consumer, Dust.FLOUR.asTag(), Items.BREAD, "bread_from_flour");
        basicSmoking(consumer, Dust.FLOUR.asTag(), Items.BREAD, "bread_from_flour");
    }

    private void ingot(Consumer<IFinishedRecipe> consumer, ITag<Item> input, IItemProvider result, String name)
    {
        basicCooking(consumer, input, result, name);
        basicBlasting(consumer, input, result, name);
    }

    private void basicCooking(Consumer<IFinishedRecipe> consumer, ITag<Item> input, IItemProvider result, String name)
    {
        CookingRecipeBuilder.smelting(
                        Ingredient.of(input),
                        result,
                        0,
                        200)
                .unlockedBy("has_item", has(input))
                .save(consumer, new ResourceLocation(ConvenientGadgetry.MODID, "smelting/" + name));
    }

    private void basicBlasting(Consumer<IFinishedRecipe> consumer, ITag<Item> input, IItemProvider result, String name)
    {
        CookingRecipeBuilder.blasting(
                        Ingredient.of(input),
                        result,
                        0,
                        100)
                .unlockedBy("has_item", has(input))
                .save(consumer, new ResourceLocation(ConvenientGadgetry.MODID, "blasting/" + name));
    }

    private void basicSmoking(Consumer<IFinishedRecipe> consumer, ITag<Item> input, IItemProvider result, String name)
    {
        CookingRecipeBuilder.cooking(
                        Ingredient.of(input),
                        result,
                        0.35F,
                        100,
                        IRecipeSerializer.SMOKING_RECIPE).
                unlockedBy("has_item", has(input))
                .save(consumer, new ResourceLocation(ConvenientGadgetry.MODID, "smoking/" + name));
    }
}
