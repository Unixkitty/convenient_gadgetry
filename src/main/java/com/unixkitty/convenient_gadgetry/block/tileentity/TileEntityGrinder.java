package com.unixkitty.convenient_gadgetry.block.tileentity;

import com.unixkitty.convenient_gadgetry.api.recipe.IGrinderRecipe;
import com.unixkitty.convenient_gadgetry.container.GrinderContainer;
import com.unixkitty.convenient_gadgetry.crafting.ModRecipeTypes;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModTileEntityTypes;
import com.unixkitty.convenient_gadgetry.util.ItemUtil;
import com.unixkitty.gemspork.lib.tileentity.TileEntityMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TileEntityGrinder extends TileEntityMod implements INamedContainerProvider
{
    public static final int ticksPerCrankRotation = 18;

    public static final int INPUT_SLOT = 0;
    public static final int PROCESSING_SLOT = 1;
    public static final int[] OUTPUT_SLOTS = new int[]{2, 3, 4};

    private static final String INVENTORY_TAG = "inventory";
    private static final String CRANKS_DONE_TAG = "cranksDone";
    private static final String CRANKS_NEEDED_TAG = "cranksNeeded";

    protected final ItemStackHandler inventory = new ItemStackHandler(5)
    {
        @Override
        protected void onLoad()
        {
            if (this.getStackInSlot(PROCESSING_SLOT).isEmpty())
            {
                TileEntityGrinder.this.cranks = 0;
                TileEntityGrinder.this.cranksNeeded = 0;
            }
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            if (slot == PROCESSING_SLOT)
            {
                return 1;
            }
            else
            {
                return super.getStackLimit(slot, stack);
            }
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return slot == INPUT_SLOT;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);

            //Required for the game to save it to disk
            TileEntityGrinder.this.markDirty();
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryCapabilityExternal = LazyOptional.of(() -> this.inventory);

    // Machines (hoppers, pipes) connected to the top and sides can only insert/extract items from the input slot
    private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityExternalInput = LazyOptional.of(() -> new RangedWrapper(this.inventory, INPUT_SLOT, INPUT_SLOT + 1));
    // Machines (hoppers, pipes) connected to the bottom can only insert/extract items from the output slots
    private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityExternalOutput = LazyOptional.of(() -> new RangedWrapper(this.inventory, OUTPUT_SLOTS[0], OUTPUT_SLOTS[OUTPUT_SLOTS.length - 1] + 1));

    public short cranks = 0;
    public int cranksNeeded = 0;

    public TileEntityGrinder()
    {
        super(ModTileEntityTypes.GRINDER.get());
    }

    public boolean canCrank(PlayerEntity player)
    {
        if (allowed(player)) return false;

        if (inventory.getStackInSlot(PROCESSING_SLOT).isEmpty())
        {
            ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);

            if (!inputStack.isEmpty())
            {
                final Optional<IGrinderRecipe> recipe = getRecipe(inputStack);

                if (recipe.isPresent())
                {
                    if (hasOutputRoom(recipe.get()))
                    {
                        final ItemStack stackToProcess = inventory.getStackInSlot(INPUT_SLOT).copy();

                        stackToProcess.setCount(1);

                        inputStack.shrink(1);

                        inventory.setStackInSlot(INPUT_SLOT, inputStack);
                        inventory.setStackInSlot(PROCESSING_SLOT, stackToProcess);

                        this.cranksNeeded = recipe.get().getCranksRequired();

                        return true;
                    }
                }
            }

            return false;
        }

        return true;
    }

    //TODO implement a player stat of how many cranks turned total?
    public void crank(PlayerEntity player)
    {
        if (allowed(player)) return;

        final ItemStack processingStack = inventory.getStackInSlot(PROCESSING_SLOT);
        final Optional<IGrinderRecipe> recipe = getRecipe(processingStack);

        this.cranks++;

        if (recipe.isPresent())
        {
            if (recipe.get().getCranksRequired() > this.cranks)
            {
                return;
            }

            final Optional<List<Pair<ItemStack, Float>>> outputs = getResults(processingStack);

            if (!outputs.isPresent())
            {
                throw new IllegalStateException("Something went wrong while getting IGrinderRecipe outputs for " + processingStack.getTranslationKey());
            }

            this.produceOutput(outputs.get().get(0).getKey().copy());

            //If any optional outputs
            if (outputs.get().size() > 1)
            {
                for (int i = 1; i < outputs.get().size(); i++)
                {
                    if (Objects.requireNonNull(this.world).rand.nextFloat() <= outputs.get().get(i).getValue())
                    {
                        this.produceOutput(outputs.get().get(i).getKey().copy());
                    }
                }
            }

            inventory.getStackInSlot(PROCESSING_SLOT).shrink(1);

            this.cranks = 0;
            this.cranksNeeded = 0;

            this.forceClientSync();
        }
    }

    private boolean allowed(PlayerEntity playerEntity)
    {
        return playerEntity == null || playerEntity instanceof FakePlayer || playerEntity.getEntityWorld().isRemote();
    }

    /* RECIPE LOGIC */

    private boolean hasOutputRoom(IGrinderRecipe recipe)
    {
        for (Pair<ItemStack, Float> itemStackFloatPair : recipe.getPossibleOutputsWithChances())
        {
            if (!hasRoomForOutputItem(itemStackFloatPair.getKey()))
            {
                return false;
            }
        }

        return true;
    }

    private boolean hasRoomForOutputItem(ItemStack itemStack)
    {
        for (int i : OUTPUT_SLOTS)
        {
            ItemStack output = inventory.getStackInSlot(i);

            if (ItemUtil.canItemStack(itemStack, output))
            {
                return true;
            }
        }

        return false;
    }

    private void produceOutput(ItemStack itemStack)
    {
        for (int i : OUTPUT_SLOTS)
        {
            ItemStack output = inventory.getStackInSlot(i);

            if (ItemUtil.canItemStack(itemStack, output))
            {
                if (output.isEmpty())
                {
                    inventory.setStackInSlot(i, itemStack);
                }
                else
                {
                    output.setCount(output.getCount() + itemStack.getCount());
                }
                return;
            }
        }
    }

    /*private boolean isInput(final ItemStack stack)
    {
        return !stack.isEmpty() && getRecipe(stack).isPresent();
    }

    private boolean isOutput(final ItemStack stack)
    {
        final Optional<List<Pair<ItemStack, Float>>> result = getResults(inventory.getStackInSlot(PROCESSING_SLOT));

        boolean isOutput = false;

        if (result.isPresent())
        {
            for (int i = 0; i < result.get().size(); i++)
            {
                if (ItemStack.areItemsEqual(result.get().get(i).getKey(), stack))
                {
                    isOutput = true;
                    break;
                }
            }
        }

        return isOutput;
    }*/

    private Optional<IGrinderRecipe> getRecipe(final ItemStack inputStack)
    {
        //Mojang want an IInventory for reasons
        return getRecipe(new Inventory(inputStack));
    }

    private Optional<IGrinderRecipe> getRecipe(final IInventory fakeInventory)
    {
        return this.world.getRecipeManager().getRecipe(ModRecipeTypes.GRINDING, fakeInventory, this.world);
    }

    private Optional<List<Pair<ItemStack, Float>>> getResults(final ItemStack inputStack)
    {
        final Inventory dummyInventory = new Inventory(inputStack);

        return getRecipe(dummyInventory).map(IGrinderRecipe::getPossibleOutputsWithChances);
    }

    /* END RECIPE LOGIC */

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent(Objects.requireNonNull(ModBlocks.GRINDER).get().getTranslationKey());
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_)
    {
        return new GrinderContainer(id, playerInventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side)
    {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (side == null)
            {
                return inventoryCapabilityExternal.cast();
            }
            else
            {
                return side == Direction.DOWN ? inventoryCapabilityExternalOutput.cast() : inventoryCapabilityExternalInput.cast();
            }
        }
        else
        {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void writePacketNBT(CompoundNBT compound)
    {
        compound.put(INVENTORY_TAG, this.inventory.serializeNBT());
        compound.putShort(CRANKS_DONE_TAG, this.cranks);
        compound.putInt(CRANKS_NEEDED_TAG, this.cranksNeeded);
    }

    @Override
    public void readPacketNBT(CompoundNBT compound)
    {
        this.inventory.deserializeNBT(compound.getCompound(INVENTORY_TAG));
        this.cranks = compound.getShort(CRANKS_DONE_TAG);
        this.cranksNeeded = compound.getInt(CRANKS_NEEDED_TAG);
    }

    /**
     * Invalidates tile entity
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

    //TODO this may be necessary for the crank
    /*@Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
    }*/

    public void forceClientSync()
    {
        this.markDirty();

        SUpdateTileEntityPacket packet = this.getUpdatePacket();

        if (packet != null && this.getWorld() instanceof ServerWorld)
        {
            ((ServerChunkProvider) this.getWorld().getChunkProvider()).chunkManager.getTrackingPlayers(new ChunkPos(this.getPos()), false).forEach(e -> e.connection.sendPacket(packet));
        }
    }
}
