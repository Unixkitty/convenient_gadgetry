package com.unixkitty.convenient_gadgetry.client;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.client.gui.GrinderScreen;
import com.unixkitty.convenient_gadgetry.client.gui.TrashcanScreen;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModContainerTypes;
import com.unixkitty.convenient_gadgetry.item.MagnetItem;
import com.unixkitty.convenient_gadgetry.network.MessageHandler;
import com.unixkitty.convenient_gadgetry.network.message.MagnetToggleMessageToServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ConvenientGadgetry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents
{
    public static KeyBinding toggleMagnet;

    /**
     * We need to register our renderers on the client because rendering code does not exist on the server
     * and trying to use it on a dedicated server will crash the game.
     * <p>
     * This method will be called by Forge when it is time for the mod to do its client-side setup
     * This method will always be called after the Registry events.
     * This means that all Blocks, Items, TileEntityTypes, etc. will all have been registered already
     */
    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new InstanceHandler());

        // Register ContainerType Screens
        // ScreenManager.registerFactory is not safe to call during parallel mod loading so we queue it to run later
        DeferredWorkQueue.runLater(() ->
        {
            ScreenManager.registerFactory(ModContainerTypes.GRINDER.get(), GrinderScreen::new);
            ScreenManager.registerFactory(ModContainerTypes.TRASHCAN.get(), TrashcanScreen::new);
        });

        RenderTypeLookup.setRenderLayer(ModBlocks.COTTON.get(), RenderType.getCutout());

        registerKeys();
    }

    private static void registerKeys()
    {
        toggleMagnet = registerKeybinding(new KeyBinding("key." + ConvenientGadgetry.MODID + ".toggle_magnet", GLFW.GLFW_KEY_X, ConvenientGadgetry.MODNAME));
    }

    private static KeyBinding registerKeybinding(KeyBinding key)
    {
        ClientRegistry.registerKeyBinding(key);

        return key;
    }

    public static class InstanceHandler
    {
        //Add cooldown of one second to prevent network spam
        @SubscribeEvent
        public void onClientTick(final TickEvent.ClientTickEvent event)
        {
            if (
                    event.phase == TickEvent.Phase.END
                            && toggleMagnet.isPressed()
                            && Minecraft.getInstance().isGameFocused()
                            && !Minecraft.getInstance().isGamePaused()
                            && Minecraft.getInstance().player != null
            )
            {
                int slot = -1;

                for (final ItemStack stack : Minecraft.getInstance().player.inventory.mainInventory)
                {
                    if (stack.isItemEqual(MagnetItem.TEMPLATE_STACK))
                    {
                        slot = Minecraft.getInstance().player.inventory.getSlotFor(stack);
                        break;
                    }
                }

                if (slot != -1)
                {
                    MessageHandler.INSTANCE.send(
                            PacketDistributor.SERVER.noArg(),
                            new MagnetToggleMessageToServer(slot)
                    );
                }
            }
        }
    }
}
