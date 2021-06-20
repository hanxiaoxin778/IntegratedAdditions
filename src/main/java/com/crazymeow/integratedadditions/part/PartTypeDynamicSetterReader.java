package com.crazymeow.integratedadditions.part;

import com.crazymeow.integratedadditions.GeneralConfig;
import com.crazymeow.integratedadditions.IntegratedAdditions;
import com.crazymeow.integratedadditions.part.aspect.AdditionAspects;
import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.api.network.IPartNetwork;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.api.part.aspect.IAspect;
import org.cyclops.integrateddynamics.core.part.aspect.AspectRegistry;
import org.cyclops.integrateddynamics.core.part.read.PartStateReaderBase;
import org.cyclops.integrateddynamics.core.part.read.PartTypeReadBase;

public class PartTypeDynamicSetterReader extends PartTypeReadBase<PartTypeDynamicSetterReader, PartStateReaderBase<PartTypeDynamicSetterReader>> {

    public PartTypeDynamicSetterReader(String name) {
        super(name);
        AspectRegistry.getInstance().register(this, Lists.<IAspect>newArrayList(
                AdditionAspects.Read.DynamicSetter.INTEGER_RANDOM_BETWEEN,
                AdditionAspects.Read.DynamicSetter.INTEGER_IDENTITY_BETWEEN,
                AdditionAspects.Read.DynamicSetter.INTEGER_ARRAY_BETWEEN
        ));
    }

    @Override
    public ModBase getMod() {
        return IntegratedAdditions._instance;
    }

    @Override
    public PartStateReaderBase<PartTypeDynamicSetterReader> constructDefaultState() {
        return new PartStateReaderBase<>();
    }

    @Override
    public int getConsumptionRate(PartStateReaderBase<PartTypeDynamicSetterReader> state) {
        return GeneralConfig.dynamicSetterReaderBaseConsumption;
    }

    public <P extends IPartType<P, S>, S extends IPartState<P>> void onUpdate(INetwork network, IPartNetwork partNetwork, P partType, PartTarget target, S state) {

    }
}