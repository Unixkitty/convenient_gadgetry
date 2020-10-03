package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class ModBlockTags extends BlockTagsProvider
{
    private Set<ResourceLocation> filter = null;

    public ModBlockTags(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerTags()
    {
        super.registerTags();

        filter = new HashSet<>(this.tagToBuilder.keySet());

        getOrCreateBuilder(ModTags.Blocks.MAGNET_BLACKLIST).add(Blocks.AIR);
    }

    @Override
    protected Path makePath(ResourceLocation id)
    {
        return filter != null && filter.contains(id) ? null : super.makePath(id); //We don't want to save vanilla tags.
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODNAME + " " + this.getClass().getSimpleName();
    }
}
