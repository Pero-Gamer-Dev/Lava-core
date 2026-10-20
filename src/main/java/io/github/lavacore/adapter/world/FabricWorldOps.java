/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.adapter.world;

import io.github.lavacore.ops.DirectWorldOps;
import io.github.lavacore.performance.LavaMetrics;
import io.github.lavacore.performance.MethodCache;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * World ops: DIRECT first (1.0.7), reflective fallback only when type is unknown.
 */
public final class FabricWorldOps {
    private FabricWorldOps() {}

    public static long getDayTime(Object serverLevel) {
        if (serverLevel instanceof Level level) {
            return DirectWorldOps.getDayTime(level);
        }
        try {
            LavaMetrics.recordReflection("WORLD_TIME");
            Method m = MethodCache.find(serverLevel.getClass(), "getDayTime", 0);
            if (m != null) {
                Object v = m.invoke(serverLevel);
                if (v instanceof Number n) return n.longValue();
            }
        } catch (Throwable ignored) {}
        return 0L;
    }

    public static void setDayTime(Object serverLevel, long time) {
        if (serverLevel instanceof ServerLevel level) {
            DirectWorldOps.setDayTime(level, time);
            return;
        }
        try {
            LavaMetrics.recordReflection("WORLD_TIME");
            Method m = MethodCache.find(serverLevel.getClass(), "setDayTime", 1);
            if (m != null) m.invoke(serverLevel, time);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static long getGameTime(Object serverLevel) {
        if (serverLevel instanceof Level level) {
            return DirectWorldOps.getGameTime(level);
        }
        try {
            LavaMetrics.recordReflection("WORLD_TIME");
            Method m = MethodCache.find(serverLevel.getClass(), "getGameTime", 0);
            if (m != null) {
                Object v = m.invoke(serverLevel);
                if (v instanceof Number n) return n.longValue();
            }
        } catch (Throwable ignored) {}
        return 0L;
    }

    public static int getMinHeight(Object serverLevel) {
        if (serverLevel instanceof Level level) {
            return DirectWorldOps.getMinBuildHeight(level);
        }
        try {
            LavaMetrics.recordReflection("WORLD_HEIGHT");
            Method m = MethodCache.find(serverLevel.getClass(), "getMinBuildHeight", 0);
            if (m != null) {
                Object v = m.invoke(serverLevel);
                if (v instanceof Number n) return n.intValue();
            }
        } catch (Throwable ignored) {}
        return -64;
    }

    public static int getMaxHeight(Object serverLevel) {
        if (serverLevel instanceof Level level) {
            return DirectWorldOps.getMaxBuildHeight(level);
        }
        try {
            LavaMetrics.recordReflection("WORLD_HEIGHT");
            Method m = MethodCache.find(serverLevel.getClass(), "getMaxBuildHeight", 0);
            if (m != null) {
                Object v = m.invoke(serverLevel);
                if (v instanceof Number n) return n.intValue();
            }
        } catch (Throwable ignored) {}
        return 320;
    }

    public static int[] getSpawn(Object serverLevel) {
        if (serverLevel instanceof ServerLevel level) {
            BlockPos pos = DirectWorldOps.getSharedSpawnPos(level);
            return new int[]{pos.getX(), pos.getY(), pos.getZ()};
        }
        try {
            LavaMetrics.recordReflection("WORLD_SPAWN");
            Object data = levelData(serverLevel);
            if (data != null) {
                Method m = MethodCache.find(data.getClass(), "getSpawnPos", 0);
                if (m == null) m = MethodCache.find(data.getClass(), "getSpawn", 0);
                if (m != null) {
                    Object pos = m.invoke(data);
                    if (pos != null) {
                        Method gx = MethodCache.find(pos.getClass(), "getX", 0);
                        Method gy = MethodCache.find(pos.getClass(), "getY", 0);
                        Method gz = MethodCache.find(pos.getClass(), "getZ", 0);
                        return new int[]{
                                ((Number) gx.invoke(pos)).intValue(),
                                ((Number) gy.invoke(pos)).intValue(),
                                ((Number) gz.invoke(pos)).intValue()
                        };
                    }
                }
            }
        } catch (Throwable ignored) {}
        return new int[]{0, 64, 0};
    }

    public static boolean hasStorm(Object serverLevel) {
        if (serverLevel instanceof Level level) {
            return DirectWorldOps.isRaining(level);
        }
        try {
            LavaMetrics.recordReflection("WORLD_WEATHER");
            Object data = levelData(serverLevel);
            if (data != null) {
                Method m = MethodCache.find(data.getClass(), "isRaining", 0);
                if (m != null) {
                    Object v = m.invoke(data);
                    if (v instanceof Boolean b) return b;
                }
            }
        } catch (Throwable ignored) {}
        return false;
    }

    public static void setStorm(Object serverLevel, boolean storm) {
        if (serverLevel instanceof ServerLevel level) {
            DirectWorldOps.setRaining(level, storm);
            return;
        }
        try {
            LavaMetrics.recordReflection("WORLD_WEATHER");
            Object data = levelData(serverLevel);
            if (data != null) {
                Method m = MethodCache.find(data.getClass(), "setRaining", 1);
                if (m != null) m.invoke(data, storm);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static Object levelData(Object serverLevel) {
        Field f = MethodCache.field(serverLevel.getClass(), "serverLevelData");
        if (f == null) f = MethodCache.field(serverLevel.getClass(), "levelData");
        try {
            return f != null ? f.get(serverLevel) : null;
        } catch (Throwable t) {
            return null;
        }
    }
}
