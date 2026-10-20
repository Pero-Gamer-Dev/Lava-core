/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.runtime;

import io.github.lavacore.bridge.LavaServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

/**
 * Guards world/entity/inventory mutations to the server main thread.
 */
public final class ThreadGuard {
    private ThreadGuard() {}

    public static void requireMainThread() {
        if (!isMainThread()) {
            throw new IllegalStateException(
                    "[Lava] Operation requires the main server thread (got " + Thread.currentThread().getName() + ")");
        }
    }

    public static boolean isMainThread() {
        try {
            LavaServer lava = LavaServer.getInstance();
            if (lava != null) {
                DedicatedServer ds = lava.getHandle();
                if (ds != null) {
                    return ds.isSameThread();
                }
            }
        } catch (Throwable ignored) {}
        // Fallback: assume OK if server not bound yet
        return true;
    }
}
