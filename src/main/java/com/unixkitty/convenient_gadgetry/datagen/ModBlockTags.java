package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTags extends BlockTagsProvider
{
    public ModBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, ConvenientGadgetry.MODID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {

    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODNAME + " " + this.getClass().getSimpleName();
    }
}
