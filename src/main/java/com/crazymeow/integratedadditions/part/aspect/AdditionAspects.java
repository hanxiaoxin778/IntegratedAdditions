package com.crazymeow.integratedadditions.part.aspect;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectWrite;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeEntity;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;

import java.util.*;

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

    public static final class Write {
        public static final class Entity {
            public static IAspectWrite<ValueObjectTypeEntity.ValueEntity, ValueObjectTypeEntity> BUILDER_ENTITY_TELEPORTER = AdditionAspectWriteBuilders.Entity.BUILDER_ENTITY_TELEPORTER.appendKind("teleporter")
                    .handle(input -> {
                        Optional<net.minecraft.entity.Entity> right = input.getRight();
                        if (right.isPresent()) {
                            BlockPos partPos = input.getLeft().getCenter().getPos().getBlockPos();
                            IAspectProperties properties = input.getMiddle();
                            double x = properties.getValue(AdditionAspectWriteBuilders.PROP_OFFSET_X).getRawValue() + partPos.getX();
                            double y = properties.getValue(AdditionAspectWriteBuilders.PROP_OFFSET_Y).getRawValue() + partPos.getY();
                            double z = properties.getValue(AdditionAspectWriteBuilders.PROP_OFFSET_Z).getRawValue() + partPos.getZ();
                            net.minecraft.entity.Entity entity = right.get();
                            ServerWorld world = (ServerWorld) entity.world;
                            if (entity instanceof ServerPlayerEntity) {
                                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                                ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
                                world.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.getEntityId());
                                player.stopRiding();
                                if (player.isSleeping()) {
                                    player.stopSleepInBed(true, true);
                                }
                                player.connection.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
                                player.setRotationYawHead(player.rotationYaw);
                            } else {
                                float yaw = MathHelper.wrapDegrees(entity.rotationYaw);
                                float pitch = MathHelper.wrapDegrees(entity.rotationPitch);
                                pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
                                entity.setLocationAndAngles(x, y, z, yaw, pitch);
                                entity.setRotationYawHead(yaw);
                            }
                        }
                        return null;
                    }).buildWrite();
        }

    }
}
