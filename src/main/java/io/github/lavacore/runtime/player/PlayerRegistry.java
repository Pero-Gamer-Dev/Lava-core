/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.runtime.player;

import io.github.lavacore.bridge.entity.LavaPlayerWrapper;
import io.github.lavacore.performance.LavaMetrics;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * First-class registry for online player wrappers.
 * Join → register, quit → unregister. Hot path: UUID → cached wrapper.
 */
public final class PlayerRegistry {

    private final Map<UUID, LavaPlayerWrapper> byUuid = new ConcurrentHashMap<>();
    private final Map<String, UUID> nameIndex = new ConcurrentHashMap<>();

    public LavaPlayerWrapper register(ServerPlayer nms) {
        if (nms == null) return null;
        UUID id = nms.getUUID();
        LavaPlayerWrapper existing = byUuid.get(id);
        if (existing != null) {
            LavaMetrics.recordCacheHit("player");
            return existing;
        }
        // Prefer versioned CraftPlayer alias when available for plugin compatibility
        LavaPlayerWrapper wrapper;
        try {
            wrapper = new org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer(nms);
        } catch (Throwable t) {
            wrapper = new LavaPlayerWrapper(nms);
        }
        byUuid.put(id, wrapper);
        nameIndex.put(nms.getGameProfile().getName().toLowerCase(), id);
        LavaMetrics.recordCacheMiss("player");
        return wrapper;
    }

    public void unregister(UUID id) {
        if (id == null) return;
        LavaPlayerWrapper removed = byUuid.remove(id);
        if (removed != null) {
            nameIndex.remove(removed.getName().toLowerCase());
        }
    }

    public Player get(UUID id) {
        if (id == null) return null;
        LavaPlayerWrapper w = byUuid.get(id);
        if (w != null) {
            LavaMetrics.recordCacheHit("player");
            return w;
        }
        LavaMetrics.recordCacheMiss("player");
        return null;
    }

    public Player getExact(String name) {
        if (name == null) return null;
        UUID id = nameIndex.get(name.toLowerCase());
        return id != null ? get(id) : null;
    }

    public Player get(String name) {
        if (name == null || name.isEmpty()) return null;
        Player exact = getExact(name);
        if (exact != null) return exact;
        String lower = name.toLowerCase();
        for (Map.Entry<String, UUID> e : nameIndex.entrySet()) {
            if (e.getKey().startsWith(lower)) {
                return get(e.getValue());
            }
        }
        return null;
    }

    public boolean contains(UUID id) {
        return id != null && byUuid.containsKey(id);
    }

    public int size() {
        return byUuid.size();
    }

    public Collection<? extends Player> all() {
        return Collections.unmodifiableCollection(byUuid.values());
    }

    public void clear() {
        byUuid.clear();
        nameIndex.clear();
    }
}
