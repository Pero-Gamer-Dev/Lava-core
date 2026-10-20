/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
// ============================================================================
// ============================================================================
package io.github.lavacore.bridge.world;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Implemented on ServerLevel via ServerLevelWorldMixin. Lets LavaWorldWrapper
 * cache itself directly on the live world object instead of in a global
 * hashmap — see LavaWorldWrapper.of() for why that's a meaningful win here.
 */
public interface LavaWorldHolder {
    LavaWorldWrapper lava$getBukkitWorld();

    void lava$setBukkitWorld(LavaWorldWrapper wrapper);
}
