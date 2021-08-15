package com.unixkitty.convenient_gadgetry.container;

import com.unixkitty.convenient_gadgetry.init.ModContainerTypes;
import com.unixkitty.convenient_gadgetry.item.DevNullItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

import static com.unixkitty.convenient_gadgetry.client.gui.ModGuiHandler.*;

public class DevNullContainer extends AbstractModContainer
{
    public static final int NUM_SLOTS = 6 * 9;

    //Client-side constructor
    public DevNullContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data)
    {
        this(windowId, playerInventory, new ItemStackHandler(NUM_SLOTS));
    }

    //Server-side constructor
    public DevNullContainer(final int windowId, final PlayerInventory playerInventory, final IItemHandler ghostInventory)
    {
        super(ModContainerTypes.DEV_NULL.get(), windowId);

        InvWrapper playerInventoryWrapper = new InvWrapper(playerInventory);

        final int DEVNULL_START_X = 8;
        final int DEVNULL_START_Y = 18;

        final int DEVNULL_ROW_COUNT = 6;
        final int DEVNULL_COLUMN_COUNT = 9;

        final int PLAYER_INVENTORY_X_POS = 8;
        final int PLAYER_INVENTORY_Y_POS = 140;

        final int HOTBAR_X_POS = 8;
        final int HOTBAR_Y_POS = 198;

        int index = 0;
        //Container slots
        for (int row = 0; row < DEVNULL_ROW_COUNT; row++)
        {
            for (int column = 0; column < DEVNULL_COLUMN_COUNT; column++)
            {
                this.addSlot(new GhostSlot(ghostInventory,
                        index++,
                        DEVNULL_START_X + column * SLOT_X_SPACING,
                        DEVNULL_START_Y + row * SLOT_Y_SPACING
                ));
            }
        }

        //Main player inventory
        for (int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; row++)
        {
            for (int column = 0; column < PLAYER_INVENTORY_COLUMN_COUNT; column++)
            {
                this.addSlot(new SlotItemHandler(playerInventoryWrapper,
                        PLAYER_INVENTORY_COLUMN_COUNT + row * PLAYER_INVENTORY_COLUMN_COUNT + column,
                        PLAYER_INVENTORY_X_POS + column * SLOT_X_SPACING,
                        PLAYER_INVENTORY_Y_POS + row * SLOT_Y_SPACING
                ));
            }
        }

        //Player hotbar
        for (int slotNumber = 0; slotNumber < PLAYER_HOTBAR_SLOT_COUNT; slotNumber++)
        {
            this.addSlot(new SlotItemHandler(playerInventoryWrapper, slotNumber, HOTBAR_X_POS + SLOT_X_SPACING * slotNumber, HOTBAR_Y_POS));
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull final PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem())
        {
            ItemStack currentStack = slot.getItem();

            // Don't accept instances of itself
            if (currentStack.getItem() instanceof DevNullItem)
            {
                return itemstack;
            }

            if (currentStack.isEmpty())
            {
                return itemstack;
            }

            // Get first available slot number
            int slotNumber = -1;

            for (int i = 0; i < NUM_SLOTS; i++)
            {
                if (this.slots.get(i).getItem().isEmpty())
                {
                    slotNumber = i;
                    break;
                }
                else
                {
                    if (this.slots.get(i).getItem().getItem() == currentStack.getItem())
                    {
                        break;
                    }
                }
            }

            if (slotNumber == -1)
            {
                return itemstack;
            }

            this.slots.get(slotNumber).set(currentStack.copy().split(1));
        }

        return itemstack;
    }

    @Nonnull
    @Override
    public ItemStack clicked(int slotId, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull final PlayerEntity player)
    {
        if ((slotId < this.slots.size()
                && slotId >= 0
                && this.slots.get(slotId).getItem().getItem() instanceof DevNullItem)
                || clickTypeIn == ClickType.SWAP)
        {
            return ItemStack.EMPTY;
        }

        return super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean stillValid(@Nonnull final PlayerEntity player)
    {
        return true;
    }
}
