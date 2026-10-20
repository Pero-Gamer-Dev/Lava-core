/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.plugin;
import io.papermc.paper.plugin.provider.classloader.ClassLoaderAccess;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
public final class LavaClassLoaderAccess implements ClassLoaderAccess {
    public static final LavaClassLoaderAccess INSTANCE = new LavaClassLoaderAccess();
    private LavaClassLoaderAccess() {}
    @Override public boolean canAccess(ConfiguredPluginClassLoader classLoader) { return true; }
}
