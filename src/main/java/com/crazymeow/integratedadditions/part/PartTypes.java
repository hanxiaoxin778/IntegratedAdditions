package com.crazymeow.integratedadditions.part;

import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.api.part.IPartTypeRegistry;
import org.cyclops.integrateddynamics.part.PartTypePanelDisplay;

public class PartTypes {

    public static final IPartTypeRegistry REGISTRY = IntegratedDynamics._instance.getRegistryManager().getRegistry(IPartTypeRegistry.class);

    public static void load() {
    }

    public static final PartTypeDynamicSetterReader DYNAMICSETTER_READER = REGISTRY.register(new PartTypeDynamicSetterReader("dynamicsetter_reader"));
    public static final PartTypePanelDisplay HUGE_DISPLAY_PANEL = REGISTRY.register(new PartTypeHugePanelDisplay("huge_display_panel"));
}
