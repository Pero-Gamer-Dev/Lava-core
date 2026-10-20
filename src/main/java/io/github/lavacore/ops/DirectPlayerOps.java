/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.ops;

import io.github.lavacore.performance.LavaMetrics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import java.util.Set;

/**
 * Direct Minecraft API operations for players — no reflection on the hot path.
 */
public final class DirectPlayerOps {
    private DirectPlayerOps() {}

    public static double getHealth(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_HEALTH");
        return player.getHealth();
    }

    public static void setHealth(ServerPlayer player, double health) {
        LavaMetrics.recordDirect("PLAYER_HEALTH");
        player.setHealth((float) Math.max(0.0, health));
    }

    public static int getFoodLevel(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_FOOD");
        return player.getFoodData().getFoodLevel();
    }

    public static void setFoodLevel(ServerPlayer player, int level) {
        LavaMetrics.recordDirect("PLAYER_FOOD");
        player.getFoodData().setFoodLevel(Math.max(0, Math.min(20, level)));
    }

    public static float getSaturation(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_FOOD");
        return player.getFoodData().getSaturationLevel();
    }

    public static void setSaturation(ServerPlayer player, float saturation) {
        LavaMetrics.recordDirect("PLAYER_FOOD");
        player.getFoodData().setSaturation(Math.max(0f, saturation));
    }

    public static int getXpLevel(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_XP");
        return player.experienceLevel;
    }

    public static void setXpLevel(ServerPlayer player, int level) {
        LavaMetrics.recordDirect("PLAYER_XP");
        player.experienceLevel = Math.max(0, level);
    }

    public static float getExp(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_XP");
        return player.experienceProgress;
    }

    public static void setExp(ServerPlayer player, float progress) {
        LavaMetrics.recordDirect("PLAYER_XP");
        player.experienceProgress = Math.max(0f, Math.min(1f, progress));
    }

    public static int getTotalExperience(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_XP");
        return player.totalExperience;
    }

    public static void setTotalExperience(ServerPlayer player, int total) {
        LavaMetrics.recordDirect("PLAYER_XP");
        player.totalExperience = Math.max(0, total);
    }

    public static boolean teleport(ServerPlayer player, ServerLevel level,
                                   double x, double y, double z, float yaw, float pitch) {
        LavaMetrics.recordDirect("PLAYER_TELEPORT");
        if (player == null) return false;
        ServerLevel target = level != null ? level : (ServerLevel) player.level();
        // 1.21.4: teleportTo(ServerLevel, double, double, double, Set<Relative>, float, float, boolean)
        player.teleportTo(target, x, y, z, java.util.Collections.emptySet(), yaw, pitch, true);
        return true;
    }

    public static void setGameMode(ServerPlayer player, GameType type) {
        LavaMetrics.recordDirect("PLAYER_GAMEMODE");
        player.setGameMode(type);
    }

    public static GameType getGameMode(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_GAMEMODE");
        return player.gameMode.getGameModeForPlayer();
    }

    public static Level getWorldLevel(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_LOCATION");
        return player.level();
    }

    public static double getX(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_LOCATION");
        return player.getX();
    }

    public static double getY(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_LOCATION");
        return player.getY();
    }

    public static double getZ(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_LOCATION");
        return player.getZ();
    }

    public static float getYaw(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_LOCATION");
        return player.getYRot();
    }

    public static float getPitch(ServerPlayer player) {
        LavaMetrics.recordDirect("PLAYER_LOCATION");
        return player.getXRot();
    }
}
