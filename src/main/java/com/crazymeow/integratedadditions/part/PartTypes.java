package com.crazymeow.integratedadditions.part;

import com.crazymeow.integratedadditions.part.aspect.AdditionAspects;
import com.google.common.collect.Lists;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.api.part.IPartTypeRegistry;
import org.cyclops.integrateddynamics.api.part.aspect.IAspect;
import org.cyclops.integrateddynamics.core.part.aspect.AspectRegistry;
import org.cyclops.integrateddynamics.part.PartTypePanelDisplay;

public class PartTypes {

    public static final IPartTypeRegistry REGISTRY = IntegratedDynamics._instance.getRegistryManager().getRegistry(IPartTypeRegistry.class);

    public static void load() {
        AspectRegistry.getInstance().register(org.cyclops.integrateddynamics.core.part.PartTypes.ENTITY_WRITER, Lists.<IAspect>newArrayList(
                AdditionAspects.Write.Entity.BUILDER_ENTITY_TELEPORTER
        ));

    }

    public static final PartTypeDynamicSetterReader DYNAMICSETTER_READER = REGISTRY.register(new PartTypeDynamicSetterReader("dynamicsetter_reader"));
    public static final PartTypePanelDisplay HUGE_DISPLAY_PANEL = REGISTRY.register(new PartTypeHugePanelDisplay("huge_display_panel"));
}
