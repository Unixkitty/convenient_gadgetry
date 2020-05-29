package com.unixkitty.convenient_gadgetry.network.message;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import net.minecraft.network.PacketBuffer;

public class MagnetToggleMessageToServer implements IMessage
{
    private boolean isMessageValid;

    private int slotIndex;

    private MagnetToggleMessageToServer()
    {
        this.isMessageValid = false;
    }

    public MagnetToggleMessageToServer(int slotIndex)
    {
        this.slotIndex = slotIndex;

        this.isMessageValid = true;
    }

    public int getSlotIndex()
    {
        return this.slotIndex;
    }

    @Override
    public boolean isMessageInvalid()
    {
        return !this.isMessageValid;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        if (!isMessageValid) return;

        buffer.writeInt(this.slotIndex);
    }

    public static MagnetToggleMessageToServer decode(PacketBuffer buffer)
    {
        MagnetToggleMessageToServer message = new MagnetToggleMessageToServer();

        try
        {
            message.slotIndex = buffer.readInt();
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            ConvenientGadgetry.log().warn("Exception while reading " + MagnetToggleMessageToServer.class.getSimpleName() + ": " + e);
            return message;
        }

        message.isMessageValid = true;

        return message;
    }

    @Override
    public String toString()
    {
        return String.format("%s[slot=%s]", getClass().getSimpleName(), this.slotIndex);
    }
}
