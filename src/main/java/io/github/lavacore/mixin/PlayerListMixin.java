/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.bridge.entity.LavaPlayerWrapper;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void lava$onJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        LavaServer server = LavaServer.getInstance();
        if (server == null) return;
        server.addPlayer(player);
        LavaPlayerWrapper bukkit = (LavaPlayerWrapper) server.getPlayer(player.getUUID());
        if (bukkit != null) {
            server.getPluginManager().callEvent(new PlayerJoinEvent(bukkit, bukkit.getName() + " joined the game"));
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void lava$onQuit(ServerPlayer player, CallbackInfo ci) {
        LavaServer server = LavaServer.getInstance();
        if (server == null) return;
        LavaPlayerWrapper bukkit = (LavaPlayerWrapper) server.getPlayer(player.getUUID());
        if (bukkit != null) {
            server.getPluginManager().callEvent(new PlayerQuitEvent(bukkit, bukkit.getName() + " left the game"));
        }
        server.removePlayer(player.getUUID());
    }
}
