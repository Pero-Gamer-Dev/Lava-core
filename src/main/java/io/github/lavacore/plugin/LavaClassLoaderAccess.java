package io.github.lavacore.plugin;

public class LavaClassLoaderAccess {
    public ClassLoader getClassLoader() {
        return LavaClassLoaderAccess.class.getClassLoader();
    }
}
