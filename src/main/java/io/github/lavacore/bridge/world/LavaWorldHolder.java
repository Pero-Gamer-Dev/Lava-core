package io.github.lavacore.bridge.world;

import org.bukkit.World;

public class LavaWorldHolder {
    private World world;

    public LavaWorldHolder(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
