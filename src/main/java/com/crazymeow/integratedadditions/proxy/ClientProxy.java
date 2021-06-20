package com.crazymeow.integratedadditions.proxy;

import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

import com.crazymeow.integratedadditions.IntegratedAdditions;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends ClientProxyComponent {
	public ClientProxy() {
		super(new CommonProxy());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public ModBase<?> getMod() {
		return IntegratedAdditions._instance;
	}
}
