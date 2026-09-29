package io.github.lavacore;

import org.bukkit.plugin.java.JavaPlugin;

public class LavaCoreInitializer extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("LavaCore initialized!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LavaCore disabled!");
    }
}
