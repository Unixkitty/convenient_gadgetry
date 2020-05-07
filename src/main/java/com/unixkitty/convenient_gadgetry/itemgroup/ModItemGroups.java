package com.unixkitty.convenient_gadgetry.itemgroup;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.item.Dust;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ModItemGroups
{
    public static final ItemGroup PRIMARY = new ConvenientGadgetryTab(ConvenientGadgetry.MODID, () -> new ItemStack(ModBlocks.GRINDER.get()));
    public static final ItemGroup DUSTS = new ConvenientGadgetryTab(ConvenientGadgetry.MODID + ".dusts", () -> new ItemStack(Dust.OBSIDIAN.asItem()));

    public static class ConvenientGadgetryTab extends ItemGroup
    {
        private final Supplier<ItemStack> iconSupplier;

        public ConvenientGadgetryTab(final String name, final Supplier<ItemStack> iconSupplier)
        {
            super(name);

            this.iconSupplier = iconSupplier;
        }

        @Override
        public ItemStack createIcon()
        {
            return iconSupplier.get();
        }
    }

}