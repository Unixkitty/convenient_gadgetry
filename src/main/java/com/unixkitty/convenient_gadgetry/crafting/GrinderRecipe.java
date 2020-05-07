package com.unixkitty.convenient_gadgetry.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unixkitty.convenient_gadgetry.api.recipe.IGrinderRecipe;
import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityGrinder;
import com.unixkitty.convenient_gadgetry.init.ModBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GrinderRecipe implements IGrinderRecipe
{
    private final ResourceLocation resourceLocation;
    private final Ingredient input;
    private final short cranks_required;
    private final Map<ItemStack, Float> outputs = new LinkedHashMap<>();

    public GrinderRecipe(ResourceLocation resourceLocation, Ingredient input, short cranks_required)
    {
        this.resourceLocation = resourceLocation;
        this.input = input;
        this.cranks_required = cranks_required;
    }

    public Ingredient getInput()
    {
        return this.input;
    }

    public Set<ItemStack> getPossibleOutputs()
    {
        return outputs.keySet();
    }

    @Override
    public List<Pair<ItemStack, Float>> getPossibleOutputsWithChances()
    {
        return outputs.entrySet().stream()
                .map(entry -> new ImmutablePair<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean matches(IInventory inv, World worldIn)
    {
        return this.input.test(inv.getStackInSlot(TileEntityGrinder.INPUT_SLOT));
    }

    @Deprecated
    @Override
    public ItemStack getRecipeOutput()
    {
        return !outputs.isEmpty() ? outputs.keySet().iterator().next() : ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId()
    {
        return this.resourceLocation;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return ModRecipeTypes.GRINDER_SERIALIZER;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(ModBlocks.GRINDER.get());
    }

    @Override
    public int getCranksRequired()
    {
        return this.cranks_required;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GrinderRecipe>
    {
        //TODO add a way to read forge tags from outputs for dynamic recipe building

        @Override
        public GrinderRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            try
            {
                final Ingredient input = Ingredient.deserialize(json.get("input"));
                final short cranks_required = (short) JSONUtils.getInt(json, "cranks_required", IGrinderRecipe.CRANKS_DEFAULT);

                final GrinderRecipe recipe = new GrinderRecipe(recipeId, input, cranks_required);

                JsonArray outputsArray = json.getAsJsonArray("outputs");

                for (JsonElement element : outputsArray)
                {
                    JsonObject object = element.getAsJsonObject();

                    ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(object, "output"), true);

                    float chance = JSONUtils.getFloat(object, "chance", 1);

                    recipe.outputs.put(output, chance);
                }

                return recipe;
            }
            catch (Exception ignored)
            {
//                ConvenientGadgetry.log().info("Skipping recipe " + recipeId.getPath() + ", caught " + e.getLocalizedMessage());
            }

            return null;
        }

        @Nullable
        @Override
        public GrinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            final Ingredient input = Ingredient.read(buffer);
            final short cranks_required = buffer.readShort();

            final GrinderRecipe recipe = new GrinderRecipe(recipeId, input, cranks_required);

            final int outputCount = buffer.readByte();

            for (int i = 0; i < outputCount; i++)
            {
                ItemStack output = buffer.readItemStack();

                float chance = buffer.readFloat();

                recipe.outputs.put(output, chance);
            }

            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, GrinderRecipe recipe)
        {
            recipe.input.write(buffer);
            buffer.writeShort(recipe.cranks_required);

            buffer.writeByte(recipe.outputs.size());

            recipe.outputs.forEach((output, chance) ->
            {
                buffer.writeItemStack(output);
                buffer.writeFloat(chance);
            });
        }
    }
}
