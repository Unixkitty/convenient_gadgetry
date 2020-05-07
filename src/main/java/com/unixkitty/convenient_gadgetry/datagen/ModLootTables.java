package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.block.BlockCropCotton;
import com.unixkitty.gemspork.lib.datagen.loot.BlockLootProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.storage.loot.ILootFunctionConsumer;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModLootTables extends BlockLootProvider
{
    /*
        Standard block drops (1 of itself) will get registered automatically if no override will be added
     */
    public ModLootTables(DataGenerator generator)
    {
        super(ConvenientGadgetry.MODID, generator);

        for (Block block : ForgeRegistries.BLOCKS)
        {
            if (ConvenientGadgetry.MODID.equals(Objects.requireNonNull(block.getRegistryName()).getNamespace()))
            {
                if (block instanceof BlockCropCotton)
                {
                    registerLoot(block, block1 -> genCrop((BlockCropCotton) block1));
                }
            }
        }
    }

    protected static LootTable.Builder genCrop(BlockCropCotton block)
    {
        return droppingAndBonusWhen(
                block.getCropItem(),
                block.getSeedsItem(),
                BlockStateProperty.builder(block).fromProperties(
                        StatePropertiesPredicate.Builder.newBuilder().withIntProp(BlockCropCotton.COTTON_AGE, 5)
                ));
    }

    protected static LootTable.Builder droppingAndBonusWhen(IItemProvider crop, IItemProvider seed, ILootCondition.IBuilder condition)
    {
        return withExplosionDecay(
                LootTable.builder().addLootPool(
                        LootPool.builder().addEntry(
                                ItemLootEntry.builder(crop).acceptCondition(condition).alternatively(
                                        ItemLootEntry.builder(seed)
                                ))).addLootPool(
                        LootPool.builder().acceptCondition(condition).addEntry(
                                ItemLootEntry.builder(crop).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3))))
        );
    }

    protected static <T> T withExplosionDecay(ILootFunctionConsumer<T> loot)
    {
        return loot.acceptFunction(ExplosionDecay.builder());
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODNAME + " " + this.getClass().getSimpleName();
    }
}
