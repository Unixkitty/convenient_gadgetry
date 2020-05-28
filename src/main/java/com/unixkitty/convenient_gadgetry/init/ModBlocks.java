package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.block.CrankBlock;
import com.unixkitty.convenient_gadgetry.block.CropCottonBlock;
import com.unixkitty.convenient_gadgetry.block.GrinderBlock;
import com.unixkitty.convenient_gadgetry.block.TrashcanBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public final class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ConvenientGadgetry.MODID);

    public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder", GrinderBlock::new);
    public static final RegistryObject<Block> CRANK = BLOCKS.register("crank", CrankBlock::new);

    public static final RegistryObject<Block> COTTON = BLOCKS.register("cotton", CropCottonBlock::new);

    public static final RegistryObject<Block> TRASHCAN = BLOCKS.register("trashcan", TrashcanBlock::new);
}
