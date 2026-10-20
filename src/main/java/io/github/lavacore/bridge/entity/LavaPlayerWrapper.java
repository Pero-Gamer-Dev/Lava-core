/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.bridge.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.bridge.world.LavaWorldWrapper;
import io.github.lavacore.ops.DirectPlayerOps;
import io.github.lavacore.runtime.RuntimeContext;
import org.bukkit.GameMode;
import org.bukkit.permissions.PermissibleBase;

import java.util.UUID;

/**
 * Adapts a connected ServerPlayer onto org.bukkit.entity.Player.
 * Only identity/world/location methods are implemented for the block-break
 * slice; the rest of the 600+ method surface is stubbed to compile.
 */
public class LavaPlayerWrapper implements Player {

    private final ServerPlayer handle;
    private final PermissibleBase perm = new PermissibleBase(this);

    public LavaPlayerWrapper(ServerPlayer handle) {
        this.handle = handle;
    }

    public ServerPlayer getHandle() {
        return handle;
    }

    @Override
    public String getName() {
        return handle.getGameProfile().getName();
    }

    @Override
    public UUID getUniqueId() {
        return handle.getUUID();
    }

    @Override
    public org.bukkit.World getWorld() {
        RuntimeContext ctx = RuntimeContext.get();
        if (ctx != null) {
            return ctx.worlds().get(handle.level());
        }
        return LavaWorldWrapper.of(handle.level());
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), handle.getX(), handle.getY(), handle.getZ(), handle.getYRot(), handle.getXRot());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(handle.getX());
            loc.setY(handle.getY());
            loc.setZ(handle.getZ());
            loc.setYaw(handle.getYRot());
            loc.setPitch(handle.getXRot());
        }
        return loc;
    }

    @Override
    public boolean isOnline() {
        return !handle.hasDisconnected();
    }

    @Override
    public org.bukkit.Server getServer() {
        return LavaServer.getInstance();
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public Component displayName() {
        return Component.text(getName());
    }

    @Override
    public Component name() {
        return Component.text(getName());
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public java.lang.Iterable<? extends net.kyori.adventure.bossbar.BossBar> activeBossBars() {
    return List.of();
    }

    @Override
    public void displayName(net.kyori.adventure.text.Component arg0) {
    // no-op stub
    }

    @Override
    public void setDisplayName(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void playerListName(net.kyori.adventure.text.Component arg0) {
    // no-op stub
    }

    @Override
    public net.kyori.adventure.text.Component playerListName() {
    return Component.empty();
    }

    @Override
    public net.kyori.adventure.text.Component playerListHeader() {
    return Component.empty();
    }

    @Override
    public net.kyori.adventure.text.Component playerListFooter() {
    return Component.empty();
    }

    @Override
    public java.lang.String getPlayerListName() {
    return "";
    }

    @Override
    public void setPlayerListName(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public java.lang.String getPlayerListHeader() {
    return "";
    }

    @Override
    public java.lang.String getPlayerListFooter() {
    return "";
    }

    @Override
    public void setPlayerListHeader(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void setPlayerListFooter(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void setPlayerListHeaderFooter(java.lang.String arg0, java.lang.String arg1) {
    // no-op stub
    }

    @Override
    public void setCompassTarget(org.bukkit.Location arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.Location getCompassTarget() {
    return null;
    }

    @Override
    public java.net.InetSocketAddress getAddress() {
    return null;
    }

    @Override
    public java.net.InetSocketAddress getHAProxyAddress() {
    return null;
    }

    @Override
    public boolean isTransferred() {
    return false;
    }

    @Override
    public java.util.concurrent.CompletableFuture<byte[]> retrieveCookie(org.bukkit.NamespacedKey arg0) {
    return CompletableFuture.completedFuture(null);
    }

    @Override
    public void storeCookie(org.bukkit.NamespacedKey arg0, byte[] arg1) {
    // no-op stub
    }

    @Override
    public void transfer(java.lang.String arg0, int arg1) {
    // no-op stub
    }

    @Override
    public void sendRawMessage(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void kickPlayer(java.lang.String arg0) {
        handle.connection.disconnect(net.minecraft.network.chat.Component.literal(arg0==null?"":arg0));
    }

    @Override
    public void kick() {
    // no-op stub
    }

    @Override
    public void kick(net.kyori.adventure.text.Component arg0) {
    // no-op stub
    }

    @Override
    public void kick(net.kyori.adventure.text.Component arg0, org.bukkit.event.player.PlayerKickEvent.Cause arg1) {
    // no-op stub
    }

    @Override
    public <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(java.lang.String arg0, java.util.Date arg1, java.lang.String arg2, boolean arg3) {
    return null;
    }

    @Override
    public <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(java.lang.String arg0, java.time.Instant arg1, java.lang.String arg2, boolean arg3) {
    return null;
    }

    @Override
    public <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(java.lang.String arg0, java.time.Duration arg1, java.lang.String arg2, boolean arg3) {
    return null;
    }

    @Override
    public org.bukkit.BanEntry<java.net.InetAddress> banIp(java.lang.String arg0, java.util.Date arg1, java.lang.String arg2, boolean arg3) {
    return null;
    }

    @Override
    public org.bukkit.BanEntry<java.net.InetAddress> banIp(java.lang.String arg0, java.time.Instant arg1, java.lang.String arg2, boolean arg3) {
    return null;
    }

    @Override
    public org.bukkit.BanEntry<java.net.InetAddress> banIp(java.lang.String arg0, java.time.Duration arg1, java.lang.String arg2, boolean arg3) {
    return null;
    }

    @Override
    public void chat(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public boolean performCommand(java.lang.String arg0) {
        if (arg0==null) return false;
        String line = arg0.startsWith("/")?arg0.substring(1):arg0;
        return LavaServer.getInstance().getCommandMap().dispatch(this, line);
    }

    @Override
    public boolean isOnGround() {
    return false;
    }

    @Override
    public boolean isSneaking() {
    return false;
    }

    @Override
    public void setSneaking(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isSprinting() {
    return false;
    }

    @Override
    public void setSprinting(boolean arg0) {
    // no-op stub
    }

    @Override
    public void saveData() {
    // no-op stub
    }

    @Override
    public void loadData() {
    // no-op stub
    }

    @Override
    public void setSleepingIgnored(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isSleepingIgnored() {
    return false;
    }

    @Override
    public org.bukkit.Location getBedSpawnLocation() {
    return null;
    }

    @Override
    public org.bukkit.Location getRespawnLocation() {
    return null;
    }

    @Override
    public void setBedSpawnLocation(org.bukkit.Location arg0) {
    // no-op stub
    }

    @Override
    public void setRespawnLocation(org.bukkit.Location arg0) {
    // no-op stub
    }

    @Override
    public void setBedSpawnLocation(org.bukkit.Location arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public void setRespawnLocation(org.bukkit.Location arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public void playNote(org.bukkit.Location arg0, byte arg1, byte arg2) {
    // no-op stub
    }

    @Override
    public void playNote(org.bukkit.Location arg0, org.bukkit.Instrument arg1, org.bukkit.Note arg2) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.Location arg0, org.bukkit.Sound arg1, float arg2, float arg3) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.Location arg0, java.lang.String arg1, float arg2, float arg3) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.Location arg0, org.bukkit.Sound arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.Location arg0, java.lang.String arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.Location arg0, org.bukkit.Sound arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4, long arg5) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.Location arg0, java.lang.String arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4, long arg5) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.entity.Entity arg0, org.bukkit.Sound arg1, float arg2, float arg3) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.entity.Entity arg0, java.lang.String arg1, float arg2, float arg3) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.entity.Entity arg0, org.bukkit.Sound arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.entity.Entity arg0, java.lang.String arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.entity.Entity arg0, org.bukkit.Sound arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4, long arg5) {
    // no-op stub
    }

    @Override
    public void playSound(org.bukkit.entity.Entity arg0, java.lang.String arg1, org.bukkit.SoundCategory arg2, float arg3, float arg4, long arg5) {
    // no-op stub
    }

    @Override
    public void stopSound(org.bukkit.Sound arg0) {
    // no-op stub
    }

    @Override
    public void stopSound(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void stopSound(org.bukkit.Sound arg0, org.bukkit.SoundCategory arg1) {
    // no-op stub
    }

    @Override
    public void stopSound(java.lang.String arg0, org.bukkit.SoundCategory arg1) {
    // no-op stub
    }

    @Override
    public void stopSound(org.bukkit.SoundCategory arg0) {
    // no-op stub
    }

    @Override
    public void stopAllSounds() {
    // no-op stub
    }

    @Override
    public void playEffect(org.bukkit.Location arg0, org.bukkit.Effect arg1, int arg2) {
    // no-op stub
    }

    @Override
    public <T> void playEffect(org.bukkit.Location arg0, org.bukkit.Effect arg1, T arg2) {
    }

    @Override
    public boolean breakBlock(org.bukkit.block.Block arg0) {
    return false;
    }

    @Override
    public void sendBlockChange(org.bukkit.Location arg0, org.bukkit.Material arg1, byte arg2) {
    // no-op stub
    }

    @Override
    public void sendBlockChange(org.bukkit.Location arg0, org.bukkit.block.data.BlockData arg1) {
    // no-op stub
    }

    @Override
    public void sendBlockChanges(java.util.Collection<org.bukkit.block.BlockState> arg0) {
    // no-op stub
    }

    @Override
    public void sendBlockChanges(java.util.Collection<org.bukkit.block.BlockState> arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public void sendBlockDamage(org.bukkit.Location arg0, float arg1) {
    // no-op stub
    }

    @Override
    public void sendMultiBlockChange(java.util.Map<? extends io.papermc.paper.math.Position, org.bukkit.block.data.BlockData> arg0) {
    // no-op stub
    }

    @Override
    public void sendBlockDamage(org.bukkit.Location arg0, float arg1, org.bukkit.entity.Entity arg2) {
    // no-op stub
    }

    @Override
    public void sendBlockDamage(org.bukkit.Location arg0, float arg1, int arg2) {
    // no-op stub
    }

    @Override
    public void sendEquipmentChange(org.bukkit.entity.LivingEntity arg0, org.bukkit.inventory.EquipmentSlot arg1, org.bukkit.inventory.ItemStack arg2) {
    // no-op stub
    }

    @Override
    public void sendEquipmentChange(org.bukkit.entity.LivingEntity arg0, java.util.Map<org.bukkit.inventory.EquipmentSlot, org.bukkit.inventory.ItemStack> arg1) {
    // no-op stub
    }

    @Override
    public void sendSignChange(org.bukkit.Location arg0, java.util.List<? extends net.kyori.adventure.text.Component> arg1, org.bukkit.DyeColor arg2, boolean arg3) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void sendSignChange(org.bukkit.Location arg0, java.lang.String[] arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void sendSignChange(org.bukkit.Location arg0, java.lang.String[] arg1, org.bukkit.DyeColor arg2) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void sendSignChange(org.bukkit.Location arg0, java.lang.String[] arg1, org.bukkit.DyeColor arg2, boolean arg3) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void sendBlockUpdate(org.bukkit.Location arg0, org.bukkit.block.TileState arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void sendPotionEffectChange(org.bukkit.entity.LivingEntity arg0, org.bukkit.potion.PotionEffect arg1) {
    // no-op stub
    }

    @Override
    public void sendPotionEffectChangeRemove(org.bukkit.entity.LivingEntity arg0, org.bukkit.potion.PotionEffectType arg1) {
    // no-op stub
    }

    @Override
    public void sendMap(org.bukkit.map.MapView arg0) {
    // no-op stub
    }

    @Override
    public void showWinScreen() {
    // no-op stub
    }

    @Override
    public boolean hasSeenWinScreen() {
    return false;
    }

    @Override
    public void setHasSeenWinScreen(boolean arg0) {
    // no-op stub
    }

    @Override
    public void sendActionBar(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void sendActionBar(char arg0, java.lang.String arg1) {
    // no-op stub
    }

    @Override
    public void sendActionBar(net.md_5.bungee.api.chat.BaseComponent... arg0) {
    // no-op stub
    }

    @Override
    public void setPlayerListHeaderFooter(net.md_5.bungee.api.chat.BaseComponent[] arg0, net.md_5.bungee.api.chat.BaseComponent[] arg1) {
    // no-op stub
    }

    @Override
    public void setPlayerListHeaderFooter(net.md_5.bungee.api.chat.BaseComponent arg0, net.md_5.bungee.api.chat.BaseComponent arg1) {
    // no-op stub
    }

    @Override
    public void setTitleTimes(int arg0, int arg1, int arg2) {
    // no-op stub
    }

    @Override
    public void setSubtitle(net.md_5.bungee.api.chat.BaseComponent[] arg0) {
    // no-op stub
    }

    @Override
    public void setSubtitle(net.md_5.bungee.api.chat.BaseComponent arg0) {
    // no-op stub
    }

    @Override
    public void showTitle(net.md_5.bungee.api.chat.BaseComponent[] arg0) {
    // no-op stub
    }

    @Override
    public void showTitle(net.md_5.bungee.api.chat.BaseComponent arg0) {
    // no-op stub
    }

    @Override
    public void showTitle(net.md_5.bungee.api.chat.BaseComponent[] arg0, net.md_5.bungee.api.chat.BaseComponent[] arg1, int arg2, int arg3, int arg4) {
    // no-op stub
    }

    @Override
    public void showTitle(net.md_5.bungee.api.chat.BaseComponent arg0, net.md_5.bungee.api.chat.BaseComponent arg1, int arg2, int arg3, int arg4) {
    // no-op stub
    }

    @Override
    public void sendTitle(com.destroystokyo.paper.Title arg0) {
    // no-op stub
    }

    @Override
    public void updateTitle(com.destroystokyo.paper.Title arg0) {
    // no-op stub
    }

    @Override
    public void hideTitle() {
    // no-op stub
    }

    @Override
    public void sendHurtAnimation(float arg0) {
    // no-op stub
    }

    @Override
    public void sendLinks(org.bukkit.ServerLinks arg0) {
    // no-op stub
    }

    @Override
    public void addCustomChatCompletions(java.util.Collection<java.lang.String> arg0) {
    // no-op stub
    }

    @Override
    public void removeCustomChatCompletions(java.util.Collection<java.lang.String> arg0) {
    // no-op stub
    }

    @Override
    public void setCustomChatCompletions(java.util.Collection<java.lang.String> arg0) {
    // no-op stub
    }

    @Override
    public void updateInventory() {
    // no-op stub
    }

    @Override
    public org.bukkit.GameMode getPreviousGameMode() {
    return null;
    }

    @Override
    public void setPlayerTime(long arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public long getPlayerTime() {
    return 0L;
    }

    @Override
    public long getPlayerTimeOffset() {
    return 0L;
    }

    @Override
    public boolean isPlayerTimeRelative() {
    return false;
    }

    @Override
    public void resetPlayerTime() {
    // no-op stub
    }

    @Override
    public void setPlayerWeather(org.bukkit.WeatherType arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.WeatherType getPlayerWeather() {
    return null;
    }

    @Override
    public void resetPlayerWeather() {
    // no-op stub
    }

    @Override
    public int getExpCooldown() {
    return 0;
    }

    @Override
    public void setExpCooldown(int arg0) {
    // no-op stub
    }

    @Override
    public void giveExp(int arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public int applyMending(int arg0) {
    return 0;
    }

    @Override
    public void giveExpLevels(int arg0) {
    // no-op stub
    }

    @Override
    public float getExp() {
    return 0.0f;
    }

    @Override
    public void setExp(float arg0) {
    // no-op stub
    }

    @Override
    public int getLevel() {
    return 0;
    }

    @Override
    public void setLevel(int arg0) {
    // no-op stub
    }

    @Override
    public int getTotalExperience() {
    return 0;
    }

    @Override
    public void setTotalExperience(int arg0) {
    // no-op stub
    }

    @Override
    public int calculateTotalExperiencePoints() {
    return 0;
    }

    @Override
    public void setExperienceLevelAndProgress(int arg0) {
    // no-op stub
    }

    @Override
    public int getExperiencePointsNeededForNextLevel() {
    return 0;
    }

    @Override
    public void sendExperienceChange(float arg0) {
    // no-op stub
    }

    @Override
    public void sendExperienceChange(float arg0, int arg1) {
    // no-op stub
    }

    @Override
    public boolean getAllowFlight() {
    return false;
    }

    @Override
    public void setAllowFlight(boolean arg0) {
    // no-op stub
    }

    @Override
    public void setFlyingFallDamage(net.kyori.adventure.util.TriState arg0) {
    // no-op stub
    }

    @Override
    public net.kyori.adventure.util.TriState hasFlyingFallDamage() {
    return null;
    }

    @Override
    public void hidePlayer(org.bukkit.entity.Player arg0) {
    // no-op stub
    }

    @Override
    public void hidePlayer(org.bukkit.plugin.Plugin arg0, org.bukkit.entity.Player arg1) {
    // no-op stub
    }

    @Override
    public void showPlayer(org.bukkit.entity.Player arg0) {
    // no-op stub
    }

    @Override
    public void showPlayer(org.bukkit.plugin.Plugin arg0, org.bukkit.entity.Player arg1) {
    // no-op stub
    }

    @Override
    public boolean canSee(org.bukkit.entity.Player arg0) {
    return false;
    }

    @Override
    public void hideEntity(org.bukkit.plugin.Plugin arg0, org.bukkit.entity.Entity arg1) {
    // no-op stub
    }

    @Override
    public void showEntity(org.bukkit.plugin.Plugin arg0, org.bukkit.entity.Entity arg1) {
    // no-op stub
    }

    @Override
    public boolean canSee(org.bukkit.entity.Entity arg0) {
    return false;
    }

    @Override
    public boolean isListed(org.bukkit.entity.Player arg0) {
    return false;
    }

    @Override
    public boolean unlistPlayer(org.bukkit.entity.Player arg0) {
    return false;
    }

    @Override
    public boolean listPlayer(org.bukkit.entity.Player arg0) {
    return false;
    }

    @Override
    public boolean isFlying() {
    return false;
    }

    @Override
    public void setFlying(boolean arg0) {
    // no-op stub
    }

    @Override
    public void setFlySpeed(float arg0) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void setWalkSpeed(float arg0) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public float getFlySpeed() {
    return 0.0f;
    }

    @Override
    public float getWalkSpeed() {
    return 0.0f;
    }

    @Override
    public void setTexturePack(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void setResourcePack(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void setResourcePack(java.lang.String arg0, byte[] arg1) {
    // no-op stub
    }

    @Override
    public void setResourcePack(java.lang.String arg0, byte[] arg1, java.lang.String arg2) {
    // no-op stub
    }

    @Override
    public void setResourcePack(java.lang.String arg0, byte[] arg1, boolean arg2) {
    // no-op stub
    }

    @Override
    public void setResourcePack(java.lang.String arg0, byte[] arg1, java.lang.String arg2, boolean arg3) {
    // no-op stub
    }

    @Override
    public void setResourcePack(java.util.UUID arg0, java.lang.String arg1, byte[] arg2, java.lang.String arg3, boolean arg4) {
    // no-op stub
    }

    @Override
    public void setResourcePack(java.util.UUID arg0, java.lang.String arg1, byte[] arg2, net.kyori.adventure.text.Component arg3, boolean arg4) {
    // no-op stub
    }

    @Override
    public org.bukkit.event.player.PlayerResourcePackStatusEvent.Status getResourcePackStatus() {
    return null;
    }

    @Override
    public void addResourcePack(java.util.UUID arg0, java.lang.String arg1, byte[] arg2, java.lang.String arg3, boolean arg4) {
    // no-op stub
    }

    @Override
    public void removeResourcePack(java.util.UUID arg0) {
    // no-op stub
    }

    @Override
    public void removeResourcePacks() {
    // no-op stub
    }

    @Override
    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
    return null;
    }

    @Override
    public void setScoreboard(org.bukkit.scoreboard.Scoreboard arg0) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException {
    // no-op stub
    }

    @Override
    public org.bukkit.WorldBorder getWorldBorder() {
    return null;
    }

    @Override
    public void setWorldBorder(org.bukkit.WorldBorder arg0) {
    // no-op stub
    }

    @Override
    public void sendHealthUpdate(double arg0, int arg1, float arg2) {
    // no-op stub
    }

    @Override
    public void sendHealthUpdate() {
    // no-op stub
    }

    @Override
    public boolean isHealthScaled() {
    return false;
    }

    @Override
    public void setHealthScaled(boolean arg0) {
    // no-op stub
    }

    @Override
    public void setHealthScale(double arg0) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public double getHealthScale() {
    return 0.0;
    }

    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
    return null;
    }

    @Override
    public void setSpectatorTarget(org.bukkit.entity.Entity arg0) {
    // no-op stub
    }

    @Override
    public void sendTitle(java.lang.String arg0, java.lang.String arg1) {
    // no-op stub
    }

    @Override
    public void sendTitle(java.lang.String arg0, java.lang.String arg1, int arg2, int arg3, int arg4) {
    // no-op stub
    }

    @Override
    public void resetTitle() {
    // no-op stub
    }

    @Override
    public void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2) {
    // no-op stub
    }

    @Override
    public void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4) {
    // no-op stub
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2, T arg3) {
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4, T arg5) {
    }

    @Override
    public void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2, double arg3, double arg4, double arg5) {
    // no-op stub
    }

    @Override
    public void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7) {
    // no-op stub
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2, double arg3, double arg4, double arg5, T arg6) {
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, T arg8) {
    }

    @Override
    public void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6) {
    // no-op stub
    }

    @Override
    public void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8) {
    // no-op stub
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, T arg7) {
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8, T arg9) {
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, T arg7, boolean arg8) {
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8, T arg9, boolean arg10) {
    }

    @Override
    public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(org.bukkit.advancement.Advancement arg0) {
    return null;
    }

    @Override
    public int getClientViewDistance() {
    return 0;
    }

    @Override
    public java.util.Locale locale() {
    return null;
    }

    @Override
    public int getPing() {
    return 0;
    }

    @Override
    public java.lang.String getLocale() {
    return "";
    }

    @Override
    public boolean getAffectsSpawning() {
    return false;
    }

    @Override
    public void setAffectsSpawning(boolean arg0) {
    // no-op stub
    }

    @Override
    public int getViewDistance() {
    return 0;
    }

    @Override
    public void setViewDistance(int arg0) {
    // no-op stub
    }

    @Override
    public int getSimulationDistance() {
    return 0;
    }

    @Override
    public void setSimulationDistance(int arg0) {
    // no-op stub
    }

    @Override
    public int getSendViewDistance() {
    return 0;
    }

    @Override
    public void setSendViewDistance(int arg0) {
    // no-op stub
    }

    @Override
    public void updateCommands() {
    // no-op stub
    }

    @Override
    public void openBook(org.bukkit.inventory.ItemStack arg0) {
    // no-op stub
    }

    @Override
    public void openSign(org.bukkit.block.Sign arg0) {
    // no-op stub
    }

    @Override
    public void openSign(org.bukkit.block.Sign arg0, org.bukkit.block.sign.Side arg1) {
    // no-op stub
    }

    @Override
    public void showDemoScreen() {
    // no-op stub
    }

    @Override
    public boolean isAllowingServerListings() {
    return false;
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() {
    return null;
    }

    @Override
    public void setPlayerProfile(com.destroystokyo.paper.profile.PlayerProfile arg0) {
    // no-op stub
    }

    @Override
    public float getCooldownPeriod() {
    return 0.0f;
    }

    @Override
    public float getCooledAttackStrength(float arg0) {
    return 0.0f;
    }

    @Override
    public void resetCooldown() {
    // no-op stub
    }

    @Override
    public <T> T getClientOption(com.destroystokyo.paper.ClientOption<T> arg0) {
    return null;
    }

    @Override
    public void sendOpLevel(byte arg0) {
    // no-op stub
    }

    @Override
    public void addAdditionalChatCompletions(java.util.Collection<java.lang.String> arg0) {
    // no-op stub
    }

    @Override
    public void removeAdditionalChatCompletions(java.util.Collection<java.lang.String> arg0) {
    // no-op stub
    }

    @Override
    public java.lang.String getClientBrandName() {
    return "";
    }

    @Override
    public void setRotation(float arg0, float arg1) {
    // no-op stub
    }

    @Override
    public void lookAt(double arg0, double arg1, double arg2, io.papermc.paper.entity.LookAnchor arg3) {
    // no-op stub
    }

    @Override
    public void lookAt(org.bukkit.entity.Entity arg0, io.papermc.paper.entity.LookAnchor arg1, io.papermc.paper.entity.LookAnchor arg2) {
    // no-op stub
    }

    @Override
    public void showElderGuardian(boolean arg0) {
    // no-op stub
    }

    @Override
    public int getWardenWarningCooldown() {
    return 0;
    }

    @Override
    public void setWardenWarningCooldown(int arg0) {
    // no-op stub
    }

    @Override
    public int getWardenTimeSinceLastWarning() {
    return 0;
    }

    @Override
    public void setWardenTimeSinceLastWarning(int arg0) {
    // no-op stub
    }

    @Override
    public int getWardenWarningLevel() {
    return 0;
    }

    @Override
    public void setWardenWarningLevel(int arg0) {
    // no-op stub
    }

    @Override
    public void increaseWardenWarningLevel() {
    // no-op stub
    }

    @Override
    public java.time.Duration getIdleDuration() {
    return null;
    }

    @Override
    public void resetIdleDuration() {
    // no-op stub
    }

    @Override
    public java.util.Set<java.lang.Long> getSentChunkKeys() {
    return Set.of();
    }

    @Override
    public java.util.Set<org.bukkit.Chunk> getSentChunks() {
    return Set.of();
    }

    @Override
    public boolean isChunkSent(long arg0) {
    return false;
    }

    @Override
    public org.bukkit.entity.Player.Spigot spigot() {
    return null;
    }

    @Override
    public void sendEntityEffect(org.bukkit.EntityEffect arg0, org.bukkit.entity.Entity arg1) {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.EntityEquipment getEquipment() {
    return null;
    }

    @Override
    public org.bukkit.inventory.PlayerInventory getInventory() {
    return null;
    }

    @Override
    public org.bukkit.inventory.Inventory getEnderChest() {
    return null;
    }

    @Override
    public org.bukkit.inventory.MainHand getMainHand() {
    return null;
    }

    @Override
    public boolean setWindowProperty(org.bukkit.inventory.InventoryView.Property arg0, int arg1) {
    return false;
    }

    @Override
    public int getEnchantmentSeed() {
    return 0;
    }

    @Override
    public void setEnchantmentSeed(int arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.InventoryView getOpenInventory() {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openInventory(org.bukkit.inventory.Inventory arg0) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openWorkbench(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openEnchanting(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public void openInventory(org.bukkit.inventory.InventoryView arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.InventoryView openMerchant(org.bukkit.entity.Villager arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openMerchant(org.bukkit.inventory.Merchant arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openAnvil(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openCartographyTable(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openGrindstone(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openLoom(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openSmithingTable(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.InventoryView openStonecutter(org.bukkit.Location arg0, boolean arg1) {
    return null;
    }

    @Override
    public void closeInventory() {
    // no-op stub
    }

    @Override
    public void closeInventory(org.bukkit.event.inventory.InventoryCloseEvent.Reason arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.ItemStack getItemInHand() {
    return null;
    }

    @Override
    public void setItemInHand(org.bukkit.inventory.ItemStack arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.ItemStack getItemOnCursor() {
    return null;
    }

    @Override
    public void setItemOnCursor(org.bukkit.inventory.ItemStack arg0) {
    // no-op stub
    }

    @Override
    public boolean hasCooldown(org.bukkit.Material arg0) {
    return false;
    }

    @Override
    public int getCooldown(org.bukkit.Material arg0) {
    return 0;
    }

    @Override
    public void setCooldown(org.bukkit.Material arg0, int arg1) {
    // no-op stub
    }

    @Override
    public void setHurtDirection(float arg0) {
    // no-op stub
    }

    @Override
    public boolean isDeeplySleeping() {
    return false;
    }

    @Override
    public int getSleepTicks() {
    return 0;
    }

    @Override
    public org.bukkit.Location getPotentialBedLocation() {
    return null;
    }

    @Override
    public org.bukkit.entity.FishHook getFishHook() {
    return null;
    }

    @Override
    public boolean sleep(org.bukkit.Location arg0, boolean arg1) {
    return false;
    }

    @Override
    public void wakeup(boolean arg0) {
    // no-op stub
    }

    @Override
    public void startRiptideAttack(int arg0, float arg1, org.bukkit.inventory.ItemStack arg2) {
    // no-op stub
    }

    @Override
    public org.bukkit.Location getBedLocation() {
    return null;
    }

    @Override
    public org.bukkit.GameMode getGameMode() {
        return switch (handle.gameMode.getGameModeForPlayer()) {
            case SURVIVAL -> GameMode.SURVIVAL;
            case CREATIVE -> GameMode.CREATIVE;
            case ADVENTURE -> GameMode.ADVENTURE;
            case SPECTATOR -> GameMode.SPECTATOR;
        };
    }
    @Override public void setGameMode(org.bukkit.GameMode arg0) {
        if (arg0==null) return;
        handle.setGameMode(switch (arg0) {
            case SURVIVAL -> net.minecraft.world.level.GameType.SURVIVAL;
            case CREATIVE -> net.minecraft.world.level.GameType.CREATIVE;
            case ADVENTURE -> net.minecraft.world.level.GameType.ADVENTURE;
            case SPECTATOR -> net.minecraft.world.level.GameType.SPECTATOR;
        });
    }

    @Override
    public boolean isBlocking() {
    return false;
    }

    @Override
    public boolean isHandRaised() {
    return false;
    }

    @Override
    public int getExpToLevel() {
    return 0;
    }

    @Override
    public org.bukkit.entity.Entity releaseLeftShoulderEntity() {
    return null;
    }

    @Override
    public org.bukkit.entity.Entity releaseRightShoulderEntity() {
    return null;
    }

    @Override
    public float getAttackCooldown() {
    return 0.0f;
    }

    @Override
    public boolean discoverRecipe(org.bukkit.NamespacedKey arg0) {
    return false;
    }

    @Override
    public int discoverRecipes(java.util.Collection<org.bukkit.NamespacedKey> arg0) {
    return 0;
    }

    @Override
    public boolean undiscoverRecipe(org.bukkit.NamespacedKey arg0) {
    return false;
    }

    @Override
    public int undiscoverRecipes(java.util.Collection<org.bukkit.NamespacedKey> arg0) {
    return 0;
    }

    @Override
    public boolean hasDiscoveredRecipe(org.bukkit.NamespacedKey arg0) {
    return false;
    }

    @Override
    public java.util.Set<org.bukkit.NamespacedKey> getDiscoveredRecipes() {
    return Set.of();
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityLeft() {
    return null;
    }

    @Override
    public void setShoulderEntityLeft(org.bukkit.entity.Entity arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityRight() {
    return null;
    }

    @Override
    public void setShoulderEntityRight(org.bukkit.entity.Entity arg0) {
    // no-op stub
    }

    @Override
    public boolean dropItem(boolean arg0) {
    return false;
    }

    @Override
    public float getExhaustion() {
    return 0.0f;
    }

    @Override
    public void setExhaustion(float arg0) {
    // no-op stub
    }

    @Override
    public float getSaturation() {
    return 0.0f;
    }

    @Override
    public void setSaturation(float arg0) {
    // no-op stub
    }

    @Override
    public int getFoodLevel() { return DirectPlayerOps.getFoodLevel(handle); }

    @Override
    public void setFoodLevel(int arg0) {
    // no-op stub
    }

    @Override
    public int getSaturatedRegenRate() {
    return 0;
    }

    @Override
    public void setSaturatedRegenRate(int arg0) {
    // no-op stub
    }

    @Override
    public int getUnsaturatedRegenRate() {
    return 0;
    }

    @Override
    public void setUnsaturatedRegenRate(int arg0) {
    // no-op stub
    }

    @Override
    public int getStarvationRate() {
    return 0;
    }

    @Override
    public void setStarvationRate(int arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.Location getLastDeathLocation() {
    return null;
    }

    @Override
    public void setLastDeathLocation(org.bukkit.Location arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.entity.Firework fireworkBoost(org.bukkit.inventory.ItemStack arg0) {
    return null;
    }

    @Override
    public double getEyeHeight() {
    return 0.0;
    }

    @Override
    public double getEyeHeight(boolean arg0) {
    return 0.0;
    }

    @Override
    public org.bukkit.Location getEyeLocation() {
    return null;
    }

    @Override
    public java.util.List<org.bukkit.block.Block> getLineOfSight(java.util.Set<org.bukkit.Material> arg0, int arg1) {
    return List.of();
    }

    @Override
    public org.bukkit.block.Block getTargetBlock(java.util.Set<org.bukkit.Material> arg0, int arg1) {
    return null;
    }

    @Override
    public org.bukkit.block.Block getTargetBlock(int arg0, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode arg1) {
    return null;
    }

    @Override
    public org.bukkit.block.BlockFace getTargetBlockFace(int arg0, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode arg1) {
    return null;
    }

    @Override
    public org.bukkit.block.BlockFace getTargetBlockFace(int arg0, org.bukkit.FluidCollisionMode arg1) {
    return null;
    }

    @Override
    public com.destroystokyo.paper.block.TargetBlockInfo getTargetBlockInfo(int arg0, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode arg1) {
    return null;
    }

    @Override
    public org.bukkit.entity.Entity getTargetEntity(int arg0, boolean arg1) {
    return null;
    }

    @Override
    public com.destroystokyo.paper.entity.TargetEntityInfo getTargetEntityInfo(int arg0, boolean arg1) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceEntities(int arg0, boolean arg1) {
    return null;
    }

    @Override
    public java.util.List<org.bukkit.block.Block> getLastTwoTargetBlocks(java.util.Set<org.bukkit.Material> arg0, int arg1) {
    return List.of();
    }

    @Override
    public org.bukkit.block.Block getTargetBlockExact(int arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.Block getTargetBlockExact(int arg0, org.bukkit.FluidCollisionMode arg1) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceBlocks(double arg0) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceBlocks(double arg0, org.bukkit.FluidCollisionMode arg1) {
    return null;
    }

    @Override
    public int getRemainingAir() {
    return 0;
    }

    @Override
    public void setRemainingAir(int arg0) {
    // no-op stub
    }

    @Override
    public int getMaximumAir() {
    return 0;
    }

    @Override
    public void setMaximumAir(int arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.ItemStack getItemInUse() {
    return null;
    }

    @Override
    public int getItemInUseTicks() {
    return 0;
    }

    @Override
    public void setItemInUseTicks(int arg0) {
    // no-op stub
    }

    @Override
    public int getArrowCooldown() {
    return 0;
    }

    @Override
    public void setArrowCooldown(int arg0) {
    // no-op stub
    }

    @Override
    public int getArrowsInBody() {
    return 0;
    }

    @Override
    public void setArrowsInBody(int arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public void setNextArrowRemoval(int arg0) {
    // no-op stub
    }

    @Override
    public int getNextArrowRemoval() {
    return 0;
    }

    @Override
    public int getBeeStingerCooldown() {
    return 0;
    }

    @Override
    public void setBeeStingerCooldown(int arg0) {
    // no-op stub
    }

    @Override
    public int getBeeStingersInBody() {
    return 0;
    }

    @Override
    public void setBeeStingersInBody(int arg0) {
    // no-op stub
    }

    @Override
    public void setNextBeeStingerRemoval(int arg0) {
    // no-op stub
    }

    @Override
    public int getNextBeeStingerRemoval() {
    return 0;
    }

    @Override
    public int getMaximumNoDamageTicks() {
    return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int arg0) {
    // no-op stub
    }

    @Override
    public double getLastDamage() {
    return 0.0;
    }

    @Override
    public void setLastDamage(double arg0) {
    // no-op stub
    }

    @Override
    public int getNoDamageTicks() {
    return 0;
    }

    @Override
    public void setNoDamageTicks(int arg0) {
    // no-op stub
    }

    @Override
    public int getNoActionTicks() {
    return 0;
    }

    @Override
    public void setNoActionTicks(int arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.entity.Player getKiller() {
    return null;
    }

    @Override
    public void setKiller(org.bukkit.entity.Player arg0) {
    // no-op stub
    }

    @Override
    public boolean addPotionEffect(org.bukkit.potion.PotionEffect arg0) {
    return false;
    }

    @Override
    public boolean addPotionEffect(org.bukkit.potion.PotionEffect arg0, boolean arg1) {
    return false;
    }

    @Override
    public boolean addPotionEffects(java.util.Collection<org.bukkit.potion.PotionEffect> arg0) {
    return false;
    }

    @Override
    public boolean hasPotionEffect(org.bukkit.potion.PotionEffectType arg0) {
    return false;
    }

    @Override
    public org.bukkit.potion.PotionEffect getPotionEffect(org.bukkit.potion.PotionEffectType arg0) {
    return null;
    }

    @Override
    public void removePotionEffect(org.bukkit.potion.PotionEffectType arg0) {
    // no-op stub
    }

    @Override
    public java.util.Collection<org.bukkit.potion.PotionEffect> getActivePotionEffects() {
    return List.of();
    }

    @Override
    public boolean clearActivePotionEffects() {
    return false;
    }

    @Override
    public boolean hasLineOfSight(org.bukkit.entity.Entity arg0) {
    return false;
    }

    @Override
    public boolean hasLineOfSight(org.bukkit.Location arg0) {
    return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
    return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean arg0) {
    // no-op stub
    }

    @Override
    public void setCanPickupItems(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean getCanPickupItems() {
    return false;
    }

    @Override
    public boolean isLeashed() {
    return false;
    }

    @Override
    public org.bukkit.entity.Entity getLeashHolder() throws java.lang.IllegalStateException {
    return null;
    }

    @Override
    public boolean setLeashHolder(org.bukkit.entity.Entity arg0) {
    return false;
    }

    @Override
    public boolean isGliding() {
    return false;
    }

    @Override
    public void setGliding(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isSwimming() {
    return false;
    }

    @Override
    public void setSwimming(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isRiptiding() {
    return false;
    }

    @Override
    public void setRiptiding(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isSleeping() {
    return false;
    }

    @Override
    public boolean isClimbing() {
    return false;
    }

    @Override
    public void setAI(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean hasAI() {
    return false;
    }

    @Override
    public void attack(org.bukkit.entity.Entity arg0) {
    // no-op stub
    }

    @Override
    public void swingMainHand() {
    // no-op stub
    }

    @Override
    public void swingOffHand() {
    // no-op stub
    }

    @Override
    public void playHurtAnimation(float arg0) {
    // no-op stub
    }

    @Override
    public void setCollidable(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isCollidable() {
    return false;
    }

    @Override
    public java.util.Set<java.util.UUID> getCollidableExemptions() {
    return Set.of();
    }

    @Override
    public <T> T getMemory(org.bukkit.entity.memory.MemoryKey<T> arg0) {
    return null;
    }

    @Override
    public <T> void setMemory(org.bukkit.entity.memory.MemoryKey<T> arg0, T arg1) {
    }

    @Override
    public org.bukkit.Sound getHurtSound() {
    return null;
    }

    @Override
    public org.bukkit.Sound getDeathSound() {
    return null;
    }

    @Override
    public org.bukkit.Sound getFallDamageSound(int arg0) {
    return null;
    }

    @Override
    public org.bukkit.Sound getFallDamageSoundSmall() {
    return null;
    }

    @Override
    public org.bukkit.Sound getFallDamageSoundBig() {
    return null;
    }

    @Override
    public org.bukkit.Sound getDrinkingSound(org.bukkit.inventory.ItemStack arg0) {
    return null;
    }

    @Override
    public org.bukkit.Sound getEatingSound(org.bukkit.inventory.ItemStack arg0) {
    return null;
    }

    @Override
    public boolean canBreatheUnderwater() {
    return false;
    }

    @Override
    public org.bukkit.entity.EntityCategory getCategory() {
    return null;
    }

    @Override
    public void setInvisible(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isInvisible() {
    return false;
    }

    @Override
    public int getArrowsStuck() {
    return 0;
    }

    @Override
    public void setArrowsStuck(int arg0) {
    // no-op stub
    }

    @Override
    public int getShieldBlockingDelay() {
    return 0;
    }

    @Override
    public void setShieldBlockingDelay(int arg0) {
    // no-op stub
    }

    @Override
    public float getSidewaysMovement() {
    return 0.0f;
    }

    @Override
    public float getUpwardsMovement() {
    return 0.0f;
    }

    @Override
    public float getForwardsMovement() {
    return 0.0f;
    }

    @Override
    public void startUsingItem(org.bukkit.inventory.EquipmentSlot arg0) {
    // no-op stub
    }

    @Override
    public void completeUsingActiveItem() {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.ItemStack getActiveItem() {
    return null;
    }

    @Override
    public void clearActiveItem() {
    // no-op stub
    }

    @Override
    public int getActiveItemRemainingTime() {
    return 0;
    }

    @Override
    public void setActiveItemRemainingTime(int arg0) {
    // no-op stub
    }

    @Override
    public boolean hasActiveItem() {
    return false;
    }

    @Override
    public int getActiveItemUsedTime() {
    return 0;
    }

    @Override
    public org.bukkit.inventory.EquipmentSlot getActiveItemHand() {
    return null;
    }

    @Override
    public boolean isJumping() {
    return false;
    }

    @Override
    public void setJumping(boolean arg0) {
    // no-op stub
    }

    @Override
    public void playPickupItemAnimation(org.bukkit.entity.Item arg0, int arg1) {
    // no-op stub
    }

    @Override
    public float getHurtDirection() {
    return 0.0f;
    }

    @Override
    public void knockback(double arg0, double arg1, double arg2) {
    // no-op stub
    }

    @Override
    public void broadcastSlotBreak(org.bukkit.inventory.EquipmentSlot arg0) {
    // no-op stub
    }

    @Override
    public void broadcastSlotBreak(org.bukkit.inventory.EquipmentSlot arg0, java.util.Collection<org.bukkit.entity.Player> arg1) {
    // no-op stub
    }

    @Override
    public org.bukkit.inventory.ItemStack damageItemStack(org.bukkit.inventory.ItemStack arg0, int arg1) {
    return null;
    }

    @Override
    public void damageItemStack(org.bukkit.inventory.EquipmentSlot arg0, int arg1) {
    // no-op stub
    }

    @Override
    public float getBodyYaw() {
    return 0.0f;
    }

    @Override
    public void setBodyYaw(float arg0) {
    // no-op stub
    }

    @Override
    public boolean canUseEquipmentSlot(org.bukkit.inventory.EquipmentSlot arg0) {
    return false;
    }

    @Override
    public org.bukkit.attribute.AttributeInstance getAttribute(org.bukkit.attribute.Attribute arg0) {
    return null;
    }

    @Override
    public void registerAttribute(org.bukkit.attribute.Attribute arg0) {
    // no-op stub
    }

    @Override
    public void damage(double arg0) {
    // no-op stub
    }

    @Override
    public void damage(double arg0, org.bukkit.entity.Entity arg1) {
    // no-op stub
    }

    @Override
    public void damage(double arg0, org.bukkit.damage.DamageSource arg1) {
    // no-op stub
    }

    @Override
    public double getHealth() { return DirectPlayerOps.getHealth(handle); }
    @Override public void setHealth(double arg0) { DirectPlayerOps.setHealth(handle, arg0); }

    @Override
    public void heal(double arg0, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason arg1) {
    // no-op stub
    }

    @Override
    public double getAbsorptionAmount() {
    return 0.0;
    }

    @Override
    public void setAbsorptionAmount(double arg0) {
    // no-op stub
    }

    @Override
    public double getMaxHealth() {
    return 0.0;
    }

    @Override
    public void setMaxHealth(double arg0) {
    // no-op stub
    }

    @Override
    public void resetMaxHealth() {
    // no-op stub
    }

    @Override
    public void setVelocity(org.bukkit.util.Vector arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.util.Vector getVelocity() {
    return null;
    }

    @Override
    public double getHeight() {
    return 0.0;
    }

    @Override
    public double getWidth() {
    return 0.0;
    }

    @Override
    public org.bukkit.util.BoundingBox getBoundingBox() {
    return null;
    }

    @Override
    public boolean isInWater() {
    return false;
    }

    @Override
    public boolean teleport(org.bukkit.Location arg0, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause arg1, io.papermc.paper.entity.TeleportFlag... arg2) {
    return false;
    }

    @Override
    public boolean teleport(org.bukkit.Location arg0) {
        if (arg0==null || arg0.getWorld()==null) return false;
        net.minecraft.server.level.ServerLevel level = handle.serverLevel();
        if (arg0.getWorld() instanceof io.github.lavacore.bridge.world.LavaWorldWrapper wrap && wrap.getHandle() instanceof net.minecraft.server.level.ServerLevel sl) level = sl;
        handle.teleportTo(level, arg0.getX(), arg0.getY(), arg0.getZ(), java.util.Set.of(), arg0.getYaw(), arg0.getPitch(), true);
        return true;
    }

    @Override
    public boolean teleport(org.bukkit.Location arg0, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause arg1) {
    return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity arg0) {
    return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity arg0, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause arg1) {
    return false;
    }

    @Override
    public java.util.concurrent.CompletableFuture<java.lang.Boolean> teleportAsync(org.bukkit.Location arg0, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause arg1, io.papermc.paper.entity.TeleportFlag... arg2) {
    return CompletableFuture.completedFuture(null);
    }

    @Override
    public java.util.List<org.bukkit.entity.Entity> getNearbyEntities(double arg0, double arg1, double arg2) {
    return List.of();
    }

    @Override
    public int getEntityId() {
    return 0;
    }

    @Override
    public int getFireTicks() {
    return 0;
    }

    @Override
    public int getMaxFireTicks() {
    return 0;
    }

    @Override
    public void setFireTicks(int arg0) {
    // no-op stub
    }

    @Override
    public void setVisualFire(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isVisualFire() {
    return false;
    }

    @Override
    public int getFreezeTicks() {
    return 0;
    }

    @Override
    public int getMaxFreezeTicks() {
    return 0;
    }

    @Override
    public void setFreezeTicks(int arg0) {
    // no-op stub
    }

    @Override
    public boolean isFrozen() {
    return false;
    }

    @Override
    public void setNoPhysics(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean hasNoPhysics() {
    return false;
    }

    @Override
    public boolean isFreezeTickingLocked() {
    return false;
    }

    @Override
    public void lockFreezeTicks(boolean arg0) {
    // no-op stub
    }

    @Override
    public void remove() {
    // no-op stub
    }

    @Override
    public boolean isDead() {
    return false;
    }

    @Override
    public boolean isValid() {
    return false;
    }

    @Override
    public boolean isPersistent() {
    return false;
    }

    @Override
    public void setPersistent(boolean arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
    return null;
    }

    @Override
    public boolean setPassenger(org.bukkit.entity.Entity arg0) {
    return false;
    }

    @Override
    public java.util.List<org.bukkit.entity.Entity> getPassengers() {
    return List.of();
    }

    @Override
    public boolean addPassenger(org.bukkit.entity.Entity arg0) {
    return false;
    }

    @Override
    public boolean removePassenger(org.bukkit.entity.Entity arg0) {
    return false;
    }

    @Override
    public boolean isEmpty() {
    return false;
    }

    @Override
    public boolean eject() {
    return false;
    }

    @Override
    public float getFallDistance() {
    return 0.0f;
    }

    @Override
    public void setFallDistance(float arg0) {
    // no-op stub
    }

    @Override
    public void setLastDamageCause(org.bukkit.event.entity.EntityDamageEvent arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.event.entity.EntityDamageEvent getLastDamageCause() {
    return null;
    }

    @Override
    public int getTicksLived() {
    return 0;
    }

    @Override
    public void setTicksLived(int arg0) {
    // no-op stub
    }

    @Override
    public void playEffect(org.bukkit.EntityEffect arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.entity.EntityType getType() {
    return null;
    }

    @Override
    public org.bukkit.Sound getSwimSound() {
    return null;
    }

    @Override
    public org.bukkit.Sound getSwimSplashSound() {
    return null;
    }

    @Override
    public org.bukkit.Sound getSwimHighSpeedSplashSound() {
    return null;
    }

    @Override
    public boolean isInsideVehicle() {
    return false;
    }

    @Override
    public boolean leaveVehicle() {
    return false;
    }

    @Override
    public org.bukkit.entity.Entity getVehicle() {
    return null;
    }

    @Override
    public void setCustomNameVisible(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isCustomNameVisible() {
    return false;
    }

    @Override
    public void setVisibleByDefault(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isVisibleByDefault() {
    return false;
    }

    @Override
    public java.util.Set<org.bukkit.entity.Player> getTrackedBy() {
    return Set.of();
    }

    @Override
    public void setGlowing(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isGlowing() {
    return false;
    }

    @Override
    public void setInvulnerable(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isInvulnerable() {
    return false;
    }

    @Override
    public boolean isSilent() {
    return false;
    }

    @Override
    public void setSilent(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean hasGravity() {
    return false;
    }

    @Override
    public void setGravity(boolean arg0) {
    // no-op stub
    }

    @Override
    public int getPortalCooldown() {
    return 0;
    }

    @Override
    public void setPortalCooldown(int arg0) {
    // no-op stub
    }

    @Override
    public java.util.Set<java.lang.String> getScoreboardTags() {
    return Set.of();
    }

    @Override
    public boolean addScoreboardTag(java.lang.String arg0) {
    return false;
    }

    @Override
    public boolean removeScoreboardTag(java.lang.String arg0) {
    return false;
    }

    @Override
    public org.bukkit.block.PistonMoveReaction getPistonMoveReaction() {
    return null;
    }

    @Override
    public org.bukkit.block.BlockFace getFacing() {
    return null;
    }

    @Override
    public org.bukkit.entity.Pose getPose() {
    return null;
    }

    @Override
    public void setPose(org.bukkit.entity.Pose arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public boolean hasFixedPose() {
    return false;
    }

    @Override
    public org.bukkit.entity.SpawnCategory getSpawnCategory() {
    return null;
    }

    @Override
    public boolean isInWorld() {
    return false;
    }

    @Override
    public java.lang.String getAsString() {
    return "";
    }

    @Override
    public org.bukkit.entity.EntitySnapshot createSnapshot() {
    return null;
    }

    @Override
    public org.bukkit.entity.Entity copy() {
    return null;
    }

    @Override
    public org.bukkit.entity.Entity copy(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public net.kyori.adventure.text.Component teamDisplayName() {
    return Component.empty();
    }

    @Override
    public org.bukkit.Location getOrigin() {
    return null;
    }

    @Override
    public boolean fromMobSpawner() {
    return false;
    }

    @Override
    public org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason getEntitySpawnReason() {
    return null;
    }

    @Override
    public boolean isUnderWater() {
    return false;
    }

    @Override
    public boolean isInRain() {
    return false;
    }

    @Override
    public boolean isInBubbleColumn() {
    return false;
    }

    @Override
    public boolean isInWaterOrRain() {
    return false;
    }

    @Override
    public boolean isInWaterOrBubbleColumn() {
    return false;
    }

    @Override
    public boolean isInWaterOrRainOrBubbleColumn() {
    return false;
    }

    @Override
    public boolean isInLava() {
    return false;
    }

    @Override
    public boolean isTicking() {
    return false;
    }

    @Override
    public java.util.Set<org.bukkit.entity.Player> getTrackedPlayers() {
    return Set.of();
    }

    @Override
    public boolean spawnAt(org.bukkit.Location arg0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason arg1) {
    return false;
    }

    @Override
    public boolean isInPowderedSnow() {
    return false;
    }

    @Override
    public double getX() {
    return 0.0;
    }

    @Override
    public double getY() {
    return 0.0;
    }

    @Override
    public double getZ() {
    return 0.0;
    }

    @Override
    public float getPitch() {
    return 0.0f;
    }

    @Override
    public float getYaw() {
    return 0.0f;
    }

    @Override
    public boolean collidesAt(org.bukkit.Location arg0) {
    return false;
    }

    @Override
    public boolean wouldCollideUsing(org.bukkit.util.BoundingBox arg0) {
    return false;
    }

    @Override
    public io.papermc.paper.threadedregions.scheduler.EntityScheduler getScheduler() {
    return null;
    }

    @Override
    public java.lang.String getScoreboardEntryName() {
    return "";
    }

    @Override
    public void broadcastHurtAnimation(java.util.Collection<org.bukkit.entity.Player> arg0) {
    // no-op stub
    }

    @Override
    public void setMetadata(java.lang.String arg0, org.bukkit.metadata.MetadataValue arg1) {
    // no-op stub
    }

    @Override
    public java.util.List<org.bukkit.metadata.MetadataValue> getMetadata(java.lang.String arg0) {
    return List.of();
    }

    @Override
    public boolean hasMetadata(java.lang.String arg0) {
    return false;
    }

    @Override
    public void removeMetadata(java.lang.String arg0, org.bukkit.plugin.Plugin arg1) {
    // no-op stub
    }

    @Override
    public void sendMessage(java.lang.String arg0) {
        if (arg0!=null) handle.sendSystemMessage(net.minecraft.network.chat.Component.literal(arg0));
    }

    @Override
    public void sendMessage(java.lang.String... arg0) {
    // no-op stub
    }

    @Override
    public void sendMessage(java.util.UUID arg0, java.lang.String arg1) {
    // no-op stub
    }

    @Override
    public void sendMessage(java.util.UUID arg0, java.lang.String... arg1) {
    // no-op stub
    }

    @Override
    public boolean isPermissionSet(java.lang.String arg0) {
    return false;
    }

    @Override
    public boolean isPermissionSet(org.bukkit.permissions.Permission arg0) {
    return false;
    }

    @Override
    public boolean hasPermission(java.lang.String arg0) { return isOp() || perm.hasPermission(arg0); }

    @Override
    public boolean hasPermission(org.bukkit.permissions.Permission arg0) {
    return false;
    }

    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(org.bukkit.plugin.Plugin arg0, java.lang.String arg1, boolean arg2) {
    return null;
    }

    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(org.bukkit.plugin.Plugin arg0) {
    return null;
    }

    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(org.bukkit.plugin.Plugin arg0, java.lang.String arg1, boolean arg2, int arg3) {
    return null;
    }

    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(org.bukkit.plugin.Plugin arg0, int arg1) {
    return null;
    }

    @Override
    public void removeAttachment(org.bukkit.permissions.PermissionAttachment arg0) {
    // no-op stub
    }

    @Override
    public void recalculatePermissions() {
    // no-op stub
    }

    @Override
    public java.util.Set<org.bukkit.permissions.PermissionAttachmentInfo> getEffectivePermissions() {
    return Set.of();
    }

    @Override
    public boolean isOp() { return handle.server.getPlayerList().isOp(handle.getGameProfile()); }
    @Override public void setOp(boolean arg0) {
        var list = handle.server.getPlayerList();
        if (arg0) list.op(handle.getGameProfile()); else list.deop(handle.getGameProfile());
        perm.recalculatePermissions();
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
    return Component.empty();
    }

    @Override
    public void customName(net.kyori.adventure.text.Component arg0) {
    // no-op stub
    }

    @Override
    public java.lang.String getCustomName() {
    return "";
    }

    @Override
    public void setCustomName(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.persistence.PersistentDataContainer getPersistentDataContainer() {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Projectile> T launchProjectile(java.lang.Class<? extends T> arg0) {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Projectile> T launchProjectile(java.lang.Class<? extends T> arg0, org.bukkit.util.Vector arg1) {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Projectile> T launchProjectile(java.lang.Class<? extends T> arg0, org.bukkit.util.Vector arg1, java.util.function.Consumer<? super T> arg2) {
    return null;
    }

    @Override
    public net.kyori.adventure.util.TriState getFrictionState() {
    return null;
    }

    @Override
    public void setFrictionState(net.kyori.adventure.util.TriState arg0) {
    // no-op stub
    }

    @Override
    public boolean isConversing() {
    return false;
    }

    @Override
    public void acceptConversationInput(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public boolean beginConversation(org.bukkit.conversations.Conversation arg0) {
    return false;
    }

    @Override
    public void abandonConversation(org.bukkit.conversations.Conversation arg0) {
    // no-op stub
    }

    @Override
    public void abandonConversation(org.bukkit.conversations.Conversation arg0, org.bukkit.conversations.ConversationAbandonedEvent arg1) {
    // no-op stub
    }

    @Override
    public void sendRawMessage(java.util.UUID arg0, java.lang.String arg1) {
    // no-op stub
    }

    @Override
    public boolean isConnected() {
    return false;
    }

    @Override
    public boolean isBanned() {
    return false;
    }

    @Override
    public <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(java.lang.String arg0, java.util.Date arg1, java.lang.String arg2) {
    return null;
    }

    @Override
    public <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(java.lang.String arg0, java.time.Instant arg1, java.lang.String arg2) {
    return null;
    }

    @Override
    public <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(java.lang.String arg0, java.time.Duration arg1, java.lang.String arg2) {
    return null;
    }

    @Override
    public boolean isWhitelisted() {
    return false;
    }

    @Override
    public void setWhitelisted(boolean arg0) {
    // no-op stub
    }

    @Override
    public long getFirstPlayed() {
    return 0L;
    }

    @Override
    public long getLastPlayed() {
    return 0L;
    }

    @Override
    public boolean hasPlayedBefore() {
    return false;
    }

    @Override
    public long getLastLogin() {
    return 0L;
    }

    @Override
    public long getLastSeen() {
    return 0L;
    }

    @Override
    public void incrementStatistic(org.bukkit.Statistic arg0) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void decrementStatistic(org.bukkit.Statistic arg0) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void incrementStatistic(org.bukkit.Statistic arg0, int arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void decrementStatistic(org.bukkit.Statistic arg0, int arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void setStatistic(org.bukkit.Statistic arg0, int arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public int getStatistic(org.bukkit.Statistic arg0) throws java.lang.IllegalArgumentException {
    return 0;
    }

    @Override
    public void incrementStatistic(org.bukkit.Statistic arg0, org.bukkit.Material arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void decrementStatistic(org.bukkit.Statistic arg0, org.bukkit.Material arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public int getStatistic(org.bukkit.Statistic arg0, org.bukkit.Material arg1) throws java.lang.IllegalArgumentException {
    return 0;
    }

    @Override
    public void incrementStatistic(org.bukkit.Statistic arg0, org.bukkit.Material arg1, int arg2) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void decrementStatistic(org.bukkit.Statistic arg0, org.bukkit.Material arg1, int arg2) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void setStatistic(org.bukkit.Statistic arg0, org.bukkit.Material arg1, int arg2) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void incrementStatistic(org.bukkit.Statistic arg0, org.bukkit.entity.EntityType arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void decrementStatistic(org.bukkit.Statistic arg0, org.bukkit.entity.EntityType arg1) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public int getStatistic(org.bukkit.Statistic arg0, org.bukkit.entity.EntityType arg1) throws java.lang.IllegalArgumentException {
    return 0;
    }

    @Override
    public void incrementStatistic(org.bukkit.Statistic arg0, org.bukkit.entity.EntityType arg1, int arg2) throws java.lang.IllegalArgumentException {
    // no-op stub
    }

    @Override
    public void decrementStatistic(org.bukkit.Statistic arg0, org.bukkit.entity.EntityType arg1, int arg2) {
    // no-op stub
    }

    @Override
    public void setStatistic(org.bukkit.Statistic arg0, org.bukkit.entity.EntityType arg1, int arg2) {
    // no-op stub
    }

    @Override
    public java.util.Map<java.lang.String, java.lang.Object> serialize() {
    return Map.of();
    }

    @Override
    public void sendPluginMessage(org.bukkit.plugin.Plugin arg0, java.lang.String arg1, byte[] arg2) {
    // no-op stub
    }

    @Override
    public java.util.Set<java.lang.String> getListeningPluginChannels() {
    return Set.of();
    }

    @Override
    public net.kyori.adventure.identity.Identity identity() {
    return null;
    }

    @Override
    public int getProtocolVersion() {
    return 0;
    }

    @Override
    public java.net.InetSocketAddress getVirtualHost() {
    return null;
    }


    @Override public int getDeathScreenScore() { return 0; }
    @Override public void setDeathScreenScore(int score) {}
    @Override public org.bukkit.Input getCurrentInput() { return null; }
    @Override public java.util.Collection<org.bukkit.entity.EnderPearl> getEnderPearls() { return java.util.List.of(); }
    @Override public int getPlayerListOrder() { return 0; }
    @Override public void setPlayerListOrder(int order) {}
    @Override public io.papermc.paper.entity.PlayerGiveResult give(java.util.Collection<org.bukkit.inventory.ItemStack> items, boolean dropIfFull) {
        return new io.papermc.paper.entity.PlayerGiveResult() {
            public java.util.Collection<org.bukkit.inventory.ItemStack> leftovers() { return java.util.List.of(); }
            public java.util.Collection<org.bukkit.entity.Item> drops() { return java.util.List.of(); }
        };
    }

    @Override public org.bukkit.entity.Item dropItem(org.bukkit.inventory.ItemStack stack, boolean throwRandomly, java.util.function.Consumer<org.bukkit.entity.Item> cb) { return null; }
    @Override public org.bukkit.entity.Item dropItem(int slot, int amount, boolean throwRandomly, java.util.function.Consumer<org.bukkit.entity.Item> cb) { return null; }
    @Override public org.bukkit.entity.Item dropItem(org.bukkit.inventory.EquipmentSlot slot, int amount, boolean throwRandomly, java.util.function.Consumer<org.bukkit.entity.Item> cb) { return null; }

    @Override public org.bukkit.Location getPotentialRespawnLocation() { return getLocation(); }
    @Override public void setCooldown(org.bukkit.inventory.ItemStack item, int ticks) {}
    @Override public int getCooldown(org.bukkit.inventory.ItemStack item) { return 0; }
    @Override public boolean hasCooldown(org.bukkit.inventory.ItemStack item) { return false; }
}
