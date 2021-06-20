package com.crazymeow.integratedadditions.proxy;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import com.crazymeow.integratedadditions.network.packet.ClearVariablePacket;
import com.crazymeow.integratedadditions.network.packet.CopyVariablePacket;
import com.crazymeow.integratedadditions.network.packet.JEIPluginPacket;
import com.crazymeow.integratedadditions.network.packet.OpenProgrammerGuiPacket;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;

public class CommonProxy extends CommonProxyComponent {
    @Override
    public ModBase<?> getMod() {
        return IntegratedAdditions._instance;
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        super.registerPacketHandlers(packetHandler);
        packetHandler.register(ClearVariablePacket.class);
        packetHandler.register(CopyVariablePacket.class);
        packetHandler.register(OpenProgrammerGuiPacket.class);
        packetHandler.register(JEIPluginPacket.class);
    }
}
