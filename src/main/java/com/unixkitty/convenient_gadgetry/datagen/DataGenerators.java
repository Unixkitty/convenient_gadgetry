package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.datagen.recipe.CraftingTableRecipes;
import com.unixkitty.convenient_gadgetry.datagen.recipe.GrinderRecipes;
import com.unixkitty.convenient_gadgetry.datagen.recipe.SmeltingRecipes;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ConvenientGadgetry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer())
        {
            BlockTagsProvider blockTagsProvider = new ModBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new ModItemTags(generator, event.getExistingFileHelper(), blockTagsProvider));
            generator.addProvider(new ModLootTables(generator));
            generator.addProvider(new CraftingTableRecipes(generator));
            generator.addProvider(new SmeltingRecipes(generator));
            generator.addProvider(new GrinderRecipes(generator));
        }
    }
}
