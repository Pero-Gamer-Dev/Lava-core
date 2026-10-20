/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.mixin;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.bridge.entity.LavaPlayerWrapper;
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {
    @Shadow public abstract net.minecraft.server.level.ServerPlayer getPlayer();
    @Inject(method="performUnsignedChatCommand", at=@At("HEAD"), cancellable=true)
    private void lava$bukkitCommand(String command, CallbackInfo ci) {
        LavaServer server = LavaServer.getInstance();
        if (server==null || command==null) return;
        LavaPlayerWrapper player = (LavaPlayerWrapper) server.getPlayer(getPlayer().getUUID());
        if (player==null) return;
        String raw = command.startsWith("/") ? command : "/"+command;
        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, raw);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) { ci.cancel(); return; }
        String line = event.getMessage().startsWith("/") ? event.getMessage().substring(1) : event.getMessage();
        try { if (server.getCommandMap().dispatch(player, line)) ci.cancel(); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
