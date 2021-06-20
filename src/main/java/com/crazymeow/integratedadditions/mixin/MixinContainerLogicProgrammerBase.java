package com.crazymeow.integratedadditions.mixin;

import com.crazymeow.integratedadditions.imixin.IMixinContainerLogicProgrammerBase;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerLogicProgrammerBase.class)
public abstract class MixinContainerLogicProgrammerBase implements IMixinContainerLogicProgrammerBase {
    @Final
    @Shadow(remap = false)
    private SimpleInventory writeSlot;

    @Override
    public SimpleInventory getWriteSlot() {
        return this.writeSlot;
    }


    @Shadow(remap = false)
    private ILogicProgrammerElement temporarySlotsElement;

    @Override
    public ILogicProgrammerElement getTemporarySlotsElement() {
        return this.temporarySlotsElement;
    }
}
