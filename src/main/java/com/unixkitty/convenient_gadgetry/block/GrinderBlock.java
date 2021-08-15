package com.unixkitty.convenient_gadgetry.block;

import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityGrinder;
import com.unixkitty.convenient_gadgetry.init.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class GrinderBlock extends ContainerBlock
{
    public GrinderBlock()
    {
        super(Block.Properties.of(Material.STONE).strength(3.5F));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn)
    {
        return ModTileEntityTypes.GRINDER.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        if (!world.isClientSide)
        {
            if (this.getMenuProvider(state, world, pos) != null && player instanceof ServerPlayerEntity)
            {
                TileEntity tile = world.getBlockEntity(pos);

                if (tile instanceof TileEntityGrinder)
                {
                    NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityGrinder) tile, pos);
                }
                else
                {
                    return ActionResultType.FAIL;
                }
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntity tileEntity = world.getBlockEntity(pos);

            if (tileEntity instanceof TileEntityGrinder)
            {
                TileEntityGrinder grinder = (TileEntityGrinder) tileEntity;

                for (int slot = 0; slot < grinder.getItemHandler().getSlots(); slot++)
                {
                    InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), grinder.getItemHandler().getStackInSlot(slot));
                }
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos)
    {
        //TODO calculate signal based on inventory
        return super.getAnalogOutputSignal(blockState, worldIn, pos);
    }
}
