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
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
    {
        //Grinder
        ShapedRecipeBuilder.shaped(ModBlocks.GRINDER.get())
                .define('s', Blocks.SMOOTH_STONE_SLAB)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('b', Blocks.SMOOTH_STONE)
                .pattern("iii")
                .pattern("isi")
                .pattern("bbb")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        //Cotton
        ShapelessRecipeBuilder.shapeless(((CropCottonBlock) ModBlocks.COTTON.get()).getBaseSeedId(), 2)
                .requires(ModItems.CROP_COTTON.get())
                .unlockedBy("has_item", has(ModItems.CROP_COTTON.get()))
                .save(consumer, HelperUtil.prefixResource(ConvenientGadgetry.MODID, "cotton_seeds_from_cotton"));
        ShapelessRecipeBuilder.shapeless(Items.STRING, 3)
                .requires(ModItems.CROP_COTTON.get())
                .requires(ModItems.CROP_COTTON.get())
                .requires(ModItems.CROP_COTTON.get())
                .unlockedBy("has_item", has(ModItems.CROP_COTTON.get()))
                .save(consumer, HelperUtil.prefixResource(ConvenientGadgetry.MODID, "string_from_cotton"));

        //Bucket helmet
        ShapelessRecipeBuilder.shapeless(ModItems.BUCKET_HELMET.get())
                .requires(Items.BUCKET)
                .unlockedBy("has_item", has(Items.BUCKET))
                .save(consumer);

        //Bronze dust
        ShapelessRecipeBuilder.shapeless(Dust.BRONZE.asItem(), 4)
                .requires(Dust.TIN.asTag())
                .requires(Dust.COPPER.asTag())
                .requires(Dust.COPPER.asTag())
                .requires(Dust.COPPER.asTag())
                .unlockedBy("has_tin_dust", has(Dust.TIN.asTag()))
                .unlockedBy("has_copper_dust", has(Dust.COPPER.asTag()))
                .save(consumer);

        //Crank
        ShapedRecipeBuilder.shaped(ModBlocks.CRANK.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .pattern("sss")
                .pattern("  s")
                .pattern("  s")
                .unlockedBy("has_item", has(ModBlocks.GRINDER.get()))
                .save(consumer);

        //Trashcan
        ShapedRecipeBuilder.shaped(ModBlocks.TRASHCAN.get())
                .define('i', Tags.Items.INGOTS_IRON)
                .define('t', Blocks.IRON_TRAPDOOR)
                .pattern(" t ")
                .pattern("i i")
                .pattern("iii")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        //Magnetic dust
        ShapelessRecipeBuilder.shapeless(ModItems.DUST_MAGNETIC.get(), 4)
                .requires(Dust.IRON.asTag())
                .requires(Dust.IRON.asTag())
                .requires(Dust.GOLD.asTag())
                .requires(Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone_dust", has(Tags.Items.DUSTS_REDSTONE))
                .save(consumer);

        //Brass dust
        ShapelessRecipeBuilder.shapeless(ModItems.DUST_BRASS.get(), 4)
                .requires(Dust.ZINC.asTag())
                .requires(Dust.COPPER.asTag())
                .requires(Dust.COPPER.asTag())
                .requires(Dust.COPPER.asTag())
                .unlockedBy("has_zinc_dust", has(Dust.ZINC.asTag()))
                .save(consumer);

        //Magnet
        ShapedRecipeBuilder.shaped(ModItems.MAGNET.get())
                .define('b', Tags.Items.DYES_BLUE)
                .define('r', Tags.Items.DYES_RED)
                .define('i', ModTags.Items.INGOT_MAGNETIC)
                .pattern("i i")
                .pattern("bir")
                .unlockedBy("has_item", has(ModTags.Items.INGOT_MAGNETIC))
                .save(consumer);

        //dev_null
        ShapelessRecipeBuilder.shapeless(ModItems.DEV_NULL.get())
                .requires(Blocks.COBBLESTONE)
                .requires(Items.APPLE)
                .unlockedBy("has_apple", has(Items.APPLE))
                .save(consumer);
    }
}
