package com.unixkitty.convenient_gadgetry.container;

import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityGrinder;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModContainerTypes;
import com.unixkitty.convenient_gadgetry.util.FunctionalIntReferenceHolder;
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

public class GrinderContainer extends AbstractModContainer
{
    private final IWorldPosCallable canInteractWithCallable;

    private final TileEntityGrinder tileEntity;

    //Client-side constructor
    public GrinderContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data)
    {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    //Server-side constructor
    public GrinderContainer(final int windowId, final PlayerInventory playerInventory, final TileEntityGrinder tileEntity)
    {
        super(ModContainerTypes.GRINDER.get(), windowId);
        this.canInteractWithCallable = IWorldPosCallable.create(Objects.requireNonNull(tileEntity.getLevel()), tileEntity.getBlockPos());

        this.tileEntity = tileEntity;

        this.addDataSlot(new FunctionalIntReferenceHolder(() -> tileEntity.cranksNeeded, v -> tileEntity.cranksNeeded = (short) v));
        this.addDataSlot(new FunctionalIntReferenceHolder(() -> tileEntity.cranks, v -> tileEntity.cranks = (short) v));

        final int INPUT_SLOT_X = 8;
        final int INPUT_SLOT_Y = 35;

        final int OUTPUT_SLOTS_X = 116;
        final int OUTPUT_SLOTS_Y = 35;

        final int PROCESSING_SLOT_X = 62;
        final int PROCESSING_SLOT_Y = 35;

        final int PLAYER_INVENTORY_X_POS = 8;
        final int PLAYER_INVENTORY_Y_POS = 84;

        final int HOTBAR_X_POS = 8;
        final int HOTBAR_Y_POS = 142;

        //Input slot
        this.addSlot(new SlotItemHandler(tileEntity.getItemHandler(), TileEntityGrinder.INPUT_SLOT, INPUT_SLOT_X, INPUT_SLOT_Y));

        //Processing slot
        this.addSlot(new SlotItemHandler(tileEntity.getItemHandler(), TileEntityGrinder.PROCESSING_SLOT, PROCESSING_SLOT_X, PROCESSING_SLOT_Y)
        {
            @Override
            public boolean mayPickup(PlayerEntity playerIn)
            {
                return false;
            }
        });

        //Output slots
        for (int column = 0; column < TileEntityGrinder.OUTPUT_SLOTS.length; column++)
        {
            this.addSlot(new SlotItemHandler(tileEntity.getItemHandler(), TileEntityGrinder.OUTPUT_SLOTS[column], OUTPUT_SLOTS_X + column * SLOT_X_SPACING, OUTPUT_SLOTS_Y));
        }

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

    public int getCranksDone()
    {
        return this.tileEntity.cranks;
    }

    public int getCranksNeeded()
    {
        return this.tileEntity.cranksNeeded;
    }

    private static TileEntityGrinder getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data)
    {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
        Objects.requireNonNull(data, "data cannot be null!");

        final TileEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());

        if (tileAtPos instanceof TileEntityGrinder)
        {
            return (TileEntityGrinder) tileAtPos;
        }

        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean stillValid(@Nonnull final PlayerEntity player)
    {
        return stillValid(canInteractWithCallable, player, Objects.requireNonNull(ModBlocks.GRINDER).get());
    }
}
