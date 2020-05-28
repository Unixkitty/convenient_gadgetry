package com.unixkitty.convenient_gadgetry.container;

import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityTrashcan;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.unixkitty.convenient_gadgetry.client.gui.ModGuiHandler.*;

public class TrashcanContainer extends Container
{
    private final IWorldPosCallable canInteractWithCallable;

    //Client-side constructor
    public TrashcanContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data)
    {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    //Server-side constructor
    public TrashcanContainer(final int windowId, final PlayerInventory playerInventory, final TileEntityTrashcan tileEntity)
    {
        super(ModContainerTypes.TRASHCAN.get(), windowId);
        this.canInteractWithCallable = IWorldPosCallable.of(Objects.requireNonNull(tileEntity.getWorld()), tileEntity.getPos());

        final int PEDESTAL_SLOT_X = 80;
        final int PEDESTAL_SLOT_Y = 35;

        final int PLAYER_INVENTORY_X_POS = 8;
        final int PLAYER_INVENTORY_Y_POS = 84;

        final int HOTBAR_X_POS = 8;
        final int HOTBAR_Y_POS = 142;

        this.addSlot(new SlotItemHandler(tileEntity.getItemHandler(), 0, PEDESTAL_SLOT_X, PEDESTAL_SLOT_Y));

        // Main player inventory
        for (int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; row++)
        {
            for (int column = 0; column < PLAYER_INVENTORY_COLUMN_COUNT; column++)
            {
                this.addSlot(new Slot(playerInventory,
                        PLAYER_INVENTORY_COLUMN_COUNT + row * PLAYER_INVENTORY_COLUMN_COUNT + column,
                        PLAYER_INVENTORY_X_POS + column * SLOT_X_SPACING,
                        PLAYER_INVENTORY_Y_POS + row * SLOT_Y_SPACING
                ));
            }
        }

        //Player hotbar
        for (int slotNumber = 0; slotNumber < PLAYER_HOTBAR_SLOT_COUNT; slotNumber++)
        {
            this.addSlot(new Slot(playerInventory, slotNumber, HOTBAR_X_POS + SLOT_X_SPACING * slotNumber, HOTBAR_Y_POS));
        }
    }

    private static TileEntityTrashcan getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data)
    {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
        Objects.requireNonNull(data, "data cannot be null!");
        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());

        if (tileAtPos instanceof TileEntityTrashcan)
        {
            return (TileEntityTrashcan) tileAtPos;
        }

        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    /**
     * Generic & dynamic version of {@link Container#transferStackInSlot(PlayerEntity, int)}.
     * Handle when the stack in slot {@code index} is shift-clicked.
     * Normally this moves the stack between the player inventory and the other inventory(s).
     *
     * @param player the player passed in
     * @param index  the index passed in
     * @return the {@link ItemStack}
     */
    @Nonnull
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int index)
    {
        ItemStack returnStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            final ItemStack slotStack = slot.getStack();
            returnStack = slotStack.copy();

            final int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots)
            {
                if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(slotStack, 0, containerSlots, false))
            {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == returnStack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return returnStack;
    }

    @Override
    public boolean canInteractWith(@Nonnull final PlayerEntity player)
    {
        return isWithinUsableDistance(canInteractWithCallable, player, Objects.requireNonNull(ModBlocks.TRASHCAN).get());
    }
}
