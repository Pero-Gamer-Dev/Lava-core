package io.github.lavacore.bridge;

import org.bukkit.Server;

public class LavaServer {
    private Server server;

    public LavaServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
}
