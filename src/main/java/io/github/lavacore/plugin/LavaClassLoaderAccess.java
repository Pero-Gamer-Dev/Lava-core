package io.github.lavacore.plugin;

public class LavaClassLoaderAccess {
    private final ClassLoader classLoader;

    public LavaClassLoaderAccess(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
