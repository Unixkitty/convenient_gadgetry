package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.block.BlockCropCotton;
import com.unixkitty.convenient_gadgetry.block.GrinderBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public final class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ConvenientGadgetry.MODID);

    public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder", GrinderBlock::new);

    public static final RegistryObject<Block> COTTON = BLOCKS.register("cotton", BlockCropCotton::new);
}
