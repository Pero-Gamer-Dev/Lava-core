/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
// ============================================================================
// ============================================================================
package io.github.lavacore.mixin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import io.github.lavacore.bridge.world.LavaWorldHolder;
import io.github.lavacore.bridge.world.LavaWorldWrapper;

/**
 * Adds a plain field to ServerLevel to hold its Bukkit World wrapper. This is
 * the same technique CraftBukkit/Paper use for entities (a cached
 * getBukkitEntity() field on the NMS class) — a direct field read instead of
 * a hashmap lookup + lock on every access.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelWorldMixin implements LavaWorldHolder {

    @Unique
    private LavaWorldWrapper lava$bukkitWorld;

    @Override
    public LavaWorldWrapper lava$getBukkitWorld() {
        return this.lava$bukkitWorld;
    }

    @Override
    public void lava$setBukkitWorld(LavaWorldWrapper wrapper) {
        this.lava$bukkitWorld = wrapper;
    }
}
