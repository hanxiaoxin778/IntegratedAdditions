package com.crazymeow.integratedadditions.mixin;

import org.cyclops.cyclopscore.inventory.container.ScrollingInventoryContainer;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Debug(print = true)
@Mixin(ScrollingInventoryContainer.class)
public interface MixinScrollingInventoryContainerAccessor {
    @Accessor(remap = false)
    List<ILogicProgrammerElement> getUnfilteredItems();
}
