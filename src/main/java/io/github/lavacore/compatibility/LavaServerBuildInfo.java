/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.compatibility;

import io.papermc.paper.ServerBuildInfo;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

public final class LavaServerBuildInfo implements ServerBuildInfo {
    public static final LavaServerBuildInfo INSTANCE = new LavaServerBuildInfo();
    private static final Key BRAND = Key.key("lavacore", "lava");

    private LavaServerBuildInfo() {}

    @Override public Key brandId() { return BRAND; }
    @Override public boolean isBrandCompatible(Key brandId) {
        return brandId != null && (brandId.equals(BRAND) || brandId.equals(ServerBuildInfo.BRAND_PAPER_ID));
    }
    @Override public String brandName() { return "Lava"; }
    @Override public String minecraftVersionId() { return "1.21.4"; }
    @Override public String minecraftVersionName() { return "1.21.4"; }
    @Override public OptionalInt buildNumber() { return OptionalInt.of(105); }
    @Override public Instant buildTime() { return Instant.parse("2026-10-03T00:00:00Z"); }
    @Override public Optional<String> gitBranch() { return Optional.of("main"); }
    @Override public Optional<String> gitCommit() { return Optional.of("1.0.7-BETA"); }
    @Override public String asString(StringRepresentation representation) {
        if (representation == StringRepresentation.VERSION_SIMPLE) return "1.21.4-Lava-1.0.7-BETA";
        return "1.21.4-R0.1-SNAPSHOT (Lava-core 1.0.7-BETA)";
    }

    /**
     * Bind Bukkit singleton without touching ServerBuildInfo Holder
     * (Holder's static init uses ServiceLoader and can throw on Fabric).
     */
    public static void installAndSetServer(Server server) {
        try {
            Field f = Bukkit.class.getDeclaredField("server");
            f.setAccessible(true);
            Object existing = f.get(null);
            if (existing != null && existing != server) {
                throw new UnsupportedOperationException("Cannot redefine singleton Server");
            }
            if (existing == null) {
                f.set(null, server);
            }
            server.getLogger().info("This server is running " + server.getName()
                    + " version " + server.getVersion()
                    + " (Bukkit: " + server.getBukkitVersion() + ")");
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Throwable t) {
            // last resort: try official setServer (may fail on buildInfo)
            try {
                Bukkit.setServer(server);
            } catch (Throwable t2) {
                throw new RuntimeException("Failed to bind Bukkit server singleton", t2);
            }
        }
    }
}
