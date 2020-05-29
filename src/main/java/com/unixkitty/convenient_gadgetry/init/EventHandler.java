package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.item.MagnetItem;
import com.unixkitty.convenient_gadgetry.network.MessageHandler;
import com.unixkitty.convenient_gadgetry.util.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ConvenientGadgetry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EventHandler
{
    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new InstanceHandler());

        MessageHandler.init();
    }

    public static class InstanceHandler
    {
        @SubscribeEvent
        public void onPlayerItemToss(final ItemTossEvent event)
        {
            if (event.getPlayer().isServerWorld() && event.getPlayer().inventory.hasItemStack(MagnetItem.TEMPLATE_STACK))
            {
                ItemStack magnet = ItemUtil.getStackIfPlayerHas(MagnetItem.TEMPLATE_STACK, event.getPlayer());

                if (magnet != null && MagnetItem.isEnabled(magnet))
                {
                    MagnetItem.setCooldown(magnet, 40);
                }
            }
        }
    }
}
