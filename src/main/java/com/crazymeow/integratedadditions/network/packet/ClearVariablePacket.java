package com.crazymeow.integratedadditions.network.packet;

import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.core.item.ItemPart;
import org.cyclops.integrateddynamics.item.ItemVariable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ClearVariablePacket extends PacketCodec {
	@CodecField
	private int slotIndex;

	public ClearVariablePacket() {

	}

	public ClearVariablePacket(int slotIndex) {
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
		ItemStack srcItemStack = inventory.getStackInSlot(slotIndex);

		Item item = srcItemStack.getItem();
		int id = -1;
		if (item instanceof ItemPart) {
			if (srcItemStack.getTag() != null && srcItemStack.getTag().contains("id", Constants.NBT.TAG_INT)) {
				id = srcItemStack.getTag().getInt("id");
			}
		} else if (item instanceof ItemVariable) {
			ItemVariable itemVariable = (ItemVariable) item;
			id = itemVariable.getVariableFacade(srcItemStack).getId();
		}
		if (id >= 0) {
			ItemStack itemStack = new ItemStack(srcItemStack.getItem(), srcItemStack.getCount());
			player.inventory.removeStackFromSlot(slotIndex);
			player.inventory.add(slotIndex, itemStack);
		}
	}
}