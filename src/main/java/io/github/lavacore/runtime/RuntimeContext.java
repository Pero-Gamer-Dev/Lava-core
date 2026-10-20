/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.runtime;

import io.github.lavacore.runtime.entity.EntityRegistry;
import io.github.lavacore.runtime.player.PlayerRegistry;
import io.github.lavacore.runtime.world.WorldRegistry;
import io.github.lavacore.scheduler.LavaScheduler;

/**
 * Central runtime context. Owned by LavaServer; avoids scattered global state.
 */
public final class RuntimeContext {

    private static volatile RuntimeContext INSTANCE;

    private final PlayerRegistry players = new PlayerRegistry();
    private final WorldRegistry worlds = new WorldRegistry();
    private final EntityRegistry entities = new EntityRegistry();
    private final RuntimeCapabilities capabilities = new RuntimeCapabilities();
    private LavaScheduler scheduler;

    public static RuntimeContext get() {
        return INSTANCE;
    }

    public static RuntimeContext create() {
        RuntimeContext ctx = new RuntimeContext();
        INSTANCE = ctx;
        return ctx;
    }

    public static void clear() {
        INSTANCE = null;
    }

    public PlayerRegistry players() {
        return players;
    }

    public WorldRegistry worlds() {
        return worlds;
    }

    public EntityRegistry entities() {
        return entities;
    }

    public RuntimeCapabilities capabilities() {
        return capabilities;
    }

    public LavaScheduler scheduler() {
        return scheduler;
    }

    public void setScheduler(LavaScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public boolean isEnabled() {
        return INSTANCE == this;
    }
}
