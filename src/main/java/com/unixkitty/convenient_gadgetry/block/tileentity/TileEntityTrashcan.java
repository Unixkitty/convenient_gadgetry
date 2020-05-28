package com.unixkitty.convenient_gadgetry.block.tileentity;

import com.unixkitty.convenient_gadgetry.container.TrashcanContainer;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModTileEntityTypes;
import com.unixkitty.gemspork.lib.tileentity.TileEntityMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class TileEntityTrashcan extends TileEntityMod implements INamedContainerProvider
{
    protected final ItemStackHandler inventory = new ItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);

            this.stacks.set(slot, ItemStack.EMPTY);
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryCapabilityExternal = LazyOptional.of(() -> this.inventory);

    public TileEntityTrashcan()
    {
        super(ModTileEntityTypes.TRASHCAN.get());
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent(Objects.requireNonNull(ModBlocks.TRASHCAN).get().getTranslationKey());
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
    {
        return new TrashcanContainer(p_createMenu_1_, p_createMenu_2_, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side)
    {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? inventoryCapabilityExternal.cast() : super.getCapability(cap, side);
    }

    /**
     * Invalidates our tile entity
     */
    @Override
    public void remove()
    {
        super.remove();
        // We need to invalidate our capability references so that any cached references (by other mods) don't
        // continue to reference our capabilities and try to use them and/or prevent them from being garbage collected
        inventoryCapabilityExternal.invalidate();
    }

    public final IItemHandlerModifiable getItemHandler()
    {
        return inventory;
    }

    @Override
    public void readPacketNBT(CompoundNBT compound)
    {
        //Not saving anything
    }

    @Override
    public void writePacketNBT(CompoundNBT compound)
    {
        //Not saving anything
    }
}
