package com.unixkitty.convenient_gadgetry;

import com.unixkitty.convenient_gadgetry.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ConvenientGadgetry.MODID)
public class ConvenientGadgetry
{
    // The MODID value here should match an entry in the META-INF/mods.toml file
    public static final String MODID = "convenient_gadgetry";
    public static final String MODNAME = "Convenient Gadgetry";

    private static final Logger LOG = LogManager.getLogger(MODNAME);

    public ConvenientGadgetry()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(ModRegistry.class);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
    }

    public static Logger log()
    {
        return LOG;
    }
}
