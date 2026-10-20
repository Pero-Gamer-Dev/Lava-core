/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.runtime.entity;

import io.github.lavacore.performance.LavaMetrics;
import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight entity wrapper cache. 1.0.7 focuses on players primarily;
 * this registry is the extension point for LivingEntity / Entity later.
 */
public final class EntityRegistry {

    private final Map<UUID, Entity> byUuid = new ConcurrentHashMap<>();

    public void register(UUID id, Entity entity) {
        if (id == null || entity == null) return;
        byUuid.put(id, entity);
        LavaMetrics.recordCacheMiss("entity");
    }

    public Entity get(UUID id) {
        if (id == null) return null;
        Entity e = byUuid.get(id);
        if (e != null) {
            LavaMetrics.recordCacheHit("entity");
        } else {
            LavaMetrics.recordCacheMiss("entity");
        }
        return e;
    }

    public void unregister(UUID id) {
        if (id != null) byUuid.remove(id);
    }

    public int size() {
        return byUuid.size();
    }

    public void clear() {
        byUuid.clear();
    }
}
