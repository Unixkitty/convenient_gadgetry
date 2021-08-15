package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.convenient_gadgetry.item.Ingot;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
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
    protected void addTags()
    {
        Arrays.stream(Dust.values()).forEach(dust ->
        {
            tag(Tags.Items.DUSTS).addTag(dust.asTag());
            tag(dust.asTag()).add(dust.asItem());
        });

        Arrays.stream(Ingot.values()).forEach(ingot ->
        {
            tag(Tags.Items.INGOTS).addTag(ingot.asTag());
            tag(ingot.asTag()).add(ingot.asItem());
        });
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODID + " " + this.getClass().getSimpleName();
    }
}
