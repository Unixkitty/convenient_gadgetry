package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModTags;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.convenient_gadgetry.item.Ingot;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class ModItemTags extends ItemTagsProvider
{
    public ModItemTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper, BlockTagsProvider blockTagProvider)
    {
        super(generatorIn, blockTagProvider, ConvenientGadgetry.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags()
    {
        Arrays.stream(Dust.values()).forEach(dust ->
        {
            getOrCreateBuilder(Tags.Items.DUSTS).addTag(dust.asTag());
            getOrCreateBuilder(dust.asTag()).add(dust.asItem());
        });

        Arrays.stream(Ingot.values()).forEach(ingot ->
        {
            getOrCreateBuilder(Tags.Items.INGOTS).addTag(ingot.asTag());
            getOrCreateBuilder(ingot.asTag()).add(ingot.asItem());
        });

        getOrCreateBuilder(ModTags.Items.MAGNET_BLACKLIST).add(Items.AIR);
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODID + " " + this.getClass().getSimpleName();
    }
}
