package com.crazymeow.integratedadditions.part.aspect;

import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AdditionAspects {

    public static void load() {
    }

    public static final class Read {
        public static final class DynamicSetter {

            private static final Random RANDOM = new Random();

            public static final IAspectValuePropagator<Integer, ValueTypeInteger.ValueInteger> PROP_GET_INTEGER = ValueTypeInteger.ValueInteger::of;

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_RANDOM_BETWEEN =
                    AdditionAspectReadBuilders.DynamicSetter.BUILDER_INTEGER_RANDOM.handle(input -> {
                        IAspectProperties properties = input.getRight();
                        int minValue = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_MIN_VALUE).getRawValue();
                        int maxValue = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_MAX_VALUE).getRawValue();
                        if (Math.max(minValue, maxValue) == minValue) {
                            minValue = minValue + maxValue;
                            maxValue = minValue - maxValue;
                            minValue = minValue - maxValue;
                        }
                        return Math.abs(RANDOM.nextInt()) % (maxValue + 1 - minValue) + minValue;
                    }).handle(PROP_GET_INTEGER, "randombetween").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_IDENTITY_BETWEEN =
                    AdditionAspectReadBuilders.DynamicSetter.BUILDER_INTEGER_IDENTITY.handle(input -> {
                        IAspectProperties properties = input.getRight();
                        int minValue = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_MIN_VALUE).getRawValue();
                        int maxValue = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_MAX_VALUE).getRawValue();
                        int increment = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_INCREMENT).getRawValue();
                        int interval = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_INTERVAL).getRawValue();
                        if (Math.max(minValue, maxValue) == minValue) {
                            minValue = minValue + maxValue;
                            maxValue = minValue - maxValue;
                            minValue = minValue - maxValue;
                        }
                        long tick = input.getLeft().getTarget().getPos().getWorld(true).getGameTime();
                        int num = (int) ((tick / interval % (maxValue + 1 - minValue) / Math.abs(increment)) * increment);
                        if (increment > 0) num += minValue;
                        else num += maxValue;
                        return num;
                    }).handle(PROP_GET_INTEGER, "identitybetween").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ARRAY_BETWEEN =
                    AdditionAspectReadBuilders.DynamicSetter.BUILDER_INTEGER_ARRAY.handle(input -> {
                        IAspectProperties properties = input.getRight();
                        String integerArray = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_INTEGER_ARRAY).getRawValue();
                        Scanner scanner = new Scanner(integerArray).useDelimiter(",");
                        List<Integer> array = new ArrayList<>();
                        while (scanner.hasNext()) array.add(scanner.nextInt());
                        int interval = properties.getValue(AdditionAspectReadBuilders.DynamicSetter.PROP_INTERVAL).getRawValue();
                        long tick = input.getLeft().getTarget().getPos().getWorld(true).getGameTime();
                        return array.get((int) (tick / interval % array.size()));
                    }).handle(PROP_GET_INTEGER, "arraybetween").buildRead();
        }
    }
}
