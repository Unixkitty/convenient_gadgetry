package com.unixkitty.convenient_gadgetry.container;

import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityTrashcan;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.unixkitty.convenient_gadgetry.client.gui.ModGuiHandler.*;

public class TrashcanContainer extends AbstractModContainer
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
        this.canInteractWithCallable = IWorldPosCallable.create(Objects.requireNonNull(tileEntity.getLevel()), tileEntity.getBlockPos());

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
        final TileEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());

        if (tileAtPos instanceof TileEntityTrashcan)
        {
            return (TileEntityTrashcan) tileAtPos;
        }

        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean stillValid(@Nonnull final PlayerEntity player)
    {
        return stillValid(canInteractWithCallable, player, Objects.requireNonNull(ModBlocks.TRASHCAN).get());
    }
}
