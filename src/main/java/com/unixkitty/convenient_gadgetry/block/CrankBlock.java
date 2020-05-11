package com.unixkitty.convenient_gadgetry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.stream.Stream;

public class CrankBlock extends Block
{
    private static final VoxelShape SHAPE_SHAFT = Block.makeCuboidShape(7, 0, 7, 9, 8, 9);
    private static final VoxelShape SHAPE_N = Block.makeCuboidShape(7, 8, 9, 9, 10, 0);
    private static final VoxelShape SHAPE_E = Block.makeCuboidShape(7, 8, 9, 16, 10, 7);
    private static final VoxelShape SHAPE_S = Block.makeCuboidShape(7, 8, 7, 9, 10, 16);
    private static final VoxelShape SHAPE_W = Block.makeCuboidShape(0, 8, 7, 9, 10, 9);

    public static final DirectionProperty FACING_PROPERTY = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape SHAPE_NORTH = Stream.of(
            SHAPE_SHAFT,
            SHAPE_N
    )
            .reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_EAST = Stream.of(
            SHAPE_SHAFT,
            SHAPE_E
    )
            .reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_SOUTH = Stream.of(
            SHAPE_SHAFT,
            SHAPE_S
    )
            .reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_WEST = Stream.of(
            SHAPE_SHAFT,
            SHAPE_W
    )
            .reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public CrankBlock()
    {
        super(Block.Properties.from(Blocks.OAK_PLANKS));

        this.setDefaultState(this.stateContainer.getBaseState().with(FACING_PROPERTY, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    /*@Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE_NORTH;
    }*/

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch (state.get(FACING_PROPERTY))
        {
            case EAST:
                return SHAPE_EAST;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_NORTH;
        }
    }
}
