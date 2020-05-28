package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.item.BucketArmorItem;
import com.unixkitty.convenient_gadgetry.item.Dust;
import com.unixkitty.convenient_gadgetry.item.MagnetItem;
import com.unixkitty.convenient_gadgetry.itemgroup.ModItemGroups;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public final class ModItems
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ConvenientGadgetry.MODID);

    public static final RegistryObject<Item> BUCKET_HELMET = ITEMS.register("bucket_helmet", () -> new BucketArmorItem(basicProperties()));

    public static final RegistryObject<Item> CROP_COTTON = ITEMS.register("crop_cotton", () -> new Item(basicProperties()));

    public static final RegistryObject<Item> DUST_SULFUR = registerDust(Dust.SULFUR);
    public static final RegistryObject<Item> DUST_IRON = registerDust(Dust.IRON);
    public static final RegistryObject<Item> DUST_GOLD = registerDust(Dust.GOLD);
    public static final RegistryObject<Item> DUST_ALUMINUM = registerDust(Dust.ALUMINUM);
    public static final RegistryObject<Item> DUST_COPPER = registerDust(Dust.COPPER);
    public static final RegistryObject<Item> DUST_LEAD = registerDust(Dust.LEAD);
    public static final RegistryObject<Item> DUST_NICKEL = registerDust(Dust.NICKEL);
    public static final RegistryObject<Item> DUST_SILVER = registerDust(Dust.SILVER);
    public static final RegistryObject<Item> DUST_TIN = registerDust(Dust.TIN);
    public static final RegistryObject<Item> DUST_OSMIUM = registerDust(Dust.OSMIUM);
    public static final RegistryObject<Item> DUST_OBSIDIAN = registerDust(Dust.OBSIDIAN);
    public static final RegistryObject<Item> DUST_QUARTZ = registerDust(Dust.QUARTZ);
    public static final RegistryObject<Item> DUST_BRONZE = registerDust(Dust.BRONZE);
    public static final RegistryObject<Item> DUST_COAL = registerDust(Dust.COAL);
    public static final RegistryObject<Item> DUST_FLOUR = registerDust(Dust.FLOUR);
    public static final RegistryObject<Item> DUST_CHARCOAL = registerDust(Dust.CHARCOAL);

    public static final RegistryObject<Item> MAGNET = ITEMS.register("magnet", () -> new MagnetItem(basicProperties().maxStackSize(1)));

    private static RegistryObject<Item> registerDust(Dust dust)
    {
        return ITEMS.register(dust.toString(), dust::asItem);
    }

    private static Item.Properties basicProperties()
    {
        return new Item.Properties().group(ModItemGroups.PRIMARY);
    }
}
