package com.crazymeow.integratedadditions.part.aspect;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectPropertyTypeInstance;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeString;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.part.aspect.build.AspectBuilder;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectProperties;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectPropertyTypeInstance;

import java.util.Scanner;
import java.util.function.Predicate;

public class AdditionAspectReadBuilders {

    public static final AspectBuilder<ValueTypeInteger.ValueInteger, ValueTypeInteger, Pair<PartTarget, IAspectProperties>> BUILDER_INTEGER = AspectBuilder.forReadType(ValueTypes.INTEGER).byMod(IntegratedAdditions._instance);

    public static final Predicate<ValueTypeInteger.ValueInteger> VALIDATOR_INTEGER_POSITIVE = input -> input.getRawValue() > 0;
    public static final Predicate<ValueTypeInteger.ValueInteger> VALIDATOR_INTEGER = input -> true;
    public static final Predicate<ValueTypeInteger.ValueInteger> VALIDATOR_INTEGER_NONZERO_POSITIVE = input -> input.getRawValue() != 0;
    public static final Predicate<ValueTypeString.ValueString> VALIDATOR_STRING_POSITIVE = input -> {
        Scanner scanner = new Scanner(input.getRawValue()).useDelimiter(",");
        boolean isValid = false;
        try {
            while (scanner.hasNext()) {
                isValid = true;
                scanner.nextInt();
            }
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    };

    public static final class DynamicSetter {
        public static final IAspectPropertyTypeInstance<ValueTypeInteger, ValueTypeInteger.ValueInteger> PROP_MIN_VALUE =
                new AspectPropertyTypeInstance<>(ValueTypes.INTEGER, "aspect.aspecttypes.integratedadditions.integer.min_value", VALIDATOR_INTEGER);

        public static final IAspectPropertyTypeInstance<ValueTypeInteger, ValueTypeInteger.ValueInteger> PROP_MAX_VALUE =
                new AspectPropertyTypeInstance<>(ValueTypes.INTEGER, "aspect.aspecttypes.integratedadditions.integer.max_value", VALIDATOR_INTEGER);

        public static final IAspectPropertyTypeInstance<ValueTypeInteger, ValueTypeInteger.ValueInteger> PROP_INTERVAL =
                new AspectPropertyTypeInstance<>(ValueTypes.INTEGER, "aspect.aspecttypes.integratedadditions.integer.interval", VALIDATOR_INTEGER_POSITIVE);

        public static final IAspectPropertyTypeInstance<ValueTypeInteger, ValueTypeInteger.ValueInteger> PROP_INCREMENT =
                new AspectPropertyTypeInstance<>(ValueTypes.INTEGER, "aspect.aspecttypes.integratedadditions.integer.increment", VALIDATOR_INTEGER_NONZERO_POSITIVE);

        public static final IAspectPropertyTypeInstance<ValueTypeString, ValueTypeString.ValueString> PROP_INTEGER_ARRAY =
                new AspectPropertyTypeInstance<>(ValueTypes.STRING, "aspect.aspecttypes.integratedadditions.string.integer_array", VALIDATOR_STRING_POSITIVE);

        public static final IAspectProperties PROPERTIES_RANDOM = new AspectProperties(ImmutableList.<IAspectPropertyTypeInstance>of(
                PROP_MIN_VALUE,
                PROP_MAX_VALUE
        ));

        static {
            PROPERTIES_RANDOM.setValue(PROP_MIN_VALUE, ValueTypeInteger.ValueInteger.of(0));
            PROPERTIES_RANDOM.setValue(PROP_MAX_VALUE, ValueTypeInteger.ValueInteger.of(100));
        }

        public static final IAspectProperties PROPERTIES_IDENTITY = new AspectProperties(ImmutableList.<IAspectPropertyTypeInstance>of(
                PROP_MIN_VALUE,
                PROP_MAX_VALUE,
                PROP_INCREMENT,
                PROP_INTERVAL
        ));

        static {
            PROPERTIES_IDENTITY.setValue(PROP_MIN_VALUE, ValueTypeInteger.ValueInteger.of(0));
            PROPERTIES_IDENTITY.setValue(PROP_MAX_VALUE, ValueTypeInteger.ValueInteger.of(100));
            PROPERTIES_IDENTITY.setValue(PROP_INCREMENT, ValueTypeInteger.ValueInteger.of(1));
            PROPERTIES_IDENTITY.setValue(PROP_INTERVAL, ValueTypeInteger.ValueInteger.of(1));
        }

        public static final IAspectProperties PROPERTIES_ARRAY = new AspectProperties(ImmutableList.<IAspectPropertyTypeInstance>of(
                PROP_INTEGER_ARRAY,
                PROP_INTERVAL
        ));

        static {
            PROPERTIES_ARRAY.setValue(PROP_INTEGER_ARRAY, ValueTypeString.ValueString.of("5,50,13,88,93"));
            PROPERTIES_ARRAY.setValue(PROP_INTERVAL, ValueTypeInteger.ValueInteger.of(1));
        }

        public static final IAspectValuePropagator<Pair<PartTarget, IAspectProperties>, Pair<PartTarget, IAspectProperties>> PROP_GET = input -> {

            return input;
        };

        public static final AspectBuilder<ValueTypeInteger.ValueInteger, ValueTypeInteger, Pair<PartTarget, IAspectProperties>>
                BUILDER_INTEGER_RANDOM = BUILDER_INTEGER.handle(PROP_GET, "dynamicsetter").withProperties(PROPERTIES_RANDOM);

        public static final AspectBuilder<ValueTypeInteger.ValueInteger, ValueTypeInteger, Pair<PartTarget, IAspectProperties>>
                BUILDER_INTEGER_IDENTITY = BUILDER_INTEGER.handle(PROP_GET, "dynamicsetter").withProperties(PROPERTIES_IDENTITY);

        public static final AspectBuilder<ValueTypeInteger.ValueInteger, ValueTypeInteger, Pair<PartTarget, IAspectProperties>>
                BUILDER_INTEGER_ARRAY = BUILDER_INTEGER.handle(PROP_GET, "dynamicsetter").withProperties(PROPERTIES_ARRAY);

    }

}
