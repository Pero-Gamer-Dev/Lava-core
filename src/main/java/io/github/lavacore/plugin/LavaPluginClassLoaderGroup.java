/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.plugin;
import io.papermc.paper.plugin.provider.classloader.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
public final class LavaPluginClassLoaderGroup implements PluginClassLoaderGroup {
    public static final LavaPluginClassLoaderGroup GLOBAL = new LavaPluginClassLoaderGroup();
    private final List<ConfiguredPluginClassLoader> loaders = new CopyOnWriteArrayList<>();
    @Override public Class<?> getClassByName(String name, boolean resolve, ConfiguredPluginClassLoader requester) {
        for (ConfiguredPluginClassLoader loader : loaders) {
            if (loader == requester) continue;
            try { return loader.loadClass(name, resolve, false, false); } catch (ClassNotFoundException ignored) {}
        }
        return null;
    }
    @Override public void remove(ConfiguredPluginClassLoader c) { loaders.remove(c); }
    @Override public void add(ConfiguredPluginClassLoader c) { if (!loaders.contains(c)) loaders.add(c); }
    @Override public ClassLoaderAccess getAccess() { return LavaClassLoaderAccess.INSTANCE; }
}
