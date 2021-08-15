package com.unixkitty.convenient_gadgetry.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
        this.localizedName = I18n.get("jei." + ConvenientGadgetry.MODID + ".category.grinding");
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

        itemStacks.set(0, Arrays.asList(recipe.getInput().getItems()));

        List<Pair<ItemStack, Float>> outputs = recipe.getPossibleOutputsWithChances();

        for (int i = 0; i < outputs.size(); i++)
        {
            itemStacks.set(i + 1, outputs.get(i).getKey());

            //TODO chance displayed is wrong if more than one output with a chance
            /*float chance = outputs.get(i).getValue();

            if (chance < 1)
            {
                itemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) ->
                {
                    if (!input)
                    {
                        tooltip.add(new StringTextComponent(I18n.format("jei.convenient_gadgetry.chance", asPercent(chance))));
                    }
                });
            }*/
        }
    }

    @Override
    public void draw(GrinderRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        this.arrow.draw(matrixStack, 30, 19);

        FontRenderer font = Minecraft.getInstance().font;

        List<Pair<ItemStack, Float>> outputs = recipe.getPossibleOutputsWithChances();

        if (recipe.getCranksRequired() != IGrinderRecipe.CRANKS_DEFAULT)
        {
            drawText(matrixStack, font, "Crank turns: " + recipe.getCranksRequired(), 0, GUI_HEIGHT / ModGuiHandler.SLOT_Y_SPACING);
        }

        for (int i = 0; i < outputs.size(); i++)
        {
            float chance = outputs.get(i).getValue();

            if (chance < 1)
            {
                drawText(matrixStack, font, asPercent(chance), 61 + 18 * i, 40);
            }
        }
    }

    //TODO test how this looks after 1.16.1 update
    private void drawText(MatrixStack matrixStack, FontRenderer fontRenderer, String text, int x, int y)
    {
        matrixStack.pushPose();
        matrixStack.scale(1f, 1f, 1f);
//        boolean wasUnicode = fontRenderer.getBidiFlag();

//        fontRenderer.setBidiFlag(false);

        fontRenderer.draw(matrixStack, text, x, y, 5592405);

//        fontRenderer.setBidiFlag(wasUnicode);
        matrixStack.popPose();
    }

    private String asPercent(float chance)
    {
        int asPercent = (int) (100 * chance);
        return asPercent < 1 ? "<1%" : asPercent + "%";
    }
}
