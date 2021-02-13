package com.unixkitty.convenient_gadgetry.init;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.DevNullContainer;
import com.unixkitty.convenient_gadgetry.container.GrinderContainer;
import com.unixkitty.convenient_gadgetry.container.TrashcanContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes
{
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, ConvenientGadgetry.MODID);

    public static final RegistryObject<ContainerType<GrinderContainer>> GRINDER = CONTAINER_TYPES.register("grinder", () ->
            IForgeContainerType.create(GrinderContainer::new)
    );

    public static final RegistryObject<ContainerType<TrashcanContainer>> TRASHCAN = CONTAINER_TYPES.register("trashcan", () ->
            IForgeContainerType.create(TrashcanContainer::new)
    );

    public static final RegistryObject<ContainerType<DevNullContainer>> DEV_NULL = CONTAINER_TYPES.register("dev_null", () ->
            IForgeContainerType.create(DevNullContainer::new)
    );
}
