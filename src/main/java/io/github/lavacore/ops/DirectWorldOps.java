/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.ops;

import io.github.lavacore.performance.LavaMetrics;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

/**
 * Direct world operations — no reflection on the hot path.
 */
public final class DirectWorldOps {
    private DirectWorldOps() {}

    public static long getDayTime(Level level) {
        LavaMetrics.recordDirect("WORLD_TIME");
        return level.getDayTime();
    }

    public static void setDayTime(ServerLevel level, long time) {
        LavaMetrics.recordDirect("WORLD_TIME");
        level.setDayTime(time);
    }

    public static long getGameTime(Level level) {
        LavaMetrics.recordDirect("WORLD_TIME");
        return level.getGameTime();
    }

    public static int getMinBuildHeight(Level level) {
        LavaMetrics.recordDirect("WORLD_HEIGHT");
        return level.getMinY();
    }

    public static int getMaxBuildHeight(Level level) {
        LavaMetrics.recordDirect("WORLD_HEIGHT");
        return level.getMinY() + level.getHeight();
    }

    public static BlockPos getSharedSpawnPos(ServerLevel level) {
        LavaMetrics.recordDirect("WORLD_SPAWN");
        return level.getSharedSpawnPos();
    }

    public static float getSharedSpawnAngle(ServerLevel level) {
        LavaMetrics.recordDirect("WORLD_SPAWN");
        return level.getSharedSpawnAngle();
    }

    public static boolean isRaining(Level level) {
        LavaMetrics.recordDirect("WORLD_WEATHER");
        return level.isRaining();
    }

    public static boolean isThundering(Level level) {
        LavaMetrics.recordDirect("WORLD_WEATHER");
        return level.isThundering();
    }

    public static void setRaining(ServerLevel level, boolean raining) {
        LavaMetrics.recordDirect("WORLD_WEATHER");
        try {
            Object data = level.getLevelData();
            if (data instanceof ServerLevelData sld) {
                sld.setRaining(raining);
                if (raining) {
                    sld.setClearWeatherTime(0);
                    sld.setRainTime(6000);
                } else {
                    sld.setClearWeatherTime(6000);
                    sld.setRainTime(0);
                }
            }
        } catch (Throwable t) {
            // best-effort
        }
    }

    public static void setThundering(ServerLevel level, boolean thundering) {
        LavaMetrics.recordDirect("WORLD_WEATHER");
        try {
            Object data = level.getLevelData();
            if (data instanceof ServerLevelData sld) {
                sld.setThundering(thundering);
                if (thundering) {
                    sld.setThunderTime(6000);
                } else {
                    sld.setThunderTime(0);
                }
            }
        } catch (Throwable t) {
            // best-effort
        }
    }

    public static String dimensionName(Level level) {
        LavaMetrics.recordDirect("WORLD_IDENTITY");
        return level.dimension().location().toString();
    }

    public static void clearWeather(ServerLevel level) {
        LavaMetrics.recordDirect("WORLD_WEATHER");
        setRaining(level, false);
        setThundering(level, false);
    }
}
