package com.crazymeow.integratedadditions;

import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;

public class GeneralConfig extends DummyConfig {
    //    @ConfigurableProperty(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
//    public static boolean analytics = false;
//
//    @ConfigurableProperty(category = "core", comment = "If the version checker should be enabled.")
//    public static boolean versionChecker = false;

    @ConfigurableProperty(category = "general", comment = "The maximum radius of entity teleporter of entity writer.", minimalValue = 0, configLocation = ModConfig.Type.SERVER)
    public static int entityWriteTeleporterRadius = 10000;

    public static int dynamicSetterReaderBaseConsumption = 1;

    public GeneralConfig() {
        super(IntegratedAdditions._instance, "general");
    }

    @Override
    public void onRegistered() {
//        if (analytics) {
//            Analytics.registerMod(getMod(), Reference.GA_TRACKING_ID);
//        }
//        if (versionChecker) {
//            Versions.registerMod(getMod(), IntegratedAdditions._instance, Reference.VERSION_URL);
//        }
    }
}
