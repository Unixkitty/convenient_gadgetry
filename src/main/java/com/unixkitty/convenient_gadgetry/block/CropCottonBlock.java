package com.unixkitty.convenient_gadgetry.block;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class CropCottonBlock extends CropsBlock
{
    public static final IntegerProperty COTTON_AGE = BlockStateProperties.AGE_0_5;
    private static final VoxelShape[] SHAPE = new VoxelShape[]{
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D)
    };

    private final LazyValue<IItemProvider> seeds;

    public CropCottonBlock()
    {
        super(Block.Properties.from(Blocks.BEETROOTS));

        this.seeds = new LazyValue<>(() -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(ConvenientGadgetry.MODID, "cotton")));
    }

    public Item getCropItem()
    {
        return ModItems.CROP_COTTON.get();
    }

    @Override
    public IItemProvider getSeedsItem()
    {
        return seeds.getValue();
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state)
    {
        return new ItemStack(this::getCropItem);
    }

    @Override
    public IntegerProperty getAgeProperty()
    {
        return COTTON_AGE;
    }

    @Override
    public int getMaxAge()
    {
        return 5;
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(3) != 0)
        {
            super.tick(state, worldIn, pos, rand);
        }
    }

    protected int getBonemealAgeIncrease(World worldIn)
    {
        return super.getBonemealAgeIncrease(worldIn) / 3;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(COTTON_AGE);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE[state.get(this.getAgeProperty())];
    }
}
