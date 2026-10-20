/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.plugin;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.loader.LavaClassRemapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LavaPluginClassLoader extends URLClassLoader implements ConfiguredPluginClassLoader {
    static { ClassLoader.registerAsParallelCapable(); }

    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private JavaPlugin plugin;

    public LavaPluginClassLoader(ClassLoader parent, PluginDescriptionFile description, File dataFolder, File file)
            throws IOException, InvalidPluginException {
        super(file.getName(), new URL[]{file.toURI().toURL()}, parent);
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        LavaPluginClassLoaderGroup.GLOBAL.add(this);

        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            Logger.getLogger("Lava").warning("Could not create data folder: " + dataFolder);
        }

        try {
            Class<?> jarClass = Class.forName(description.getMain(), true, this);
            Class<? extends JavaPlugin> pluginClass = jarClass.asSubclass(JavaPlugin.class);
            JavaPlugin instance = pluginClass.getDeclaredConstructor().newInstance();
            // CRITICAL: Paper plugins require init before onLoad/onEnable
            this.init(instance);
        } catch (ClassNotFoundException e) {
            throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", e);
        } catch (ClassCastException e) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' must extend JavaPlugin", e);
        } catch (Exception e) {
            throw new InvalidPluginException("Exception initializing " + description.getMain(), e);
        }
    }

    @Override
    public PluginMeta getConfiguration() {
        return description;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve, boolean checkGlobal, boolean checkLibraries)
            throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException ex) {
            if (checkGlobal) {
                Class<?> found = LavaPluginClassLoaderGroup.GLOBAL.getClassByName(name, resolve, this);
                if (found != null) return found;
            }
            throw ex;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/') + ".class";
        JarEntry entry = jar.getJarEntry(path);
        if (entry == null) {
            return super.findClass(name);
        }
        try (InputStream in = jar.getInputStream(entry)) {
            byte[] bytes = LavaClassRemapper.remap(in.readAllBytes());
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    @Override
    public void init(JavaPlugin plugin) {
        Logger pluginLogger = Logger.getLogger(description.getName());
        pluginLogger.setLevel(Level.ALL);
        plugin.init(
                LavaServer.getInstance(),
                description,
                dataFolder,
                file,
                this,
                description,
                pluginLogger
        );
        this.plugin = plugin;
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public PluginClassLoaderGroup getGroup() {
        return LavaPluginClassLoaderGroup.GLOBAL;
    }

    @Override
    public void close() throws IOException {
        LavaPluginClassLoaderGroup.GLOBAL.remove(this);
        try {
            super.close();
        } finally {
            jar.close();
        }
    }
}
