package com.unixkitty.convenient_gadgetry.crafting;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.api.recipe.IGrinderRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ConvenientGadgetry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes
{
    public static final IRecipeType<IGrinderRecipe> GRINDING = new RecipeType<>();
    public static final IRecipeSerializer<GrinderRecipe> GRINDER_SERIALIZER = new GrinderRecipe.Serializer();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        Registry.register(Registry.RECIPE_TYPE, IGrinderRecipe.TYPE_ID, GRINDING);
        event.getRegistry().register(GRINDER_SERIALIZER.setRegistryName(IGrinderRecipe.TYPE_ID));
    }

    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T>
    {
        @Override
        public String toString()
        {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }
}
