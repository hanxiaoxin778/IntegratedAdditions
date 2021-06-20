package com.crazymeow.integratedadditions.part;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.integrateddynamics.part.PartTypePanelDisplay;

public class PartTypeHugePanelDisplay extends PartTypePanelDisplay {

    public PartTypeHugePanelDisplay(String name) {
        super(name);
    }

    @Override
    protected ModBase getMod() {
        return IntegratedAdditions._instance;
    }
}