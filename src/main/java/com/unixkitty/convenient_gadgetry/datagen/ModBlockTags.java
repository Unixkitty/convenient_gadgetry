package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModTags;
import net.minecraft.block.Blocks;
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
        tag(ModTags.Blocks.MAGNET_BLACKLIST).add(Blocks.AIR);
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODNAME + " " + this.getClass().getSimpleName();
    }
}
