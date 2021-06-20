package com.crazymeow.integratedadditions.imixin;

import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;

public interface IMixinContainerLogicProgrammerBase {
    SimpleInventory getWriteSlot();
    ILogicProgrammerElement getTemporarySlotsElement();
}
