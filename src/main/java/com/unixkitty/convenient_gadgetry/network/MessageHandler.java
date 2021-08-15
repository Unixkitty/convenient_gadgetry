package com.unixkitty.convenient_gadgetry.network;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.GhostSlot;
import com.unixkitty.convenient_gadgetry.item.MagnetItem;
import com.unixkitty.convenient_gadgetry.network.message.IMessage;
import com.unixkitty.convenient_gadgetry.network.message.MagnetToggleMessageToServer;
import com.unixkitty.convenient_gadgetry.network.message.MessageGhostSlot;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

public class MessageHandler
{
    public static final byte MAGNET_TOGGLE_ID = 1;
    public static final byte GHOST_SLOT_ID = 2;

    public static SimpleChannel INSTANCE;
    public static final ResourceLocation resourceLocation = new ResourceLocation(ConvenientGadgetry.MODID, "message_channel");
    public static final String MESSAGE_PROTOCOL_VERSION = "1";

    private static boolean initialized = false;

    public static void init()
    {
        if (initialized) return;
        initialized = true;

        INSTANCE = NetworkRegistry.newSimpleChannel(resourceLocation, () ->
                        MESSAGE_PROTOCOL_VERSION,
                MessageHandler::shouldClientAccept,
                MessageHandler::shouldServerAccept
        );

        INSTANCE.registerMessage(
                MAGNET_TOGGLE_ID,
                MagnetToggleMessageToServer.class,
                MagnetToggleMessageToServer::encode,
                MagnetToggleMessageToServer::decode,
                MessageHandler::handleServerMessage,
                Optional.of(PLAY_TO_SERVER)
        );
        INSTANCE.registerMessage(
                GHOST_SLOT_ID,
                MessageGhostSlot.class,
                MessageGhostSlot::encode,
                MessageGhostSlot::decode,
                MessageHandler::handleServerMessage,
                Optional.of(PLAY_TO_SERVER)
        );
    }

    public static void handleServerMessage(final IMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        LogicalSide sideReceived = context.getDirection().getReceptionSide();

        if (sideReceived != LogicalSide.SERVER || message.isMessageInvalid())
        {
            ConvenientGadgetry.log().warn(message.getClass().getSimpleName() + " was invalid: side " + sideReceived + ", message: " + message.toString());
            return;
        }

        if (message instanceof MagnetToggleMessageToServer && context.getSender() != null)
        {
            context.enqueueWork(() -> MagnetItem.handleKeyPacket(context.getSender(), ((MagnetToggleMessageToServer) message).getSlotIndex()));
        }
        if (message instanceof MessageGhostSlot && context.getSender() != null)
        {
            context.enqueueWork(() ->
            {
                Container container = context.getSender().containerMenu;
                if (container != null)
                {
                    Slot slot = container.slots.get(((MessageGhostSlot) message).getSlotIndex());

                    if (slot instanceof GhostSlot)
                    {
                        slot.set(((MessageGhostSlot) message).getItemStack());
                    }
                }
            });
        }

        context.setPacketHandled(true);
    }

    public static boolean shouldClientAccept(String protocolVersion)
    {
        return MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

    public static boolean shouldServerAccept(String protocolVersion)
    {
        return MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }
}
