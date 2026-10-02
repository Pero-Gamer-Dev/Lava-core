package io.github.lavacore.bridge.entity;

import org.bukkit.entity.Player;

public class LavaPlayerWrapper {
    private final Player player;

    public LavaPlayerWrapper(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
