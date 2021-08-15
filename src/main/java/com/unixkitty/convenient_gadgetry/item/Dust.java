package com.unixkitty.convenient_gadgetry.item;

import com.unixkitty.convenient_gadgetry.itemgroup.ModItemGroups;
import com.unixkitty.gemspork.item.TagHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.ForgeHooks;

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
    MAGNETIC("magnetic"),
    ZINC("zinc"),
    BRASS("brass");

    private final String name;
    private final ITag.INamedTag<Item> itemTag;
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
            this.item = new Item(new Item.Properties().tab(ModItemGroups.DUSTS));
        }
        else
        {
            int _burnTime;

            switch (name)
            {
                case "coal":
                    _burnTime = ForgeHooks.getBurnTime(Items.COAL.getDefaultInstance());
                    break;
                case "charcoal":
                    _burnTime = ForgeHooks.getBurnTime(Items.CHARCOAL.getDefaultInstance());
                    break;
                default:
                    _burnTime = burnTime;
                    break;
            }

            this.item = new Item(new Item.Properties().tab(ModItemGroups.DUSTS))
            {
                @Override
                public int getBurnTime(ItemStack itemStack)
                {
                    return _burnTime;
                }
            };
        }
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
        return "dust_" + this.name;
    }
}
