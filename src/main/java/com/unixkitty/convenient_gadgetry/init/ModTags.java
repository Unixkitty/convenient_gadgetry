package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.gemspork.item.TagHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

public class ModTags
{
    public static class Items
    {
        public static final ITag.INamedTag<Item> INGOT_MAGNETIC = TagHelper.forgeItemTag("ingots", "magnetic");

        public static final ITag.INamedTag<Item> MAGNET_BLACKLIST = TagHelper.itemTag(ConvenientGadgetry.MODID, "magnet_blacklist");
    }

    public static class Blocks
    {
        public static final ITag<Block> MAGNET_BLACKLIST = TagHelper.blockTag(ConvenientGadgetry.MODID, "magnet_blacklist");
    }
}
