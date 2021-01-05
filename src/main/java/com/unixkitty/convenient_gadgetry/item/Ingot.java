package com.unixkitty.convenient_gadgetry.item;

import com.unixkitty.convenient_gadgetry.itemgroup.ModItemGroups;
import com.unixkitty.gemspork.item.TagHelper;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

public enum Ingot implements IItemProvider
{
    ALUMINUM("aluminum"),
    COPPER("copper"),
    LEAD("lead"),
    NICKEL("nickel"),
    SILVER("silver"),
    TIN("tin"),
    BRONZE("bronze"),
    MAGNETIC("magnetic"),
    ZINC("zinc"),
    BRASS("brass");

    private final String name;
    private final ITag.INamedTag<Item> itemTag;
    private final Item item;

    Ingot(String name)
    {
        this.name = name;

        this.itemTag = TagHelper.forgeItemTag("ingots", name);
        this.item = new Item(new Item.Properties().group(ModItemGroups.DUSTS));
    }

    public ITag.INamedTag<Item> asTag()
    {
        return this.itemTag;
    }

    @Override
    public Item asItem()
    {
        return this.item;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return "ingot_" + this.name;
    }
}
