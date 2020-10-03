package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModItems;
import com.unixkitty.convenient_gadgetry.init.ModTags;
import com.unixkitty.convenient_gadgetry.item.Dust;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ModItemTags extends ItemTagsProvider
{
    private Set<ResourceLocation> filter = null;

    public ModItemTags(DataGenerator generatorIn, BlockTagsProvider blockTagProvider)
    {
        super(generatorIn, blockTagProvider);
    }

    @Override
    protected void registerTags()
    {
        super.registerTags();

        filter = new HashSet<>(this.tagToBuilder.keySet());

        Arrays.stream(Dust.values()).forEach(dust ->
        {
            getOrCreateBuilder(Tags.Items.DUSTS).addTag(dust.asTag());
            getOrCreateBuilder(dust.asTag()).add(dust.asItem());
        });

        getOrCreateBuilder(ModTags.Items.MAGNET_BLACKLIST).add(Items.AIR);

        getOrCreateBuilder(Tags.Items.INGOTS).addTag(ModTags.Items.INGOT_MAGNETIC);
        getOrCreateBuilder(ModTags.Items.INGOT_MAGNETIC).add(ModItems.INGOT_MAGNETIC.get());
    }

    @Override
    protected Path makePath(ResourceLocation id)
    {
        return filter != null && filter.contains(id) ? null : super.makePath(id); //We don't want to save vanilla tags.
    }

    @Override
    public String getName()
    {
        return ConvenientGadgetry.MODID + " " + this.getClass().getSimpleName();
    }
}
