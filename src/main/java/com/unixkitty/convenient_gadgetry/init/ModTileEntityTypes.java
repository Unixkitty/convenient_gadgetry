package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityGrinder;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public final class ModTileEntityTypes
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ConvenientGadgetry.MODID);

    public static final RegistryObject<TileEntityType<TileEntityGrinder>> GRINDER = TILE_ENTITY_TYPES.register("grinder", () ->
            TileEntityType.Builder.create(TileEntityGrinder::new, Objects.requireNonNull(ModBlocks.GRINDER).get())
                    .build(null)
    );
}
