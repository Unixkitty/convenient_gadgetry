package com.unixkitty.convenient_gadgetry.network.message;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class MessageGhostSlot implements IMessage
{
    private boolean isMessageValid;

    private int slotIndex;
    private ItemStack itemStack;

    private MessageGhostSlot()
    {
        this.isMessageValid = false;
    }

    public MessageGhostSlot(int slotIndex, ItemStack itemStack)
    {
        this.slotIndex = slotIndex;
        this.itemStack = itemStack;

        this.isMessageValid = true;
    }

    public int getSlotIndex()
    {
        return this.slotIndex;
    }

    public ItemStack getItemStack()
    {
        return this.itemStack;
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
        buffer.writeItem(this.itemStack);
    }

    public static MessageGhostSlot decode(PacketBuffer buffer)
    {
        MessageGhostSlot message = new MessageGhostSlot();

        try
        {
            message.slotIndex = buffer.readInt();
            message.itemStack = buffer.readItem();
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            ConvenientGadgetry.log().warn("Exception while reading " + MessageGhostSlot.class.getSimpleName() + ": " + e);
            return message;
        }

        message.isMessageValid = true;

        return message;
    }

    @Override
    public String toString()
    {
        return String.format("%s[slotIndex=%s,itemStack=%s]", getClass().getSimpleName(), this.slotIndex, this.itemStack);
    }
}
