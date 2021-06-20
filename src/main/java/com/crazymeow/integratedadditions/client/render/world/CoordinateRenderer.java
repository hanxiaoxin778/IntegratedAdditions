package com.crazymeow.integratedadditions.client.render.world;

import com.crazymeow.integratedadditions.item.ItemCoordinate;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CoordinateRenderer {
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity player = minecraft.player;
        ItemStack itemStack = player.getHeldItemMainhand();
        Item item = itemStack.getItem();
        if (item instanceof ItemCoordinate) {
            ItemCoordinate itemCoordinate = (ItemCoordinate) item;
            BlockPos position = itemCoordinate.getPosition(itemStack);
            if (position != null) {
                MatrixStack matrixStack = event.getMatrixStack();
                float extraOffset = 0.00f;
//                RenderSystem.disableDepthTest();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                RenderSystem.disableTexture();
                RenderSystem.lineWidth(3.0F);
                matrixStack.push();
                Vector3d offset = minecraft.getRenderManager().info.getProjectedView();
                IRenderTypeBuffer.Impl buffer = minecraft.getRenderTypeBuffers().getBufferSource();
                IVertexBuilder builder = buffer.getBuffer(RenderType.getLines());
                AxisAlignedBB aabb = VoxelShapes.fullCube().getBoundingBox().offset(position).offset(-offset.x, -offset.y, -offset.z).expand(extraOffset, extraOffset, extraOffset).expand(-extraOffset, -extraOffset, -extraOffset);
                WorldRenderer.drawBoundingBox(matrixStack, builder, aabb, 1, 1, 1, 0.75f);
                buffer.finish(RenderType.getLines());
                matrixStack.pop();
                RenderSystem.enableTexture();
//                RenderSystem.enableDepthTest();
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
}
