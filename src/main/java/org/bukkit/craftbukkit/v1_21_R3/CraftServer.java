/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package org.bukkit.craftbukkit.v1_21_R3;
import net.minecraft.server.dedicated.DedicatedServer;
import io.github.lavacore.bridge.LavaServer;
public class CraftServer extends LavaServer {
    public CraftServer(DedicatedServer vanilla){ super(vanilla); }
    public DedicatedServer getServer(){ return getHandle(); }
}
