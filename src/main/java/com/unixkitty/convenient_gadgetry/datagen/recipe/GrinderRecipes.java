package com.unixkitty.convenient_gadgetry.datagen.recipe;

import com.google.common.collect.Maps;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.api.recipe.GrinderRecipeBuilder;
import com.unixkitty.convenient_gadgetry.api.recipe.IGrinderRecipe;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.gemspork.item.TagHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.Util;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class GrinderRecipes extends RecipeProvider
{
    private static final String from_ore = "_from_ore";

    private final Map<Item, Block> WOOL_BY_COLOR = Util.make(Maps.newHashMap(), (map) ->
    {
        map.put(Items.WHITE_DYE, Blocks.WHITE_WOOL);
        map.put(Items.ORANGE_DYE, Blocks.ORANGE_WOOL);
        map.put(Items.MAGENTA_DYE, Blocks.MAGENTA_WOOL);
        map.put(Items.LIGHT_BLUE_DYE, Blocks.LIGHT_BLUE_WOOL);
        map.put(Items.YELLOW_DYE, Blocks.YELLOW_WOOL);
        map.put(Items.LIME_DYE, Blocks.LIME_WOOL);
        map.put(Items.PINK_DYE, Blocks.PINK_WOOL);
        map.put(Items.GRAY_DYE, Blocks.GRAY_WOOL);
        map.put(Items.LIGHT_GRAY_DYE, Blocks.LIGHT_GRAY_WOOL);
        map.put(Items.CYAN_DYE, Blocks.CYAN_WOOL);
        map.put(Items.PURPLE_DYE, Blocks.PURPLE_WOOL);
        map.put(Items.BLUE_DYE, Blocks.BLUE_WOOL);
        map.put(Items.BROWN_DYE, Blocks.BROWN_WOOL);
        map.put(Items.GREEN_DYE, Blocks.GREEN_WOOL);
        map.put(Items.RED_DYE, Blocks.RED_WOOL);
        map.put(Items.BLACK_DYE, Blocks.BLACK_WOOL);
    });

    public GrinderRecipes(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        for (Dust dust : Dust.values())
        {
            switch (dust)
            {
                case SULFUR:
                    continue;
                case COAL:
                    ore(dust, consumer);
                    ingot(dust, Ingredient.fromItems(Items.COAL), consumer);
                    break;
                case FLOUR:
                    GrinderRecipeBuilder.create(dust.toString() + "_from_wheat", Ingredient.fromItems(Items.WHEAT), new ItemStack(dust)).build(consumer);
                    break;
                case OBSIDIAN:
                    GrinderRecipeBuilder.create(dust.toString() + "_from_obsidian", Ingredient.fromTag(Tags.Items.OBSIDIAN), new ItemStack(dust)).build(consumer);
                    break;
                case CHARCOAL:
                    ingot(dust, Ingredient.fromItems(Items.CHARCOAL), consumer);
                    break;
                case MAGNETIC:
                case BRONZE:
                    ingot(dust, consumer);
                    break;
                case QUARTZ:
                    ore(dust, consumer);
                    ingot(dust, Ingredient.fromItems(Items.QUARTZ), consumer);
                    break;
                default:
                    ore(dust, consumer);
                    ingot(dust, consumer);
                    break;
            }
        }

        GrinderRecipeBuilder.create("blaze_powder_from_blaze_rods", Ingredient.fromItems(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 4),
                4,
                optional(new ItemStack(Dust.SULFUR), 0.5f)
        ).build(consumer);

        GrinderRecipeBuilder.create("seeds_from_grass", Ingredient.fromItems(Blocks.GRASS), new ItemStack(Items.WHEAT_SEEDS),
                4,
                optional(new ItemStack(Items.BEETROOT_SEEDS, 1), 0.125f),
                optional(new ItemStack(ModBlocks.COTTON.get(), 1), 0.25f)
        ).build(consumer);

        //Vanilla things
        WOOL_BY_COLOR.forEach((item, block) ->
        {
            ItemStack dyeStack = new ItemStack(item);

            GrinderRecipeBuilder.create("string_from_" + Objects.requireNonNull(DyeColor.getColor(dyeStack)).toString() + "_wool",
                    Ingredient.fromItems(block),
                    new ItemStack(Items.STRING, 4),
                    IGrinderRecipe.CRANKS_DEFAULT,
                    optional(dyeStack, 0.5f)
            ).build(consumer);

        });

        GrinderRecipeBuilder.create("gravel_from_cobble", Ingredient.fromTag(Tags.Items.COBBLESTONE), new ItemStack(Blocks.GRAVEL), 6).build(consumer);
        GrinderRecipeBuilder.create("sand_from_gravel", Ingredient.fromTag(Tags.Items.GRAVEL), new ItemStack(Blocks.SAND)).build(consumer);
        GrinderRecipeBuilder.create("redstone_dust" + from_ore, Ingredient.fromItems(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE, 6)).build(consumer);
        GrinderRecipeBuilder.create("prismarine_shard_from_prismarine", Ingredient.fromItems(Blocks.PRISMARINE), new ItemStack(Items.PRISMARINE_SHARD, 2),
                6,
                optional(new ItemStack(Items.PRISMARINE_SHARD, 1), 0.5f)
        ).build(consumer);
        GrinderRecipeBuilder.create("prismarine_shard_from_sea_lantern", Ingredient.fromItems(Blocks.SEA_LANTERN), new ItemStack(Items.PRISMARINE_SHARD, 2),
                6,
                optional(new ItemStack(Items.PRISMARINE_CRYSTALS, 1), 0.5f),
                optional(new ItemStack(Items.PRISMARINE_CRYSTALS, 2), 0.25f)
        ).build(consumer);
        GrinderRecipeBuilder.create("glowstone_dust_from_glowstone", Ingredient.fromItems(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST, 4)).build(consumer);
        GrinderRecipeBuilder.create("sand_from_sandstone", Ingredient.fromTag(Tags.Items.SANDSTONE), new ItemStack(Blocks.SAND, 4), 6).build(consumer);
        GrinderRecipeBuilder.create("bonemeal_from_bone", Ingredient.fromTag(Tags.Items.BONES), new ItemStack(Items.BONE_MEAL, 6), 8).build(consumer);
    }

    private Pair<ItemStack, Float> optional(ItemStack stack, float chance)
    {
        return new ImmutablePair<>(stack, chance);
    }

    private void ingot(Dust dust, Consumer<IFinishedRecipe> consumer)
    {
        ingot(dust, Ingredient.fromTag(TagHelper.forgeItemTag("ingots", dust.getName())), IGrinderRecipe.CRANKS_DEFAULT * 2, consumer);
    }

    private void ingot(Dust dust, Ingredient ingredient, Consumer<IFinishedRecipe> consumer)
    {
        ingot(dust, ingredient, IGrinderRecipe.CRANKS_DEFAULT, consumer);
    }

    private void ingot(Dust dust, Ingredient ingredient, int cranks, Consumer<IFinishedRecipe> consumer)
    {
        GrinderRecipeBuilder.create(dust.toString() + "_from_" + dust.getName(), ingredient, new ItemStack(dust), cranks).build(consumer);
    }

    private void ore(Dust dust, Consumer<IFinishedRecipe> consumer)
    {
        GrinderRecipeBuilder.create(dust.toString() + from_ore, Ingredient.fromTag(TagHelper.forgeItemTag("ores", dust.getName())), new ItemStack(dust, 2)).build(consumer);
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODID + " " + this.getClass().getSimpleName();
    }
}
