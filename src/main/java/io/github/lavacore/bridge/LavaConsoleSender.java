package io.github.lavacore.bridge;

import org.bukkit.command.ConsoleCommandSender;

public class LavaConsoleSender {
    private final ConsoleCommandSender sender;

    public LavaConsoleSender(ConsoleCommandSender sender) {
        this.sender = sender;
    }

    public ConsoleCommandSender getSender() {
        return sender;
    }
}
