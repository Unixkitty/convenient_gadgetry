package com.unixkitty.convenient_gadgetry.compat.jei;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.api.recipe.IGrinderRecipe;
import com.unixkitty.convenient_gadgetry.client.gui.GrinderScreen;
import com.unixkitty.convenient_gadgetry.container.GrinderContainer;
import com.unixkitty.convenient_gadgetry.crafting.ModRecipeTypes;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin
{
    public static final int GUI_OFFSET = 1;

    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(ConvenientGadgetry.MODID, "plugin/main");

    @Override
    public ResourceLocation getPluginUid()
    {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        registration.addRecipeCategories(new GrinderRecipeCategoryJei(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        registration.addRecipes(getRecipesOfType(ModRecipeTypes.GRINDING), IGrinderRecipe.TYPE_ID);
    }

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType)
    {
        return Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .collect(Collectors.toList());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(GrinderScreen.class, GrinderScreen.ARROW_X, GrinderScreen.ARROW_Y, GrinderScreen.ARROW_WIDTH, GrinderScreen.ARROW_HEIGHT, IGrinderRecipe.TYPE_ID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        //input slot, amount of slots, inventory slot, amount of slots
        registration.addRecipeTransferHandler(GrinderContainer.class, IGrinderRecipe.TYPE_ID, 0, 1, 5, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GRINDER.get()), IGrinderRecipe.TYPE_ID);
    }
}
