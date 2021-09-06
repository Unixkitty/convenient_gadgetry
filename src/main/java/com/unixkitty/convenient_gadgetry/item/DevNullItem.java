package com.unixkitty.convenient_gadgetry.item;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.container.DevNullContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class DevNullItem extends Item
{
    public static final String FILTERS_TAG = "filters";
    public static ItemStack TEMPLATE_STACK;

    public DevNullItem(Properties properties)
    {
        super(properties);

        TEMPLATE_STACK = new ItemStack(this);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        if (!world.isClientSide)
        {
            ItemStack itemStack = player.getItemInHand(hand);

            if (itemStack.getItem() instanceof DevNullItem)
            {
                ItemStackHandler ghostInventory = new ItemStackHandler(DevNullContainer.NUM_SLOTS)
                {
                    @Override
                    protected void onContentsChanged(int slot)
                    {
                        itemStack.getOrCreateTag().put(FILTERS_TAG, serializeNBT());
                    }
                };

                ghostInventory.deserializeNBT(itemStack.getOrCreateTagElement(FILTERS_TAG));

                player.openMenu(new SimpleNamedContainerProvider((id, playerInventory, playerEntity) ->
                        new DevNullContainer(id, playerInventory, ghostInventory), new TranslationTextComponent("text." + ConvenientGadgetry.MODID + ".dev_null_gui")
                ));
            }
        }

        return super.use(world, player, hand);
    }

    public static List<ItemStack> getFiltersAsList(ItemStack devNull)
    {
        List<ItemStack> stacks = new ArrayList<>();
        ListNBT tagList = devNull.getOrCreateTagElement(FILTERS_TAG).getList("Items", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT itemTags = tagList.getCompound(i);
            stacks.add(ItemStack.of(itemTags));
        }

        return stacks;
    }
}
