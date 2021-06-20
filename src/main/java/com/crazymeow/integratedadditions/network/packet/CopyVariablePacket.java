package com.crazymeow.integratedadditions.network.packet;

import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.api.IntegratedDynamicsAPI;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.api.item.IVariableFacadeHandlerRegistry;
import org.cyclops.integrateddynamics.item.ItemVariable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CopyVariablePacket extends PacketCodec {
    @CodecField
    private int slotIndex;

    public CopyVariablePacket() {

    }

    public CopyVariablePacket(int slotIndex) {
        this.slotIndex = slotIndex;
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
        PlayerInventory inventory = player.inventory;
        if (inventory.getFirstEmptyStack() >= 0) {
            ItemStack srcItemStack = inventory.getStackInSlot(slotIndex);
            Item item = srcItemStack.getItem();
            if (item instanceof ItemVariable) {
                ItemVariable itemVariable = (ItemVariable) item;
                IVariableFacade variableFacade = itemVariable.getVariableFacade(srcItemStack);
                if (variableFacade.isValid()) {
                    int inventorySize = inventory.getSizeInventory();
                    IVariableFacade tempVariableFacade;
                    boolean shrunk = false;
                    for (int j = 0; j < inventorySize; j++) {
                        ItemStack element = inventory.getStackInSlot(j);
                        if (!(element.getItem() instanceof ItemVariable))
                            continue;
                        tempVariableFacade = RegistryEntries.ITEM_VARIABLE.getVariableFacade(element);
                        if (!tempVariableFacade.isValid()) {
                            element.shrink(1);
                            shrunk = true;
                            break;
                        }
                    }
                    if (shrunk) {
                        ItemStack itemStack = IntegratedDynamicsAPI.getRegistryManager().getRegistry(IVariableFacadeHandlerRegistry.class).copy(true, srcItemStack);
                        itemStack.setCount(1);
                        player.inventory.addItemStackToInventory(itemStack);
                    }
                }
            }
        }
    }
}