package com.unixkitty.convenient_gadgetry.item;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.init.ModTags;
import com.unixkitty.convenient_gadgetry.util.ItemUtil;
import com.unixkitty.convenient_gadgetry.util.Vector3;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

public class MagnetItem extends Item
{
    public static ItemStack TEMPLATE_STACK = ItemStack.EMPTY;

    private static final String COOLDOWN_TAG = "cooldown";
    private static final String ENABLED_TAG = "enabled";
    //Inverted means attract items only when sneaking
    private static final String INVERTED_TAG = "inverted";

    private static final int range = 8;

    private static final Field PICKUP_DELAY_FIELD = ObfuscationReflectionHelper.findField(ItemEntity.class, "field_145804_b");

    public MagnetItem(Properties properties)
    {
        super(properties);

        PICKUP_DELAY_FIELD.setAccessible(true);

        TEMPLATE_STACK = new ItemStack(this);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);

        tooltip.add(new TranslationTextComponent("text." + ConvenientGadgetry.MODID + ".magnet.info1", isEnabled(stack)).applyTextStyle(TextFormatting.DARK_AQUA));
        tooltip.add(new TranslationTextComponent("text." + ConvenientGadgetry.MODID + ".magnet.info2", isInverted(stack)).applyTextStyle(TextFormatting.DARK_AQUA));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return isEnabled(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        if (!world.isRemote)
        {
            ItemStack stack = player.getHeldItem(hand);

            if (player.isSneaking())
            {
                toggleInverted(stack);

                informPlayer(player, stack, INVERTED_TAG);
            }
            else
            {
                toggle(stack);

                informPlayer(player, stack, ENABLED_TAG);
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        if (entity instanceof PlayerEntity /*&& !world.isRemote*/ && !entity.isSpectator())
        {
            int cooldown = getCooldown(stack);

            /*if (magnetSuppressionBlock)
            {
                if (cooldown < 0)
                {
                    setCooldown(stack, 2);
                }
                return;
            }*/

            if (cooldown <= 0)
            {
                if (isEnabled(stack) && entity.isSneaking() == isInverted(stack))
                {
                    double x = entity.getPosX();
                    double y = entity.getPosY() + 0.75;
                    double z = entity.getPosZ();

                    List<ItemEntity> items = entity.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
                    int pulled = 0;

                    for (ItemEntity item : items)
                    {
                        if (((MagnetItem) stack.getItem()).canPullItem(item))
                        {
                            if (pulled > 200)
                            {
                                break;
                            }

                            ItemUtil.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);

                            pulled++;
                        }
                    }
                }
            }
            else
            {
                setCooldown(stack, cooldown - 1);
            }
        }
    }

    private boolean canPullItem(ItemEntity item)
    {
        int pickupDelay = 0;

        try
        {
            pickupDelay = PICKUP_DELAY_FIELD.getInt(item);
        }
        catch (IllegalAccessException ignored)
        {

        }

        if (!item.isAlive() || pickupDelay >= 40 /* || Magnet suppression block can go here */ || item.getPersistentData().getBoolean("PreventRemoteMovement"))
        {
            return false;
        }

        ItemStack stack = item.getItem();
        if (stack.isEmpty() || ModTags.Items.MAGNET_BLACKLIST.contains(stack.getItem()))
        {
            return false;
        }

        BlockPos pos = new BlockPos(item);

        if (ModTags.Blocks.MAGNET_BLACKLIST.contains(item.world.getBlockState(pos).getBlock()))
        {
            return false;
        }

        return !ModTags.Blocks.MAGNET_BLACKLIST.contains(item.world.getBlockState(pos.down()).getBlock());
    }

    public static boolean isEnabled(ItemStack stack)
    {
        return ItemUtil.getBoolean(stack, ENABLED_TAG, false);
    }

    public static void toggle(ItemStack stack)
    {
        ItemUtil.setBoolean(stack, ENABLED_TAG, !isEnabled(stack));
    }

    public static int getCooldown(ItemStack stack)
    {
        return ItemUtil.getInt(stack, COOLDOWN_TAG, 0);
    }

    public static void setCooldown(ItemStack stack, int cooldown)
    {
        ItemUtil.setInt(stack, COOLDOWN_TAG, cooldown);
    }

    public static boolean isInverted(ItemStack stack)
    {
        return ItemUtil.getBoolean(stack, INVERTED_TAG, false);
    }

    public static void toggleInverted(ItemStack stack)
    {
        ItemUtil.setBoolean(stack, INVERTED_TAG, !isInverted(stack));
    }

    public static void handleKeyPacket(ServerPlayerEntity player, int slotIndex)
    {
        if (player.inventory.hasItemStack(TEMPLATE_STACK))
        {
            ItemStack stack = player.inventory.getStackInSlot(slotIndex);

            toggle(stack);

            informPlayer(player, stack, ENABLED_TAG);
        }
    }

    private static void informPlayer(final PlayerEntity player, final ItemStack stack, String tag)
    {
        player.sendMessage(new TranslationTextComponent("text." + ConvenientGadgetry.MODID + ".magnet_" + tag, isEnabled(stack)));
    }
}
