package com.crazymeow.integratedadditions.client.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeybindingRegistry {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> ClientRegistry.registerKeyBinding(KeyBoardInput.CLEAR_KEY));
		event.enqueueWork(() -> ClientRegistry.registerKeyBinding(KeyBoardInput.SET_KEY));
		event.enqueueWork(() -> ClientRegistry.registerKeyBinding(KeyBoardInput.COPY_KEY));
		//		event.enqueueWork(() -> ClientRegistry.registerKeyBinding(KeyBoardInput.MODIFY_KEY));
		event.enqueueWork(() -> ClientRegistry.registerKeyBinding(KeyBoardInput.SHOW_KEY));
	}
}