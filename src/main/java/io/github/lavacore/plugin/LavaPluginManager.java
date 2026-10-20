/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.plugin;

import java.util.Optional;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.java.JavaPluginLoader;
// LavaJavaPluginLoader used for load/enable
import io.github.lavacore.bridge.LavaServer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Thin PluginManager that can dispatch events and load Java plugins from
 * the plugins/ folder using Paper's API-side JavaPluginLoader.
 */
public class LavaPluginManager implements PluginManager {

    private final LavaServer server;
    private final LavaJavaPluginLoader javaLoader;
    private final Map<String, Plugin> plugins = new LinkedHashMap<>();

    public LavaPluginManager(LavaServer server) {
        this.server = server;
        this.javaLoader = new LavaJavaPluginLoader(server);
    }

    @Override
    public void callEvent(Event event) {
        if (event == null) {
            return;
        }
        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();
        for (RegisteredListener registration : listeners) {
            if (!registration.getPlugin().isEnabled()) {
                continue;
            }
            try {
                registration.callEvent(event);
            } catch (Throwable t) {
                server.getLogger().log(Level.SEVERE,
                        "Could not pass event " + event.getEventName()
                                + " to " + registration.getPlugin().getDescription().getFullName(), t);
            }
        }
    }

    @Override
    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
        }
        for (Map.Entry<Class<? extends Event>, java.util.Set<RegisteredListener>> entry
                : javaLoader.createRegisteredListeners(listener, plugin).entrySet()) {
            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
        }
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority,
                              EventExecutor executor, Plugin plugin) {
        registerEvent(event, listener, priority, executor, plugin, false);
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority,
                              EventExecutor executor, Plugin plugin, boolean ignoreCancelled) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");
        }
        getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
    }

    private static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            java.lang.reflect.Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            }
            throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
        }
    }

    @Override
    public Set<org.bukkit.permissions.Permission> getPermissions() {
        return Set.of();
    }

    @Override
    public void removePermission(String name) {}
    @Override
    public void removePermission(org.bukkit.permissions.Permission perm) {}
    @Override
    public org.bukkit.permissions.Permission getPermission(String name) { return null; }
    @Override
    public void addPermission(org.bukkit.permissions.Permission perm) {}

    @Override
    public Plugin getPlugin(String name) {
        return plugins.get(name == null ? null : name.toLowerCase());
    }

    @Override
    public Plugin[] getPlugins() {
        return plugins.values().toArray(Plugin[]::new);
    }

    @Override
    public boolean isPluginEnabled(String name) {
        Plugin plugin = getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public boolean isPluginEnabled(Plugin plugin) {
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public Plugin[] loadPlugins(File directory) {
        if (directory == null || !directory.isDirectory()) {
            return new Plugin[0];
        }
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".jar"));
        return loadPlugins(files == null ? new File[0] : files);
    }

    @Override
    public Plugin[] loadPlugins(File[] files) {
        List<Plugin> loaded = new ArrayList<>();
        if (files == null) {
            return new Plugin[0];
        }
        // Phase 1: read descriptions
        List<LavaDependencyResolver.Candidate> candidates = new ArrayList<>();
        for (File file : files) {
            try {
                org.bukkit.plugin.PluginDescriptionFile desc = javaLoader.getPluginDescription(file);
                candidates.add(new LavaDependencyResolver.Candidate(file, desc));
            } catch (Exception e) {
                server.getLogger().log(Level.SEVERE, "Could not read plugin.yml for " + file.getName(), e);
            }
        }
        // Phase 2: dependency order
        List<LavaDependencyResolver.Candidate> ordered =
                new LavaDependencyResolver(server.getLogger()).resolve(candidates);
        // Phase 3: load in order
        for (LavaDependencyResolver.Candidate c : ordered) {
            try {
                Plugin plugin = loadPlugin(c.file());
                if (plugin != null) {
                    loaded.add(plugin);
                    server.getLogger().info("[Lava] Loaded " + plugin.getDescription().getFullName());
                }
            } catch (Exception e) {
                server.getLogger().log(Level.SEVERE, "Could not load plugin " + c.file().getName(), e);
            }
        }
        return loaded.toArray(Plugin[]::new);
    }

    @Override
    public Plugin loadPlugin(File file) throws org.bukkit.plugin.InvalidPluginException, org.bukkit.plugin.InvalidDescriptionException, org.bukkit.plugin.UnknownDependencyException {
        Plugin plugin = javaLoader.loadPlugin(file);
        plugins.put(plugin.getName().toLowerCase(), plugin);
        try {
            java.util.List<Command> commands = PluginCommandYamlParser.parse(plugin);
            server.getCommandMap().registerAll(plugin.getDescription().getName(), commands);
        } catch (Exception e) {
            server.getLogger().log(Level.WARNING, "Could not register commands for " + plugin.getName(), e);
        }
        try { plugin.onLoad(); } catch (Throwable t) {
            server.getLogger().log(Level.SEVERE, "onLoad failed for " + plugin.getName(), t);
        }
        return plugin;
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        if (plugin == null || plugin.isEnabled()) {
            return;
        }
        try {
            javaLoader.enablePlugin(plugin);
            callEvent(new org.bukkit.event.server.PluginEnableEvent(plugin));
            server.getLogger().info("[Lava] Enabled " + plugin.getDescription().getFullName());
        } catch (Throwable t) {
            server.getLogger().log(Level.SEVERE, "[Lava] Failed enabling " + plugin.getName(), t);
            try { javaLoader.disablePlugin(plugin); } catch (Throwable ignored) {}
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        if (plugin != null && plugin.isEnabled()) {
            javaLoader.disablePlugin(plugin);
        }
    }

    @Override
    public void disablePlugins() {
        for (Plugin plugin : getPlugins()) {
            disablePlugin(plugin);
        }
    }

    @Override
    public void clearPlugins() {
        disablePlugins();
        plugins.clear();
        HandlerList.unregisterAll();
    }

    @Override
    public void registerInterface(java.lang.Class<? extends org.bukkit.plugin.PluginLoader> arg0) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public java.util.Set<org.bukkit.permissions.Permission> getDefaultPermissions(boolean arg0) {
    return Set.of();
    }

    @Override
    public void recalculatePermissionDefaults(org.bukkit.permissions.Permission arg0) {
    // no-op stub
    }

    @Override
    public void subscribeToPermission(java.lang.String arg0, org.bukkit.permissions.Permissible arg1) {
    // no-op stub
    }

    @Override
    public void unsubscribeFromPermission(java.lang.String arg0, org.bukkit.permissions.Permissible arg1) {
    // no-op stub
    }

    @Override
    public java.util.Set<org.bukkit.permissions.Permissible> getPermissionSubscriptions(java.lang.String arg0) {
    return Set.of();
    }

    @Override
    public void subscribeToDefaultPerms(boolean arg0, org.bukkit.permissions.Permissible arg1) {
    // no-op stub
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean arg0, org.bukkit.permissions.Permissible arg1) {
    // no-op stub
    }

    @Override
    public java.util.Set<org.bukkit.permissions.Permissible> getDefaultPermSubscriptions(boolean arg0) {
    return Set.of();
    }

    @Override
    public boolean useTimings() {
    return false;
    }

    @Override
    public boolean isTransitiveDependency(io.papermc.paper.plugin.configuration.PluginMeta arg0, io.papermc.paper.plugin.configuration.PluginMeta arg1) {
    return false;
    }

    @Override
    public void overridePermissionManager(org.bukkit.plugin.Plugin arg0, io.papermc.paper.plugin.PermissionManager arg1) {
    // no-op stub
    }

    @Override
    public void addPermissions(java.util.List<org.bukkit.permissions.Permission> arg0) {
    // no-op stub
    }

    @Override
    public void clearPermissions() {
    // no-op stub
    }

}
