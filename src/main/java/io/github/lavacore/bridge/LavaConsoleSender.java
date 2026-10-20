/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.bridge;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

/**
 * Console sender backed by the Lava server logger.
 */
public class LavaConsoleSender implements ConsoleCommandSender {

    private final LavaServer server;

    public LavaConsoleSender(LavaServer server) {
        this.server = server;
    }

    @Override
    public void sendMessage(String message) {
        server.getLogger().info(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        if (messages == null) {
            return;
        }
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public void sendMessage(UUID sender, String message) {
        sendMessage(message);
    }

    @Override
    public void sendMessage(UUID sender, String[] messages) {
        sendMessage(messages);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public Component name() {
        return Component.text("CONSOLE");
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        // console is always op
    }

    @Override
    public boolean isPermissionSet(String name) {
        return true;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return true;
    }

    @Override
    public boolean hasPermission(String name) {
        return true;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return true;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
    }

    @Override
    public void recalculatePermissions() {
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return Set.of();
    }

    @Override
    public boolean isConversing() {
        return false;
    }

    @Override
    public void acceptConversationInput(String input) {
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return false;
    }

    @Override
    public void abandonConversation(Conversation conversation) {
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
    }

    @Override
    public void sendRawMessage(String message) {
        sendMessage(message);
    }

    @Override
    public void sendRawMessage(UUID sender, String message) {
        sendMessage(message);
    }

    @Override
    public void sendMessage(Identity source, Component message, MessageType type) {
        sendMessage(net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(message));
    }

    @Override
    public org.bukkit.command.CommandSender.Spigot spigot() {
    return null;
    }

}
