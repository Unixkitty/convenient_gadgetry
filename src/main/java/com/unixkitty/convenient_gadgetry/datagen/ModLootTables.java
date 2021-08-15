package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.block.CrankBlock;
import com.unixkitty.convenient_gadgetry.block.CropCottonBlock;
import com.unixkitty.gemspork.lib.datagen.loot.BlockLootProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
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
                if (block instanceof CropCottonBlock)
                {
                    registerLoot(block, block1 -> genCrop((CropCottonBlock) block1));
                }
                if (block instanceof CrankBlock)
                {
                    registerLoot(block, block1 -> crankDrop());
                }
            }
        }
    }

    protected static LootTable.Builder genCrop(CropCottonBlock block)
    {
        return droppingAndBonusWhen(
                block.getCropItem(),
                block.getBaseSeedId(),
                BlockStateProperty.hasBlockStateProperties(block).setProperties(
                        StatePropertiesPredicate.Builder.properties().hasProperty(CropCottonBlock.COTTON_AGE, 5)
                ));
    }

    protected static LootTable.Builder droppingAndBonusWhen(IItemProvider crop, IItemProvider seed, ILootCondition.IBuilder condition)
    {
        return withExplosionDecay(
                LootTable.lootTable().withPool(
                        LootPool.lootPool().add(
                                ItemLootEntry.lootTableItem(crop).when(condition).otherwise(
                                        ItemLootEntry.lootTableItem(seed)
                                ))).withPool(
                        LootPool.lootPool().when(condition).add(
                                ItemLootEntry.lootTableItem(crop).apply(ApplyBonus.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))
        );
    }

    protected static <T> T withExplosionDecay(ILootFunctionConsumer<T> loot)
    {
        return loot.apply(ExplosionDecay.explosionDecay());
    }

    protected static LootTable.Builder crankDrop()
    {
        LootEntry.Builder<?> entry = ItemLootEntry.lootTableItem(Items.STICK).apply(SetCount.setCount(ConstantRange.exactly(2)));
        LootPool.Builder pool = LootPool.lootPool().name("main").setRolls(ConstantRange.exactly(1)).add(entry).when(SurvivesExplosion.survivesExplosion());

        return LootTable.lootTable().withPool(pool);
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODNAME + " " + this.getClass().getSimpleName();
    }
}
