/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.command.defaults.VersionCommand;
import org.bukkit.plugin.Plugin;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.performance.LavaMetrics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public final class LavaBuiltinCommands {
    private LavaBuiltinCommands() {}

    public static void register(LavaServer server) {
        SimpleCommandMap map = (SimpleCommandMap) server.getCommandMap();
        try {
            map.setFallbackCommands();
        } catch (Throwable t) {
            server.getLogger().warning("[Lava] setFallbackCommands failed: " + t.getMessage());
        }
        try {
            map.register("bukkit", new PluginsCommand("plugins"));
            map.register("bukkit", new VersionCommand("version"));
            map.register("bukkit", new ReloadCommand("reload"));
        } catch (Throwable t) {
            server.getLogger().warning("[Lava] Paper default commands failed, using fallbacks: " + t.getMessage());
            map.register("bukkit", pluginsFallback());
            map.register("bukkit", versionFallback(server));
        }
        map.register("lava", lavaCommand(server));
        server.getLogger().info("[Lava] Built-in commands: /plugins /version /reload /help /lava [performance|runtime|compatibility|plugins|version]");
    }

    private static Command pluginsFallback() {
        return new Command("plugins", "List loaded Bukkit plugins", "/plugins", Arrays.asList("pl")) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
                StringBuilder sb = new StringBuilder();
                sb.append(ChatColor.WHITE).append("Plugins (").append(plugins.length).append("): ");
                for (int i = 0; i < plugins.length; i++) {
                    Plugin p = plugins[i];
                    sb.append(p.isEnabled() ? ChatColor.GREEN : ChatColor.RED).append(p.getName());
                    if (i + 1 < plugins.length) sb.append(ChatColor.WHITE).append(", ");
                }
                sender.sendMessage(sb.toString());
                return true;
            }
            @Override
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                return Collections.emptyList();
            }
        };
    }

    private static Command versionFallback(LavaServer server) {
        return new Command("version", "Show server version", "/version [plugin]", Arrays.asList("ver", "about")) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GREEN + "This server is running " + server.getName()
                            + " version " + server.getVersion());
                    sender.sendMessage(ChatColor.GREEN + "Bukkit API: " + server.getBukkitVersion());
                    return true;
                }
                Plugin p = Bukkit.getPluginManager().getPlugin(args[0]);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + "Plugin not found: " + args[0]);
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + p.getName() + " v" + p.getDescription().getVersion());
                return true;
            }
        };
    }

    private static Command lavaCommand(LavaServer server) {
        return new Command("lava", "Lava-core status and diagnostics", "/lava [performance|runtime|compatibility|plugins|version]", List.of("lavacore")) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                if (args.length > 0) {
                    String sub = args[0].toLowerCase();
                    if (sub.equals("performance") || sub.equals("perf") || sub.equals("diagnostics")) {
                        LavaMetrics.report(sender);
                        return true;
                    }
                    if (sub.equals("runtime") || sub.equals("rt")) {
                        LavaMetrics.reportRuntime(sender);
                        return true;
                    }
                    if (sub.equals("compatibility") || sub.equals("compat") || sub.equals("features")) {
                        LavaMetrics.reportCompatibility(sender);
                        return true;
                    }
                    if (sub.equals("plugins") || sub.equals("pl")) {
                        return pluginsFallback().execute(sender, "plugins", new String[0]);
                    }
                    if (sub.equals("version") || sub.equals("ver")) {
                        sender.sendMessage(ChatColor.GOLD + "Lava-core " + server.getVersion());
                        sender.sendMessage(ChatColor.YELLOW + "Bukkit: " + server.getBukkitVersion());
                        sender.sendMessage(ChatColor.GRAY + "Codename: Direct Runtime");
                        return true;
                    }
                }
                sender.sendMessage(ChatColor.GOLD + "Lava-core " + server.getVersion() + " — Direct Runtime");
                sender.sendMessage(ChatColor.YELLOW + "Plugins: " + Bukkit.getPluginManager().getPlugins().length
                        + " | Online: " + server.getOnlinePlayers().size() + "/" + server.getMaxPlayers());
                sender.sendMessage(ChatColor.GRAY + "/lava performance     — direct vs reflection metrics");
                sender.sendMessage(ChatColor.GRAY + "/lava runtime         — registries & capabilities");
                sender.sendMessage(ChatColor.GRAY + "/lava compatibility   — feature matrix");
                sender.sendMessage(ChatColor.GRAY + "/lava plugins         — list Bukkit plugins");
                sender.sendMessage(ChatColor.GRAY + "/lava version         — version info");
                return true;
            }
            @Override
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                if (args.length == 1) {
                    return List.of("performance", "runtime", "compatibility", "plugins", "version");
                }
                return Collections.emptyList();
            }
        };
    }
}
