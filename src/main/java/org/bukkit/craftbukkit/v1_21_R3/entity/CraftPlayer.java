/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package org.bukkit.craftbukkit.v1_21_R3.entity;
import net.minecraft.server.level.ServerPlayer;
import io.github.lavacore.bridge.entity.LavaPlayerWrapper;
public class CraftPlayer extends LavaPlayerWrapper {
    public CraftPlayer(ServerPlayer handle){ super(handle); }
}
