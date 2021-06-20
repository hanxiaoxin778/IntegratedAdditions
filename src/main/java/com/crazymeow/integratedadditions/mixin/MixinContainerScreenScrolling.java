package com.crazymeow.integratedadditions.mixin;

import com.crazymeow.integratedadditions.imixin.IMixinContainerScreenScrolling;
import org.cyclops.cyclopscore.client.gui.component.input.WidgetTextFieldExtended;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenScrolling;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerScreenScrolling.class)
public abstract class MixinContainerScreenScrolling implements IMixinContainerScreenScrolling{

    @Shadow(remap = false)
    private WidgetTextFieldExtended searchField;

    @Override
    public void setSearchFieldText(String text) {
        searchField.setText(text);
        this.updateSearch(text);
    }

    @Override
    public void setSearchFieldTextFocused2(boolean isFocusedIn) {
        searchField.setFocused2(isFocusedIn);
    }

    @Shadow(remap = false)
    protected abstract void updateSearch(String searchString);
}
