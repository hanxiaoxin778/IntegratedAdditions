package com.crazymeow.integratedadditions.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;

public class JEIPluginPacket extends PacketCodec {
    @CodecField
    private int slotIndex;
    @CodecField
    private ItemStack itemStack;

    public JEIPluginPacket() {
    }

    public JEIPluginPacket(int slotIndex, ItemStack itemStack) {
        this.slotIndex = slotIndex;
        this.itemStack = itemStack;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(World world, PlayerEntity player) {

    }

    @Override
    public void actionServer(World world, ServerPlayerEntity player) {
        if (player.openContainer instanceof ContainerLogicProgrammerBase) {
            IInventory temporaryInputSlots = ((ContainerLogicProgrammerBase) player.openContainer).getTemporaryInputSlots();
            temporaryInputSlots.setInventorySlotContents(this.slotIndex, this.itemStack);
        }
    }
}