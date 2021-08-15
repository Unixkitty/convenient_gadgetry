package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.block.CrankBlock;
import com.unixkitty.convenient_gadgetry.block.CropCottonBlock;
import com.unixkitty.convenient_gadgetry.itemgroup.ModItemGroups;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ConvenientGadgetry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModRegistry
{
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        // BlockItems for all blocks
        ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block ->
        {
            Item.Properties properties = new Item.Properties().tab(ModItemGroups.PRIMARY);

            Item itemBlock;

            if (block instanceof CropCottonBlock)
            {
                itemBlock = new BlockNamedItem(block, properties);
            }
            else
            {
                if (block instanceof CrankBlock)
                {
                    properties.stacksTo(1);
                }

                itemBlock = new BlockItem(block, properties);
            }

            event.getRegistry().register(itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        });
    }
}
