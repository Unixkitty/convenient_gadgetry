package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.item.DevNullItem;
import com.unixkitty.convenient_gadgetry.item.MagnetItem;
import com.unixkitty.convenient_gadgetry.network.MessageHandler;
import com.unixkitty.convenient_gadgetry.util.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

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
            if (event.getPlayer().isEffectiveAi() && event.getPlayer().inventory.contains(MagnetItem.TEMPLATE_STACK))
            {
                ItemStack magnet = ItemUtil.getStackIfPlayerHas(MagnetItem.TEMPLATE_STACK, event.getPlayer());

                if (magnet != null && MagnetItem.isEnabled(magnet))
                {
                    MagnetItem.setCooldown(magnet, 40);
                }
            }
        }

        @SubscribeEvent
        public void onItemPickup(final EntityItemPickupEvent event)
        {
            if (event.getPlayer().isEffectiveAi() && ItemUtil.getStackIfPlayerHas(DevNullItem.TEMPLATE_STACK, event.getPlayer()) != null)
            {
                if (event.getItem().getItem().isEmpty()) return;

                ItemStack pickedUpStack = event.getItem().getItem();

                for (int i = 0; i < event.getPlayer().inventory.getContainerSize(); i++)
                {
                    final ItemStack itemStack = event.getPlayer().inventory.getItem(i);

                    if (itemStack.getItem() == DevNullItem.TEMPLATE_STACK.getItem())
                    {
                        final List<ItemStack> filters = DevNullItem.getFiltersAsList(itemStack);

                        if (filters.size() != 0)
                        {
                            for (ItemStack filter : filters)
                            {
                                if (pickedUpStack.sameItemStackIgnoreDurability(filter))
                                {
                                    //Begone, trash!
                                    pickedUpStack.setCount(0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
