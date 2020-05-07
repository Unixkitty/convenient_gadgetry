package com.unixkitty.convenient_gadgetry.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.unixkitty.convenient_gadgetry.block.tileentity.TileEntityGrinder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

import javax.annotation.Nonnull;

public class TileEntityGrinderRender extends TileEntityRenderer<TileEntityGrinder>
{
    public TileEntityGrinderRender(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull TileEntityGrinder pedestal, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        if (pedestal.getWorld() == null) return;
    }
}
