/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package org.bukkit.craftbukkit.entity;
import net.minecraft.server.level.ServerPlayer;
public class CraftPlayer extends org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer {
    public CraftPlayer(ServerPlayer handle){ super(handle); }
}
