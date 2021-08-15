package com.unixkitty.convenient_gadgetry.api.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.crafting.ModRecipeTypes;
import com.unixkitty.gemspork.item.ItemNBTHelper;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class GrinderRecipeBuilder
{
    private final FinishedRecipe recipe;

    private GrinderRecipeBuilder(String name, Ingredient input, int cranks_required, ItemStack output)
    {
        this.recipe = new FinishedRecipe(name, input, cranks_required, output);
    }

    public void build(Consumer<IFinishedRecipe> consumer)
    {
        consumer.accept(this.recipe);
    }

    public static GrinderRecipeBuilder create(String name, Ingredient input, ItemStack output)
    {
        return create(name, input, output, IGrinderRecipe.CRANKS_DEFAULT);
    }

    public static GrinderRecipeBuilder create(String name, Ingredient input, ItemStack output, int cranks_required)
    {
        return create(name, input, output, cranks_required, null);
    }

    public static GrinderRecipeBuilder create(String name, Ingredient input, ItemStack output, int cranks_required, Pair<ItemStack, Float> optional_output1)
    {
        return create(name, input, output, cranks_required, optional_output1, null);
    }

    public static GrinderRecipeBuilder create(String name, Ingredient input, ItemStack output, int cranks_required, @Nullable Pair<ItemStack, Float> optional_output1, @Nullable Pair<ItemStack, Float> optional_output2)
    {
        GrinderRecipeBuilder result = new GrinderRecipeBuilder(name, input, cranks_required, output);

        if (optional_output1 != null)
        {
            result.recipe.outputs.put(optional_output1.getKey(), optional_output1.getValue());
        }

        if (optional_output2 != null)
        {
            result.recipe.outputs.put(optional_output2.getKey(), optional_output2.getValue());
        }

        return result;
    }

    private static class FinishedRecipe implements IFinishedRecipe
    {
        private final ResourceLocation resourceLocation;
        private final Ingredient input;
        private final short cranks_required;
        private final Map<ItemStack, Float> outputs = new LinkedHashMap<>();

        private FinishedRecipe(String name, Ingredient input, int cranks_required, ItemStack output)
        {
            this.resourceLocation = new ResourceLocation(ConvenientGadgetry.MODID, "grinding/" + name);
            this.input = input;
            this.cranks_required = (short) cranks_required;
            this.outputs.put(output, 1F);
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            Preconditions.checkArgument(outputs.size() <= 3);

            json.add("input", this.input.toJson());
            json.addProperty("cranks_required", cranks_required);

            JsonArray outputsArray = new JsonArray();
            json.add("outputs", outputsArray);

            outputs.forEach((output, chance) ->
            {
                JsonObject object = new JsonObject();

                object.add("output", ItemNBTHelper.serializeStack(output));
                object.addProperty("chance", chance);

                outputsArray.add(object);
            });
        }

        @Override
        public ResourceLocation getId()
        {
            return this.resourceLocation;
        }

        @Override
        public IRecipeSerializer<?> getType()
        {
            return ModRecipeTypes.GRINDER_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement()
        {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId()
        {
            return null;
        }
    }
}
