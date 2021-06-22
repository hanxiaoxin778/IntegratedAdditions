package com.crazymeow.integratedadditions;

import com.crazymeow.integratedadditions.client.render.part.HugeDisplayPartOverlayRenderer;
import com.crazymeow.integratedadditions.item.ItemCoordinateConfig;
import com.crazymeow.integratedadditions.part.PartTypes;
import com.crazymeow.integratedadditions.part.aspect.AdditionAspects;
import com.crazymeow.integratedadditions.proxy.ClientProxy;
import com.crazymeow.integratedadditions.proxy.CommonProxy;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ItemGroupMod;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.integrateddynamics.client.render.part.PartOverlayRenderers;

@Mod(IntegratedAdditions.MODID)
public class IntegratedAdditions extends ModBase<IntegratedAdditions> {

    public static final String MODID = "integratedadditions";

    public static IntegratedAdditions _instance;

    public IntegratedAdditions() {
        super(MODID, (instance) -> _instance = instance);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegistriesCreate);
    }

    public void onRegistriesCreate(RegistryEvent.NewRegistry event) {
        AdditionAspects.load();
        PartTypes.load();
        if(MinecraftHelpers.isClientSide()) {
            PartOverlayRenderers.REGISTRY.register(PartTypes.HUGE_DISPLAY_PANEL, new HugeDisplayPartOverlayRenderer());
        }
    }

    @Override
    public ItemGroup constructDefaultItemGroup() {
        return new ItemGroupMod(this, () -> RegistryEntries.ITEM_COORDINATE);
    }

    @Override
    public void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());
        configHandler.addConfigurable(new ItemCoordinateConfig());
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        super.setup(event);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
    }

    public static void clog(String message) {
        IntegratedAdditions._instance.log(Level.INFO, message);
    }

    public static void clog(Level level, String message) {
        IntegratedAdditions._instance.log(level, message);
    }

}
