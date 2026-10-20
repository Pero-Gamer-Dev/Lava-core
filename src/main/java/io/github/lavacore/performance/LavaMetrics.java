/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.performance;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.runtime.RuntimeContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Performance metrics 2.0 — tracks direct vs reflective operations and cache hits.
 */
public final class LavaMetrics {
    private static final AtomicLong PLAYER_LOOKUPS = new AtomicLong();
    private static final AtomicLong WORLD_LOOKUPS = new AtomicLong();
    private static final AtomicLong BLOCK_LOOKUPS = new AtomicLong();
    private static final AtomicLong WRAPPER_CREATES = new AtomicLong();
    private static final AtomicLong DIRECT_OPS = new AtomicLong();
    private static final AtomicLong REFLECTION_OPS = new AtomicLong();
    private static final AtomicLong CACHE_HITS = new AtomicLong();
    private static final AtomicLong CACHE_MISSES = new AtomicLong();
    private static final AtomicLong EVENT_DISPATCHES = new AtomicLong();
    private static final AtomicLong SCHEDULER_EXECS = new AtomicLong();

    private static final Map<String, AtomicLong> DIRECT_BY_KEY = new ConcurrentHashMap<>();
    private static final Map<String, AtomicLong> REFLECT_BY_KEY = new ConcurrentHashMap<>();
    private static final Map<String, AtomicLong> CACHE_HIT_BY = new ConcurrentHashMap<>();
    private static final Map<String, AtomicLong> CACHE_MISS_BY = new ConcurrentHashMap<>();

    private static final long START = System.currentTimeMillis();

    private LavaMetrics() {}

    public static void playerLookup() { PLAYER_LOOKUPS.incrementAndGet(); }
    public static void worldLookup() { WORLD_LOOKUPS.incrementAndGet(); }
    public static void blockLookup() { BLOCK_LOOKUPS.incrementAndGet(); }
    public static void wrapperCreate() { WRAPPER_CREATES.incrementAndGet(); }

    public static void recordDirect(String key) {
        DIRECT_OPS.incrementAndGet();
        DIRECT_BY_KEY.computeIfAbsent(key, k -> new AtomicLong()).incrementAndGet();
    }

    public static void recordReflection(String key) {
        REFLECTION_OPS.incrementAndGet();
        REFLECT_BY_KEY.computeIfAbsent(key, k -> new AtomicLong()).incrementAndGet();
    }

    public static void recordCacheHit(String kind) {
        CACHE_HITS.incrementAndGet();
        CACHE_HIT_BY.computeIfAbsent(kind, k -> new AtomicLong()).incrementAndGet();
    }

    public static void recordCacheMiss(String kind) {
        CACHE_MISSES.incrementAndGet();
        CACHE_MISS_BY.computeIfAbsent(kind, k -> new AtomicLong()).incrementAndGet();
    }

    public static void recordEventDispatch() { EVENT_DISPATCHES.incrementAndGet(); }
    public static void recordSchedulerExec() { SCHEDULER_EXECS.incrementAndGet(); }

    public static long directOps() { return DIRECT_OPS.get(); }
    public static long reflectionOps() { return REFLECTION_OPS.get(); }
    public static long hotPathReflection() { return REFLECTION_OPS.get(); }

    public static double directPercent() {
        long d = DIRECT_OPS.get();
        long r = REFLECTION_OPS.get();
        long total = d + r;
        if (total == 0) return 100.0;
        return (100.0 * d) / total;
    }

    public static void report(CommandSender sender) {
        LavaServer server = LavaServer.getInstance();
        RuntimeContext ctx = RuntimeContext.get();
        int players = server != null ? server.getOnlinePlayers().size() : 0;
        int worlds = server != null ? server.getWorlds().size() : 0;
        int plugins = Bukkit.getPluginManager() != null ? Bukkit.getPluginManager().getPlugins().length : 0;
        long uptime = (System.currentTimeMillis() - START) / 1000;
        double pct = directPercent();
        long d = DIRECT_OPS.get();
        long r = REFLECTION_OPS.get();

        sender.sendMessage("§6╔══════════════════════════════════╗");
        sender.sendMessage("§6║       LAVA PERFORMANCE 1.0.7     ║");
        sender.sendMessage("§6╠══════════════════════════════════╣");
        sender.sendMessage("§e║ Version:          §f1.0.7-BETA");
        sender.sendMessage("§e║ Uptime:           §f" + uptime + "s");
        sender.sendMessage("§e║ Players:          §f" + players);
        sender.sendMessage("§e║ Worlds:           §f" + worlds);
        sender.sendMessage("§e║ Plugins:          §f" + plugins);
        if (ctx != null) {
            sender.sendMessage("§e║ Cached players:   §f" + ctx.players().size());
            sender.sendMessage("§e║ Cached worlds:    §f" + ctx.worlds().size());
            sender.sendMessage("§e║ Cached entities:  §f" + ctx.entities().size());
        }
        sender.sendMessage("§6╠══════════════════════════════════╣");
        sender.sendMessage(String.format("§e║ Direct ops:       §a%.1f%% §7(%d)", pct, d));
        sender.sendMessage(String.format("§e║ Reflection fb:    §c%.1f%% §7(%d)", 100.0 - pct, r));
        sender.sendMessage("§e║ Hot-path reflect: §f" + r);
        sender.sendMessage("§e║ Cache hits:       §f" + CACHE_HITS.get());
        sender.sendMessage("§e║ Cache misses:     §f" + CACHE_MISSES.get());
        sender.sendMessage("§e║ Event dispatches: §f" + EVENT_DISPATCHES.get());
        sender.sendMessage("§e║ Scheduler execs:  §f" + SCHEDULER_EXECS.get());
        sender.sendMessage("§e║ MethodCache res:  §f" + MethodCache.resolves());
        sender.sendMessage("§e║ MethodCache hit:  §f" + MethodCache.hits());
        sender.sendMessage("§6╚══════════════════════════════════╝");

        if (!DIRECT_BY_KEY.isEmpty()) {
            sender.sendMessage("§7Direct by key:");
            DIRECT_BY_KEY.forEach((k, v) ->
                    sender.sendMessage("§7  " + k + ": §a" + v.get()
                            + " §7(refl " + REFLECT_BY_KEY.getOrDefault(k, new AtomicLong(0)).get() + ")"));
        }
    }

    public static void reportRuntime(CommandSender sender) {
        LavaServer server = LavaServer.getInstance();
        RuntimeContext ctx = RuntimeContext.get();
        sender.sendMessage("§6╔══════════════════════════════════╗");
        sender.sendMessage("§6║         LAVA RUNTIME             ║");
        sender.sendMessage("§6╠══════════════════════════════════╣");
        sender.sendMessage("§e║ Runtime:     §aENABLED");
        sender.sendMessage("§e║ Version:     §f1.0.7-BETA — Direct Runtime");
        sender.sendMessage("§e║ Minecraft:   §f1.21.4");
        if (server != null) {
            sender.sendMessage("§e║ Plugins:     §f" + Bukkit.getPluginManager().getPlugins().length);
            sender.sendMessage("§e║ Players:     §f" + server.getOnlinePlayers().size());
            sender.sendMessage("§e║ Worlds:      §f" + server.getWorlds().size());
        }
        if (ctx != null) {
            sender.sendMessage("§e║ Cached wrap: §f" + (ctx.players().size() + ctx.worlds().size() + ctx.entities().size()));
            if (ctx.scheduler() != null) {
                sender.sendMessage("§e║ Sched tasks: §f" + ctx.scheduler().getPendingTasks().size());
            }
            sender.sendMessage("§6╠══════════════════════════════════╣");
            sender.sendMessage("§e║ Capabilities:");
            for (String line : ctx.capabilities().summary().split("\n")) {
                sender.sendMessage("§7  " + line);
            }
        }
        sender.sendMessage("§6╚══════════════════════════════════╝");
    }

    public static void reportCompatibility(CommandSender sender) {
        RuntimeContext ctx = RuntimeContext.get();
        sender.sendMessage("§6╔══════════════════════════════════╗");
        sender.sendMessage("§6║      LAVA COMPATIBILITY          ║");
        sender.sendMessage("§6╠══════════════════════════════════╣");
        sender.sendMessage("§e Feature                  Status");
        sender.sendMessage("§7--------------------------------------");
        sender.sendMessage("§f Player API               §aSUPPORTED");
        sender.sendMessage("§f World API                §aSUPPORTED");
        sender.sendMessage("§f Block API                §aSUPPORTED");
        sender.sendMessage("§f Basic Entity API         §ePARTIAL");
        sender.sendMessage("§f Inventory                §ePARTIAL");
        sender.sendMessage("§f ItemStack                §ePARTIAL");
        sender.sendMessage("§f Events                   §ePARTIAL");
        sender.sendMessage("§f Commands                 §aSUPPORTED");
        sender.sendMessage("§f Scheduler                §aSUPPORTED");
        sender.sendMessage("§f Permissions              §ePARTIAL");
        sender.sendMessage("§f Paper API                §ePARTIAL");
        sender.sendMessage("§f Paper NMS                §cUNSUPPORTED");
        if (ctx != null) {
            sender.sendMessage("§6╠══════════════════════════════════╣");
            sender.sendMessage("§7" + ctx.capabilities().summary().replace("\n", "\n§7"));
        }
        sender.sendMessage("§6╚══════════════════════════════════╝");
    }
}
