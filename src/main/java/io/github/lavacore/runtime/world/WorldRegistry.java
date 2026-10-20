/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.runtime.world;

import io.github.lavacore.bridge.world.LavaWorldWrapper;
import io.github.lavacore.performance.LavaMetrics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central world wrapper registry. Prefer WorldRegistry.get(level) over new LavaWorldWrapper(...).
 * LavaWorldWrapper.of still uses the per-level holder cache; this registry tracks all known worlds.
 */
public final class WorldRegistry {

    private final Map<UUID, LavaWorldWrapper> byUid = new ConcurrentHashMap<>();
    private final Map<String, UUID> nameIndex = new ConcurrentHashMap<>();

    public LavaWorldWrapper get(Level level) {
        if (level == null) return null;
        LavaWorldWrapper w = LavaWorldWrapper.of(level);
        UUID uid = w.getUID();
        if (!byUid.containsKey(uid)) {
            byUid.put(uid, w);
            nameIndex.put(w.getName().toLowerCase(), uid);
            LavaMetrics.recordCacheMiss("world");
        } else {
            LavaMetrics.recordCacheHit("world");
        }
        return w;
    }

    public World get(UUID uid) {
        if (uid == null) return null;
        LavaWorldWrapper w = byUid.get(uid);
        if (w != null) {
            LavaMetrics.recordCacheHit("world");
            return w;
        }
        LavaMetrics.recordCacheMiss("world");
        return null;
    }

    public World get(String name) {
        if (name == null) return null;
        UUID uid = nameIndex.get(name.toLowerCase());
        return uid != null ? get(uid) : null;
    }

    public void register(LavaWorldWrapper world) {
        if (world == null) return;
        byUid.put(world.getUID(), world);
        nameIndex.put(world.getName().toLowerCase(), world.getUID());
    }

    public void unregister(UUID uid) {
        if (uid == null) return;
        LavaWorldWrapper removed = byUid.remove(uid);
        if (removed != null) {
            nameIndex.remove(removed.getName().toLowerCase());
        }
    }

    public Collection<World> all() {
        return Collections.unmodifiableCollection(byUid.values());
    }

    public List<World> snapshot() {
        return new ArrayList<>(byUid.values());
    }

    public int size() {
        return byUid.size();
    }

    public void clear() {
        byUid.clear();
        nameIndex.clear();
    }

    /** Sync from a live MinecraftServer's levels. */
    public void syncFrom(Iterable<ServerLevel> levels) {
        for (ServerLevel level : levels) {
            get(level);
        }
    }
}
