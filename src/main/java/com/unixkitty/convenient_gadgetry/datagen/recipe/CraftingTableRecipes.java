package com.unixkitty.convenient_gadgetry.datagen.recipe;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.block.CropCottonBlock;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModItems;
import com.unixkitty.convenient_gadgetry.init.ModTags;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.gemspork.lib.HelperUtil;
import com.unixkitty.gemspork.lib.datagen.recipe.CraftingTableRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CraftingTableRecipes extends CraftingTableRecipeProvider
{
    public CraftingTableRecipes(DataGenerator generator)
    {
        super(ConvenientGadgetry.MODID, generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        //Grinder
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.GRINDER.get())
                .key('s', Blocks.SMOOTH_STONE_SLAB)
                .key('i', Tags.Items.INGOTS_IRON)
                .key('b', Blocks.SMOOTH_STONE)
                .patternLine("iii")
                .patternLine("isi")
                .patternLine("bbb")
                .addCriterion("has_item", hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);

        //Cotton
        ShapelessRecipeBuilder.shapelessRecipe(((CropCottonBlock) ModBlocks.COTTON.get()).getSeedsItem(), 2)
                .addIngredient(ModItems.CROP_COTTON.get())
                .addCriterion("has_item", hasItem(ModItems.CROP_COTTON.get()))
                .build(consumer, HelperUtil.prefixResource(ConvenientGadgetry.MODID, "cotton_seeds_from_cotton"));
        ShapelessRecipeBuilder.shapelessRecipe(Items.STRING, 3)
                .addIngredient(ModItems.CROP_COTTON.get())
                .addIngredient(ModItems.CROP_COTTON.get())
                .addIngredient(ModItems.CROP_COTTON.get())
                .addCriterion("has_item", hasItem(ModItems.CROP_COTTON.get()))
                .build(consumer, HelperUtil.prefixResource(ConvenientGadgetry.MODID, "string_from_cotton"));

        //Bucket helmet
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.BUCKET_HELMET.get())
                .addIngredient(Items.BUCKET)
                .addCriterion("has_item", hasItem(Items.BUCKET))
                .build(consumer);

        //Bronze dust
        ShapelessRecipeBuilder.shapelessRecipe(Dust.BRONZE.asItem())
                .addIngredient(Dust.TIN.asTag())
                .addIngredient(Dust.COPPER.asTag())
                .addIngredient(Dust.COPPER.asTag())
                .addIngredient(Dust.COPPER.asTag())
                .addCriterion("has_tin_dust", hasItem(Dust.TIN.asTag()))
                .addCriterion("has_copper_dust", hasItem(Dust.COPPER.asTag()))
                .build(consumer);

        //Crank
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.CRANK.get())
                .key('s', Tags.Items.RODS_WOODEN)
                .patternLine("sss")
                .patternLine("  s")
                .patternLine("  s")
                .addCriterion("has_item", hasItem(ModBlocks.GRINDER.get()))
                .build(consumer);

        //Trashcan
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.TRASHCAN.get())
                .key('i', Tags.Items.INGOTS_IRON)
                .key('t', Blocks.IRON_TRAPDOOR)
                .patternLine(" t ")
                .patternLine("i i")
                .patternLine("iii")
                .addCriterion("has_item", hasItem(Items.IRON_INGOT))
                .build(consumer);

        //Magnetic dust
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.DUST_MAGNETIC.get(), 4)
                .addIngredient(Dust.IRON.asTag())
                .addIngredient(Dust.IRON.asTag())
                .addIngredient(Dust.GOLD.asTag())
                .addIngredient(Tags.Items.DUSTS_REDSTONE)
                .addCriterion("has_redstone_dust", hasItem(Tags.Items.DUSTS_REDSTONE))
                .build(consumer);

        //Brass dust
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.DUST_BRASS.get(), 4)
                .addIngredient(Dust.ZINC.asTag())
                .addIngredient(Dust.COPPER.asTag())
                .addIngredient(Dust.COPPER.asTag())
                .addIngredient(Dust.COPPER.asTag())
                .addCriterion("has_zinc_dust", hasItem(Dust.ZINC.asTag()))
                .build(consumer);

        //Magnet
        ShapedRecipeBuilder.shapedRecipe(ModItems.MAGNET.get())
                .key('b', Tags.Items.DYES_BLUE)
                .key('r', Tags.Items.DYES_RED)
                .key('i', ModTags.Items.INGOT_MAGNETIC)
                .patternLine("i i")
                .patternLine("bir")
                .addCriterion("has_item", hasItem(ModTags.Items.INGOT_MAGNETIC))
                .build(consumer);

        //dev_null
        /*ShapelessRecipeBuilder.shapelessRecipe(ModItems.DEV_NULL.get())
                .addIngredient(Blocks.COBBLESTONE)
                .addIngredient(Items.APPLE)
                .addCriterion("has_apple", hasItem(Items.APPLE))
                .build(consumer);*/
    }
}
