package com.unixkitty.convenient_gadgetry.datagen;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.gemspork.lib.datagen.ItemTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;

import java.util.Arrays;

public class ModItemTags extends ItemTagProvider
{
    public ModItemTags(DataGenerator generatorIn)
    {
        super(ConvenientGadgetry.MODID, generatorIn);
    }

    @Override
    protected void registerCustomTags()
    {
        Arrays.stream(Dust.values()).forEach(dust ->
        {
            getBuilder(Tags.Items.DUSTS).add(dust.asTag());
            getBuilder(dust.asTag()).add(dust.asItem());
        });
    }
}
