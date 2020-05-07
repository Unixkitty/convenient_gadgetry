package com.unixkitty.convenient_gadgetry.compat.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.api.recipe.IGrinderRecipe;
import com.unixkitty.convenient_gadgetry.client.gui.GrinderScreen;
import com.unixkitty.convenient_gadgetry.client.gui.ModGuiHandler;
import com.unixkitty.convenient_gadgetry.crafting.GrinderRecipe;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GrinderRecipeCategoryJei implements IRecipeCategory<GrinderRecipe>
{
    private static final int GUI_START_X = 57;
    private static final int GUI_START_Y = 16;
    private static final int GUI_WIDTH = 112;
    private static final int GUI_HEIGHT = 54;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public GrinderRecipeCategoryJei(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(GrinderScreen.BACKGROUND_TEXTURE, GUI_START_X - JeiPlugin.GUI_OFFSET, GUI_START_Y - JeiPlugin.GUI_OFFSET, GUI_WIDTH + JeiPlugin.GUI_OFFSET, GUI_HEIGHT + JeiPlugin.GUI_OFFSET);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.GRINDER.get()));
        this.arrow = guiHelper.drawableBuilder(GrinderScreen.BACKGROUND_TEXTURE, GrinderScreen.ARROW_START_X, GrinderScreen.ARROW_START_Y, GrinderScreen.ARROW_WIDTH, GrinderScreen.ARROW_HEIGHT).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.localizedName = new TranslationTextComponent("jei." + ConvenientGadgetry.MODID + ".category.grinding").getFormattedText();
    }

    @Override
    public ResourceLocation getUid()
    {
        return IGrinderRecipe.TYPE_ID;
    }

    @Override
    public Class<? extends GrinderRecipe> getRecipeClass()
    {
        return GrinderRecipe.class;
    }

    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public IDrawable getIcon()
    {
        return this.icon;
    }

    @Override
    public void setIngredients(GrinderRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getInput()));
        ingredients.setOutputs(VanillaTypes.ITEM, new ArrayList<>(recipe.getPossibleOutputs()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GrinderRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(0, true, 5, 19);

        itemStacks.init(1, false, 59, 19);
        itemStacks.init(2, false, 59 + 18, 19);
        itemStacks.init(3, false, 59 + 18 + 18, 19);

        itemStacks.set(0, Arrays.asList(recipe.getInput().getMatchingStacks()));

        List<Pair<ItemStack, Float>> outputs = recipe.getPossibleOutputsWithChances();

        for (int i = 0; i < outputs.size(); i++)
        {
            itemStacks.set(i + 1, outputs.get(i).getKey());
        }
    }

    @Override
    public void draw(GrinderRecipe recipe, double mouseX, double mouseY)
    {
        this.arrow.draw(30, 19);

        FontRenderer font = Minecraft.getInstance().fontRenderer;

        List<Pair<ItemStack, Float>> outputs = recipe.getPossibleOutputsWithChances();

//        int spacing = (((GUI_HEIGHT - 26) / 2) / 2) / 2;

        if (recipe.getCranksRequired() != IGrinderRecipe.CRANKS_DEFAULT)
        {
            drawText(font, "Crank turns: " + recipe.getCranksRequired(), 0, GUI_HEIGHT / ModGuiHandler.SLOT_Y_SPACING);
        }

        for (int i = 0; i < outputs.size(); i++)
        {
            float chance = outputs.get(i).getValue();

            if (chance < 1)
            {
                int asPercent = (int) (100 * chance);
                String text = asPercent < 1 ? "<1%" : asPercent + "%";
                drawText(font, text, 61 + 18 * i, 40);
            }
        }
    }

    private void drawText(FontRenderer fontRenderer, String text, int x, int y)
    {
        RenderSystem.pushMatrix();
        RenderSystem.scalef(1f, 1f, 1f);
        boolean wasUnicode = fontRenderer.getBidiFlag();

        fontRenderer.setBidiFlag(false);

        fontRenderer.drawString(text, x, y, 5592405);

        fontRenderer.setBidiFlag(wasUnicode);
        RenderSystem.popMatrix();
    }
}
