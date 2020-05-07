package com.unixkitty.convenient_gadgetry.item;

import com.unixkitty.convenient_gadgetry.ConvenientGadgetry;
import com.unixkitty.convenient_gadgetry.client.render.model.BucketOnHeadModel;
import com.unixkitty.gemspork.item.DynamicTieredArmorProperties;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class BucketArmorItem extends ArmorItem
{
    private LazyValue<BipedModel<LivingEntity>> model;

    public BucketArmorItem(Properties builder)
    {
        super(BucketArmorMaterial.INSTANCE, EquipmentSlotType.HEAD, builder);

        this.model = DistExecutor.runForDist(() -> () -> new LazyValue<>(this::provideArmorModel),
                () -> () -> null);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, BipedModel _default)
    {
        return this.model.getValue();
    }

    @OnlyIn(Dist.CLIENT)
    public BipedModel<LivingEntity> provideArmorModel()
    {
        return new BucketOnHeadModel();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        return ConvenientGadgetry.MODID + ":textures/models/armor/bucket.png";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TranslationTextComponent("text.convenient_gadgetry.bucket.info").applyTextStyle(TextFormatting.GRAY));
    }

    @Override
    public String getTranslationKey()
    {
        return Items.BUCKET.getTranslationKey();
    }

    private static class BucketArmorMaterial implements IArmorMaterial
    {
        private static final BucketArmorMaterial INSTANCE = new BucketArmorMaterial();

        private static final IArmorMaterial GOLD = ArmorMaterial.GOLD;
        private static final IArmorMaterial IRON = ArmorMaterial.IRON;
        private static final DynamicTieredArmorProperties DYNAMIC_PROPERTIES = new DynamicTieredArmorProperties(
                "bucket",
                0,
                1,
                IRON.getEnchantability(),
                0.25f,
                GOLD,
                IRON,
                IRON.getSoundEvent(),
                Tags.Items.INGOTS_IRON
        );

        private BucketArmorMaterial()
        {
        }

        @Override
        public int getDurability(EquipmentSlotType slot)
        {
            return DYNAMIC_PROPERTIES.getDurability(slot);
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slot)
        {
            return IRON.getDamageReductionAmount(slot);
        }

        @Override
        public int getEnchantability()
        {
            return IRON.getEnchantability();
        }

        @Override
        public SoundEvent getSoundEvent()
        {
            return DYNAMIC_PROPERTIES.getSoundEvent();
        }

        @Override
        public Ingredient getRepairMaterial()
        {
            return DYNAMIC_PROPERTIES.getRepairMaterial();
        }

        @Override
        public String getName()
        {
            return DYNAMIC_PROPERTIES.getName();
        }

        @Override
        public float getToughness()
        {
            return ArmorMaterial.DIAMOND.getToughness() / 2;
        }
    }
}
