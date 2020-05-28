package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags
{
    public static class Items
    {
        public static final Tag<Item> MAGNET_BLACKLIST = tag("magnet_blacklist");

        private static Tag<Item> tag(String name)
        {
            return new ItemTags.Wrapper(new ResourceLocation(ConvenientGadgetry.MODID, name));
        }
    }

    public static class Blocks
    {
        public static final Tag<Block> MAGNET_BLACKLIST = tag("magnet_blacklist");

        private static Tag<Block> tag(String name)
        {
            return new BlockTags.Wrapper(new ResourceLocation(ConvenientGadgetry.MODID, name));
        }
    }
}
