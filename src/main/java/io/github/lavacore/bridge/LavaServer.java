package io.github.lavacore.bridge;

import org.bukkit.Server;

public class LavaServer {
    private final Server server;

    public LavaServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
}
