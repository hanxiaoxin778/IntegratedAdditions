package com.crazymeow.integratedadditions;

import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;

public class GeneralConfig extends DummyConfig {
    //    @ConfigurableProperty(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
//    public static boolean analytics = false;
//
//    @ConfigurableProperty(category = "core", comment = "If the version checker should be enabled.")
//    public static boolean versionChecker = false;

//    //因为参数输入的拦截是在客户端做的，客户端满足条件的情况下都允许被通过
//    @ConfigurableProperty(category = "general", comment = "The maximum radius of entity teleporter of entity writer.", minimalValue = 0, configLocation = ModConfig.Type.CLIENT)
//    public static int entityWriteTeleporterRadiusClient = Integer.MAX_VALUE;
//
//    @ConfigurableProperty(category = "general", comment = "The maximum radius of entity teleporter of entity writer.", minimalValue = 0, configLocation = ModConfig.Type.SERVER)
//    public static int entityWriteTeleporterRadiusServer = 100;

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
