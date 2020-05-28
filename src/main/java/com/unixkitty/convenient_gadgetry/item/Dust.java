package com.unixkitty.convenient_gadgetry.item;

import com.unixkitty.convenient_gadgetry.itemgroup.ModItemGroups;
import com.unixkitty.gemspork.item.TagHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.IItemProvider;

public enum Dust implements IItemProvider
{
    SULFUR("sulfur"),
    IRON("iron"),
    GOLD("gold"),
    ALUMINUM("aluminum"),
    COPPER("copper"),
    LEAD("lead"),
    NICKEL("nickel"),
    SILVER("silver"),
    TIN("tin"),
    OSMIUM("osmium"),
    OBSIDIAN("obsidian"),
    QUARTZ("quartz"),
    BRONZE("bronze"),
    COAL("coal", 1),
    FLOUR("flour"),
    CHARCOAL("charcoal", 1),
    ;

    private final String name;
    private final Tag<Item> itemTag;
    private final Item item;

    Dust(String name)
    {
        this(name, 0);
    }

    Dust(String name, int burnTime)
    {
        this.name = name;

        if (name.equals("flour"))
        {
            this.itemTag = TagHelper.itemTag("forge", "flour");
        }
        else
        {
            this.itemTag = TagHelper.forgeItemTag("dusts", name);
        }

        if (burnTime == 0)
        {
            this.item = new Item(new Item.Properties().group(ModItemGroups.DUSTS));
        }
        else
        {
            int _burnTime;

            switch (name)
            {
                case "coal":
                    _burnTime = FurnaceTileEntity.getBurnTimes().get(Items.COAL);
                    break;
                case "charcoal":
                    _burnTime = FurnaceTileEntity.getBurnTimes().get(Items.CHARCOAL);
                    break;
                default:
                    _burnTime = burnTime;
                    break;
            }

            this.item = new Item(new Item.Properties().group(ModItemGroups.DUSTS))
            {
                @Override
                public int getBurnTime(ItemStack itemStack)
                {
                    return _burnTime;
                }
            };
        }
    }

    public Tag<Item> asTag()
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
        return "dust_" + this.name;
    }
}
