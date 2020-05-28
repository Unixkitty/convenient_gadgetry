package com.unixkitty.convenient_gadgetry.block.tileentity;

import com.unixkitty.convenient_gadgetry.block.CrankBlock;
import com.unixkitty.convenient_gadgetry.init.ModTileEntityTypes;
import com.unixkitty.gemspork.lib.tileentity.TileEntityMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import java.util.Objects;

public class TileEntityCrank extends TileEntityMod
{
    public static final int ticksPerCrankRotation = 18;

    public static final String DAMAGE_TAG = "damage";
    public static final String LAST_CRANK_ATTEMPT_TAG = "lastCrankAttempt";

    private int damage;
    private long lastCrankAttempt;

    public TileEntityCrank()
    {
        super(ModTileEntityTypes.CRANK.get());
    }

    @Override
    public void readPacketNBT(CompoundNBT compound)
    {
        damage = compound.getInt(DAMAGE_TAG);
        lastCrankAttempt = compound.getLong(LAST_CRANK_ATTEMPT_TAG);
    }

    @Override
    public void writePacketNBT(CompoundNBT compound)
    {
        compound.putInt(DAMAGE_TAG, damage);
        compound.putLong(LAST_CRANK_ATTEMPT_TAG, lastCrankAttempt);
    }

    public boolean handleRightClick(PlayerEntity player)
    {
        if (this.getWorld() == null || this.getWorld().isRemote())
        {
            return false;
        }

        long crankAttemptTime = this.getWorld().getGameTime();

        if ((crankAttemptTime - lastCrankAttempt) >= ticksPerCrankRotation)
        {
            lastCrankAttempt = crankAttemptTime;

            final TileEntityGrinder grinder = this.getGrinder();

            if (grinder != null)
            {
                if (grinder.canCrank(player))
                {
                    this.damage = 0;
                    this.markDirty();

                    grinder.crank(player);

                    return true;
                }
                else
                {
                    this.getWorld().playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_STEP, SoundCategory.BLOCKS, 0.75f, 1.0f);

                    this.applyDamage();
                }
            }
        }

        return false;
    }

    private TileEntityGrinder getGrinder()
    {
        if (this.getWorld() == null)
        {
            return null;
        }

        final TileEntity tileEntity = this.getWorld().getTileEntity(this.pos.down());

        if (tileEntity instanceof TileEntityGrinder)
        {
            return (TileEntityGrinder) tileEntity;
        }

        return null;
    }

    private void applyDamage()
    {
        this.damage++;

        if (this.damage > 10)
        {
            ((CrankBlock) this.getBlockState().getBlock()).dropCrank(Objects.requireNonNull(this.getWorld()), pos);
        }
    }
}
