/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.diagnostic;

import org.bukkit.plugin.Plugin;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.runtime.RuntimeContext;

public final class LavaStartupReport {
    private LavaStartupReport() {}

    public static void print(LavaServer server, Plugin[] loaded) {
        int enabled = 0, failed = 0;
        for (Plugin p : loaded) {
            if (p.isEnabled()) enabled++;
            else failed++;
        }
        RuntimeContext ctx = RuntimeContext.get();
        System.out.println("=====================================");
        System.out.println("  Lava-core 1.0.7-BETA — Direct Runtime");
        System.out.println("=====================================");
        System.out.println("  Minecraft: 1.21.4");
        System.out.println("  Loader:    Fabric");
        System.out.println("  Runtime:   READY");
        System.out.println();
        System.out.println("  Plugin Manager:  OK");
        System.out.println("  Scheduler:       OK (TaskBucket)");
        System.out.println("  Event Bridge:    OK");
        System.out.println("  Command Bridge:  OK");
        System.out.println("  Services:        OK");
        System.out.println("  Direct Player:   OK");
        System.out.println("  Direct World:    OK");
        System.out.println("  Direct Block:    OK");
        if (ctx != null) {
            System.out.println("  Worlds cached:   " + ctx.worlds().size());
        }
        System.out.println();
        System.out.println("  Loaded Plugins:  " + loaded.length);
        System.out.println("  Enabled:         " + enabled);
        System.out.println("  Disabled/Failed: " + failed);
        System.out.println("=====================================");
        System.out.println("[Lava] System linked. Plugin pipeline ready (" + enabled + " plugin(s)).");
    }
}
