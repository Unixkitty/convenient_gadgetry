package com.unixkitty.convenient_gadgetry.block;

import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityCrank;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import com.unixkitty.convenient_gadgetry.init.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class CrankBlock extends Block
{
    private static final VoxelShape SHAPE_SHAFT = Block.box(7, 0, 7, 9, 8, 9);
    private static final VoxelShape SHAPE_N = Block.box(7, 8, 9, 9, 10, 0);
    private static final VoxelShape SHAPE_E = Block.box(7, 8, 9, 16, 10, 7);
    private static final VoxelShape SHAPE_S = Block.box(7, 8, 7, 9, 10, 16);
    private static final VoxelShape SHAPE_W = Block.box(0, 8, 7, 9, 10, 9);

    public static final DirectionProperty FACING_PROPERTY = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape SHAPE_NORTH = Stream.of(
                    SHAPE_SHAFT,
                    SHAPE_N
            )
            .reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_EAST = Stream.of(
                    SHAPE_SHAFT,
                    SHAPE_E
            )
            .reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_SOUTH = Stream.of(
                    SHAPE_SHAFT,
                    SHAPE_S
            )
            .reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_WEST = Stream.of(
                    SHAPE_SHAFT,
                    SHAPE_W
            )
            .reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public CrankBlock()
    {
        super(Block.Properties.copy(Blocks.OAK_PLANKS).isValidSpawn(ModBlocks::neverAllowSpawn));

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING_PROPERTY, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state)
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
        switch (state.getValue(FACING_PROPERTY))
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

    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING_PROPERTY, rot.rotate(state.getValue(FACING_PROPERTY)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING_PROPERTY)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING_PROPERTY);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModTileEntityTypes.CRANK.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        if (!world.isClientSide)
        {
            if (player == null || player instanceof FakePlayer)
            {
                this.dropCrank(world, pos);

                return ActionResultType.FAIL;
            }

            if (player.getItemInHand(hand).isEmpty() && !player.isShiftKeyDown())
            {
                final TileEntity tileEntity = world.getBlockEntity(pos);

                if (tileEntity instanceof TileEntityCrank)
                {
                    if (((TileEntityCrank) tileEntity).handleRightClick(player))
                    {
                        //TODO apply crank turns stat to player
                        this.rotate(world, state, pos);

                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TranslationTextComponent("text.convenient_gadgetry.crank.info").withStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos)
    {
        return world.getBlockState(pos.below()).getBlock() == ModBlocks.GRINDER.get();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (!world.isClientSide)
        {
            if (!state.canSurvive(world, pos))
            {
                this.dropCrank(world, pos);
            }
        }
    }

    public void dropCrank(World world, BlockPos pos)
    {
        world.destroyBlock(pos, true);
        world.sendBlockUpdated(pos, this.defaultBlockState(), world.getBlockState(pos), 3);
    }

    private void rotate(World world, BlockState state, BlockPos pos)
    {
        world.setBlock(pos, state.setValue(FACING_PROPERTY, state.getValue(FACING_PROPERTY).getClockWise()), 3);
    }
}
