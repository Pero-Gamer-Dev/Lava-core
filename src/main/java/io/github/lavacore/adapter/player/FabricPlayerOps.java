/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.adapter.player;

import io.github.lavacore.ops.DirectPlayerOps;
import io.github.lavacore.performance.LavaMetrics;
import io.github.lavacore.performance.MethodCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Player ops: DIRECT first (1.0.7), reflective fallback only if direct path fails.
 */
public final class FabricPlayerOps {
    private FabricPlayerOps() {}

    public static boolean teleport(Object serverPlayer, Object serverLevel,
                                   double x, double y, double z, float yaw, float pitch) {
        if (serverPlayer instanceof ServerPlayer sp) {
            ServerLevel level = serverLevel instanceof ServerLevel sl ? sl : null;
            return DirectPlayerOps.teleport(sp, level, x, y, z, yaw, pitch);
        }
        // Reflective fallback
        if (serverPlayer == null) return false;
        try {
            LavaMetrics.recordReflection("PLAYER_TELEPORT");
            Method m = MethodCache.find(serverPlayer.getClass(), "teleportTo", 7);
            if (m == null) m = MethodCache.findContains(serverPlayer.getClass(), "teleport", 7);
            if (m != null && m.getParameterCount() == 7) {
                Object level = serverLevel != null ? serverLevel : getEntityLevel(serverPlayer);
                m.invoke(serverPlayer, level, x, y, z, Set.of(), yaw, pitch);
                return true;
            }
            m = MethodCache.find(serverPlayer.getClass(), "teleportTo", 5);
            if (m != null) {
                m.invoke(serverPlayer, x, y, z, yaw, pitch);
                return true;
            }
            Method setPos = MethodCache.find(serverPlayer.getClass(), "setPos", 3);
            if (setPos != null) setPos.invoke(serverPlayer, x, y, z);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public static void setFoodLevel(Object serverPlayer, int level) {
        if (serverPlayer instanceof ServerPlayer sp) {
            DirectPlayerOps.setFoodLevel(sp, level);
            return;
        }
        try {
            LavaMetrics.recordReflection("PLAYER_FOOD");
            Object food = foodData(serverPlayer);
            if (food == null) return;
            level = Math.max(0, Math.min(20, level));
            Method m = MethodCache.find(food.getClass(), "setFoodLevel", 1);
            if (m != null) {
                m.invoke(food, level);
                return;
            }
            Field f = MethodCache.field(food.getClass(), "foodLevel");
            if (f != null) f.setInt(food, level);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getFoodLevel(Object serverPlayer) {
        if (serverPlayer instanceof ServerPlayer sp) {
            return DirectPlayerOps.getFoodLevel(sp);
        }
        try {
            LavaMetrics.recordReflection("PLAYER_FOOD");
            Object food = foodData(serverPlayer);
            if (food == null) return 20;
            Method m = MethodCache.find(food.getClass(), "getFoodLevel", 0);
            if (m != null) {
                Object v = m.invoke(food);
                if (v instanceof Number n) return n.intValue();
            }
            Field f = MethodCache.field(food.getClass(), "foodLevel");
            if (f != null) return f.getInt(food);
        } catch (Throwable ignored) {}
        return 20;
    }

    public static void setSaturation(Object serverPlayer, float saturation) {
        if (serverPlayer instanceof ServerPlayer sp) {
            DirectPlayerOps.setSaturation(sp, saturation);
            return;
        }
        try {
            LavaMetrics.recordReflection("PLAYER_FOOD");
            Object food = foodData(serverPlayer);
            if (food == null) return;
            Method m = MethodCache.find(food.getClass(), "setSaturation", 1);
            if (m == null) m = MethodCache.findContains(food.getClass(), "Saturation", 1);
            if (m != null) {
                m.invoke(food, saturation);
                return;
            }
            Field f = MethodCache.field(food.getClass(), "saturationLevel");
            if (f != null) f.setFloat(food, saturation);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static float getSaturation(Object serverPlayer) {
        if (serverPlayer instanceof ServerPlayer sp) {
            return DirectPlayerOps.getSaturation(sp);
        }
        try {
            LavaMetrics.recordReflection("PLAYER_FOOD");
            Object food = foodData(serverPlayer);
            if (food == null) return 0f;
            Method m = MethodCache.find(food.getClass(), "getSaturationLevel", 0);
            if (m != null) {
                Object v = m.invoke(food);
                if (v instanceof Number n) return n.floatValue();
            }
            Field f = MethodCache.field(food.getClass(), "saturationLevel");
            if (f != null) return f.getFloat(food);
        } catch (Throwable ignored) {}
        return 0f;
    }

    public static void setExperienceLevel(Object serverPlayer, int level) {
        if (serverPlayer instanceof ServerPlayer sp) {
            DirectPlayerOps.setXpLevel(sp, level);
            return;
        }
        try {
            LavaMetrics.recordReflection("PLAYER_XP");
            Field f = MethodCache.field(serverPlayer.getClass(), "experienceLevel");
            if (f != null) f.setInt(serverPlayer, Math.max(0, level));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getExperienceLevel(Object serverPlayer) {
        if (serverPlayer instanceof ServerPlayer sp) {
            return DirectPlayerOps.getXpLevel(sp);
        }
        try {
            LavaMetrics.recordReflection("PLAYER_XP");
            Field f = MethodCache.field(serverPlayer.getClass(), "experienceLevel");
            if (f != null) return f.getInt(serverPlayer);
        } catch (Throwable ignored) {}
        return 0;
    }

    private static Object foodData(Object serverPlayer) {
        if (serverPlayer instanceof ServerPlayer sp) {
            return sp.getFoodData();
        }
        try {
            Method m = MethodCache.find(serverPlayer.getClass(), "getFoodData", 0);
            if (m != null) return m.invoke(serverPlayer);
            Field f = MethodCache.field(serverPlayer.getClass(), "foodData");
            if (f != null) return f.get(serverPlayer);
        } catch (Throwable ignored) {}
        return null;
    }

    private static Object getEntityLevel(Object entity) {
        try {
            Method m = MethodCache.find(entity.getClass(), "level", 0);
            if (m == null) m = MethodCache.find(entity.getClass(), "getLevel", 0);
            if (m != null) return m.invoke(entity);
        } catch (Throwable ignored) {}
        return null;
    }
}
