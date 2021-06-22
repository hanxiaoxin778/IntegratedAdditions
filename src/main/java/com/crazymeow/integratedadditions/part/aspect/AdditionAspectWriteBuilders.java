package com.crazymeow.integratedadditions.part.aspect;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectPropertyTypeInstance;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeEntity;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeDouble;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.part.aspect.build.AspectBuilder;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectProperties;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectPropertyTypeInstance;
import org.cyclops.integrateddynamics.part.aspect.write.AspectWriteBuilders;

import java.util.Optional;
import java.util.function.Predicate;

public class AdditionAspectWriteBuilders {
    public static final IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueObjectTypeEntity.ValueEntity>, Triple<PartTarget, IAspectProperties, Optional<net.minecraft.entity.Entity>>>
            PROP_GET_ENTITY = input -> Triple.of(input.getLeft(), input.getMiddle(), input.getRight().getRawValue());

    public static final Predicate<ValueTypeDouble.ValueDouble> VALIDATOR_DOUBLE = input -> input.getRawValue() <= 10000 && input.getRawValue() >= -10000;

    public static final IAspectPropertyTypeInstance<ValueTypeDouble, ValueTypeDouble.ValueDouble> PROP_OFFSET_X =
            new AspectPropertyTypeInstance<>(ValueTypes.DOUBLE, "aspect.aspecttypes.integrateddynamics.double.offset_x", VALIDATOR_DOUBLE);
    public static final IAspectPropertyTypeInstance<ValueTypeDouble, ValueTypeDouble.ValueDouble> PROP_OFFSET_Y =
            new AspectPropertyTypeInstance<>(ValueTypes.DOUBLE, "aspect.aspecttypes.integrateddynamics.double.offset_y", VALIDATOR_DOUBLE);
    public static final IAspectPropertyTypeInstance<ValueTypeDouble, ValueTypeDouble.ValueDouble> PROP_OFFSET_Z =
            new AspectPropertyTypeInstance<>(ValueTypes.DOUBLE, "aspect.aspecttypes.integrateddynamics.double.offset_z", VALIDATOR_DOUBLE);


    public static final IAspectProperties PROPERTIES_PARTICLE = new AspectProperties(ImmutableList.<IAspectPropertyTypeInstance>of(
            PROP_OFFSET_X,
            PROP_OFFSET_Y,
            PROP_OFFSET_Z
    ));

    static {
        PROPERTIES_PARTICLE.setValue(PROP_OFFSET_X, ValueTypeDouble.ValueDouble.of(0.5D));
        PROPERTIES_PARTICLE.setValue(PROP_OFFSET_Y, ValueTypeDouble.ValueDouble.of(0.5D));
        PROPERTIES_PARTICLE.setValue(PROP_OFFSET_Z, ValueTypeDouble.ValueDouble.of(0.5D));
    }

    public static final AspectBuilder<ValueObjectTypeEntity.ValueEntity, ValueObjectTypeEntity, Triple<PartTarget, IAspectProperties, ValueObjectTypeEntity.ValueEntity>>
            BUILDER_ENTITY = AspectWriteBuilders.getValue(AspectBuilder.forWriteType(ValueTypes.OBJECT_ENTITY)).byMod(IntegratedAdditions._instance);

    public static final class Entity {
        public static final AspectBuilder<ValueObjectTypeEntity.ValueEntity, ValueObjectTypeEntity, Triple<PartTarget, IAspectProperties, Optional<net.minecraft.entity.Entity>>>
                BUILDER_ENTITY_TELEPORTER = AdditionAspectWriteBuilders.BUILDER_ENTITY.appendKind("entity").handle(PROP_GET_ENTITY).withProperties(PROPERTIES_PARTICLE);
    }
}
