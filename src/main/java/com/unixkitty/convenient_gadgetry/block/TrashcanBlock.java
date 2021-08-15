package com.unixkitty.convenient_gadgetry.block;

import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityTrashcan;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModTileEntityTypes;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class TrashcanBlock extends ContainerBlock
{
    private static final VoxelShape SHAPE = Stream.of(
                    Block.box(2, 0, 2, 14, 1, 14),
                    Block.box(3, 1, 3, 13, 14, 13),
                    Block.box(2, 14, 2, 14, 15, 14),
                    Block.box(1, 15, 1, 15, 16, 15)
            )
            .reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public TrashcanBlock()
    {
        super(Block.Properties.copy(Blocks.IRON_BLOCK).isValidSpawn(ModBlocks::neverAllowSpawn));
    }

    /**
     * @deprecated Call via {@link BlockState#getShape(IBlockReader, BlockPos, ISelectionContext)}
     * Implementing/overriding is fine.
     */
    @Nonnull
    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context)
    {
        return Stream.of(
                        Block.box(3, 0, 3, 13, 12, 13),
                        Block.box(2, 12, 2, 14, 14, 14),
                        Block.box(5, 15, 7, 11, 16, 9),
                        Block.box(10, 14, 7, 11, 15, 9),
                        Block.box(5, 14, 7, 6, 15, 9),
                        Block.box(2, 9, 6, 3, 10, 10),
                        Block.box(13, 9, 6, 14, 10, 10)
                )
                .reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
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
        return ModTileEntityTypes.TRASHCAN.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (!world.isClientSide)
        {
            TileEntity tile = world.getBlockEntity(pos);

            if (!(tile instanceof TileEntityTrashcan)) return ActionResultType.FAIL;

            NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityTrashcan) tile, pos);
        }

        return ActionResultType.SUCCESS;
    }
}
