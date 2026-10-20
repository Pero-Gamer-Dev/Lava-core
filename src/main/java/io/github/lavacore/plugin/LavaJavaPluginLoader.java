/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.plugin;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import io.github.lavacore.bridge.LavaServer;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public final class LavaJavaPluginLoader implements PluginLoader {
    private final Server server;
    private final JavaPluginLoader listenerFactory;

    public LavaJavaPluginLoader() {
        this.server = LavaServer.getInstance();
        this.listenerFactory = server == null ? null : new JavaPluginLoader(server);
    }

    public LavaJavaPluginLoader(Server server) {
        this.server = server;
        this.listenerFactory = new JavaPluginLoader(server);
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        if (file == null || !file.exists()) {
            throw new InvalidPluginException("Plugin file does not exist: " + file);
        }
        PluginDescriptionFile description;
        try {
            description = getPluginDescription(file);
        } catch (InvalidDescriptionException ex) {
            throw new InvalidPluginException(ex);
        }
        File dataFolder = new File(file.getParentFile(), description.getName());
        try {
            LavaPluginClassLoader loader = new LavaPluginClassLoader(
                    JavaPlugin.class.getClassLoader(), description, dataFolder, file);
            JavaPlugin plugin = loader.getPlugin();
            if (plugin == null) {
                throw new InvalidPluginException("Plugin instance is null after load: " + description.getName());
            }
            return plugin;
        } catch (InvalidPluginException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPluginException(e);
        }
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        try (JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("plugin.yml");
            if (entry == null) {
                entry = jar.getJarEntry("paper-plugin.yml");
            }
            if (entry == null) {
                throw new InvalidDescriptionException("No plugin.yml in " + file.getName());
            }
            try (InputStream in = jar.getInputStream(entry)) {
                return new PluginDescriptionFile(in);
            }
        } catch (InvalidDescriptionException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    @Override
    public Pattern[] getPluginFileFilters() {
        return new Pattern[]{Pattern.compile("\\.jar$")};
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
        return listenerFactory.createRegisteredListeners(listener, plugin);
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        if (!(plugin instanceof JavaPlugin jp)) {
            return;
        }
        if (plugin.isEnabled()) {
            return;
        }
        try {
            plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());
            jp.setEnabled(true);
        } catch (Throwable t) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE,
                    "Error enabling " + plugin.getName(), t);
            throw t instanceof RuntimeException re ? re : new RuntimeException(t);
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        if (!(plugin instanceof JavaPlugin jp)) {
            return;
        }
        if (!plugin.isEnabled()) {
            return;
        }
        try {
            plugin.getLogger().info("Disabling " + plugin.getDescription().getFullName());
            jp.setEnabled(false);
        } catch (Throwable t) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE,
                    "Error disabling " + plugin.getName(), t);
        }
    }
}
