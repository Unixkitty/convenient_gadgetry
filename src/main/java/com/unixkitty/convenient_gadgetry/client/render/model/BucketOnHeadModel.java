package com.unixkitty.convenient_gadgetry.client.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

public class BucketOnHeadModel extends ModelArmor
{
    private final ModelRenderer bucket;

    public BucketOnHeadModel()
    {
        super(EquipmentSlotType.HEAD);

        this.texWidth = 48;
        this.texHeight = 48;

        this.bucket = new ModelRenderer(this);
        bucket.setPos(0.0F, 22.0F, 0.0F);
        bucket.texOffs(38, 34).addBox(-1.0F, 1.0F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bucket.texOffs(0, 30).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 8.0F, 10.0F, 0.0F, false);
        bucket.texOffs(0, 20).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);
        bucket.texOffs(0, 13).addBox(-3.0F, -13.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        bucket.texOffs(0, 8).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        this.head.addChild(this.bucket);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        this.bucket.visible = slot == EquipmentSlotType.HEAD;
        this.hat.visible = false;

        this.head = this.bucket;

        super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @SuppressWarnings("unused")
    protected void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
