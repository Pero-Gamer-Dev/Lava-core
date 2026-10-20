/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.plugin.Plugin;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.command.LavaBuiltinCommands;
import io.github.lavacore.diagnostic.LavaStartupReport;
import io.github.lavacore.events.LavaBlockBridge;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Fabric server entrypoint. Does not require Fabric API.
 * Bukkit binding runs from DedicatedServerMixin after levels load.
 */
public class LavaCoreInitializer implements DedicatedServerModInitializer {
    private static final AtomicBoolean BOUND = new AtomicBoolean(false);

    @Override
    public void onInitializeServer() {
        System.out.println("[Lava] 1.0.7-BETA bootstrapping (MC 1.21.4 plugin bridge)...");
        File pluginFolder = new File("plugins");
        if (!pluginFolder.exists() && !pluginFolder.mkdirs()) {
            System.err.println("[Lava] Could not create plugins/ directory");
        }
    }

    public static void initializeLavaEngine(DedicatedServer dedicatedServerInstance) {
        if (!BOUND.compareAndSet(false, true)) {
            return;
        }
        try {
            System.out.println("[Lava] Binding Bukkit API to Fabric context...");
            LavaServer server = LavaServer.bind(dedicatedServerInstance);

            LavaBuiltinCommands.register(server);
            LavaBlockBridge.registerEventBuses();

            File pluginFolder = server.getPluginsFolder();
            if (!pluginFolder.exists()) {
                pluginFolder.mkdirs();
            }

            File[] jars = pluginFolder.listFiles((d, n) -> n != null && n.endsWith(".jar"));
            System.out.println("[Lava] Scanning plugins/ (" + (jars == null ? 0 : jars.length) + " jar(s))...");

            Plugin[] loaded = server.getPluginManager().loadPlugins(pluginFolder);
            for (Plugin plugin : loaded) {
                try {
                    server.getPluginManager().enablePlugin(plugin);
                } catch (Exception e) {
                    System.err.println("[Lava] Failed to enable plugin " + plugin.getName());
                    e.printStackTrace();
                }
            }
            LavaStartupReport.print(server, loaded);
            for (Plugin plugin : loaded) {
                System.out.println("[Lava]  - " + plugin.getName() + " v"
                        + plugin.getDescription().getVersion()
                        + (plugin.isEnabled() ? " [ENABLED]" : " [DISABLED]"));
            }
        } catch (Exception ex) {
            BOUND.set(false);
            System.err.println("[Lava] Failed to bind Bukkit singleton!");
            ex.printStackTrace();
        }
    }
}
