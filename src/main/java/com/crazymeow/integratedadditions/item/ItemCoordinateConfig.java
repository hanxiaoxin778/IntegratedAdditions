package com.crazymeow.integratedadditions.item;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

public class ItemCoordinateConfig extends ItemConfig {

    public ItemCoordinateConfig() {
        super(
                IntegratedAdditions._instance,
                "coordinate",
                eConfig -> new ItemCoordinate(new Item.Properties()
                        .maxStackSize(1)
                        .group(IntegratedAdditions._instance.getDefaultItemGroup()))
        );
    }

}

