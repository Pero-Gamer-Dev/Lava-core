/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.bridge;
import java.util.Set;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.kyori.adventure.text.Component;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import io.github.lavacore.bridge.entity.LavaPlayerWrapper;
import io.github.lavacore.bridge.world.LavaWorldWrapper;
import io.github.lavacore.inventory.LavaItemFactory;
import io.github.lavacore.plugin.LavaPluginManager;
import io.github.lavacore.runtime.RuntimeContext;
import io.github.lavacore.scheduler.LavaScheduler;
import io.github.lavacore.unsafe.LavaUnsafeValues;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.messaging.StandardMessenger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Minimal org.bukkit.Server bound to a live DedicatedServer.
 * This is the missing singleton the block-break slice needs so
 * Bukkit.getPlayer / Bukkit.getPluginManager resolve.
 */
public class LavaServer implements org.bukkit.Server {
    private final org.bukkit.Server.Spigot spigot = new org.bukkit.Server.Spigot() {};

    private static LavaServer INSTANCE;

    private final DedicatedServer vanilla;
    private final Logger logger = Logger.getLogger("Lava");
    private final LavaPluginManager pluginManager;
    private final SimpleCommandMap commandMap;
    private final RuntimeContext runtime;
    private final File pluginsFolder;
    private final File worldContainer;
    private final LavaConsoleSender console = new LavaConsoleSender(this);
    private final LavaScheduler scheduler = new LavaScheduler();
    private final SimpleServicesManager services = new SimpleServicesManager();
    private final StandardMessenger messenger = new StandardMessenger();
    private final LavaItemFactory itemFactory = new LavaItemFactory();
    private final LavaUnsafeValues unsafe = new LavaUnsafeValues();

    public LavaServer(DedicatedServer vanilla) {
        this.vanilla = vanilla;
        this.pluginsFolder = new File("plugins");
        this.worldContainer = new File(".");
        this.commandMap = new SimpleCommandMap(this, new ConcurrentHashMap<>());
        this.pluginManager = new LavaPluginManager(this);
        this.runtime = RuntimeContext.create();
        this.runtime.setScheduler(scheduler);
    }

    public static LavaServer getInstance() {
        return INSTANCE;
    }

    public static LavaServer bind(DedicatedServer vanilla) {
        LavaServer server = new org.bukkit.craftbukkit.v1_21_R3.CraftServer(vanilla);
        INSTANCE = server;
        io.github.lavacore.compatibility.LavaServerBuildInfo.installAndSetServer(server);
        // Sync worlds into registry
        if (server.runtime != null) {
            server.runtime.worlds().syncFrom(vanilla.getAllLevels());
        }
        return server;
    }

    public DedicatedServer getHandle() {
        return vanilla;
    }

    public void addPlayer(ServerPlayer nms) {
        runtime.players().register(nms);
    }

    public void removePlayer(UUID id) {
        runtime.players().unregister(id);
        runtime.entities().unregister(id);
    }

    public RuntimeContext getRuntime() {
        return runtime;
    }

    @Override
    public org.bukkit.Server.Spigot spigot() {
        return spigot;
    }

    @Override
    public org.bukkit.UnsafeValues getUnsafe() {
        return unsafe;
    }

    @Override
    public String getName() {
        return "Lava";
    }

    @Override
    public String getVersion() {
        return "1.0.7-BETA (MC: 1.21.4)";
    }

    @Override
    public String getBukkitVersion() {
        return "1.21.4-R0.1-SNAPSHOT";
    }

    @Override
    public String getMinecraftVersion() {
        return "1.21.4";
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public org.bukkit.command.CommandMap getCommandMap() {
        return commandMap;
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return Collections.unmodifiableCollection(runtime.players().all());
    }

    @Override
    public Player getPlayer(UUID id) {
        return runtime.players().get(id);
    }

    @Override
    public Player getPlayer(String name) {
        return runtime.players().get(name);
    }

    @Override
    public Player getPlayerExact(String name) {
        return runtime.players().getExact(name);
    }

    @Override
    public List<Player> matchPlayer(String name) {
        if (name == null) {
            return List.of();
        }
        String lower = name.toLowerCase(Locale.ROOT);
        List<Player> out = new ArrayList<>();
        for (Player player : runtime.players().all()) {
            if (player.getName().toLowerCase(Locale.ROOT).contains(lower)) {
                out.add(player);
            }
        }
        return out;
    }

    @Override
    public int getMaxPlayers() {
        return vanilla.getMaxPlayers();
    }

    @Override
    public int getPort() {
        return vanilla.getPort();
    }

    @Override
    public int getViewDistance() {
        return vanilla.getPlayerList().getViewDistance();
    }

    @Override
    public int getSimulationDistance() {
        return vanilla.getPlayerList().getSimulationDistance();
    }

    @Override
    public String getIp() {
        String ip = vanilla.getLocalIp();
        return ip == null ? "" : ip;
    }

    @Override
    public List<World> getWorlds() {
        runtime.worlds().syncFrom(vanilla.getAllLevels());
        return runtime.worlds().snapshot();
    }

    @Override
    public World getWorld(String name) {
        if (name == null) {
            return null;
        }
        for (World world : getWorlds()) {
            if (world.getName().equalsIgnoreCase(name) || world.getName().endsWith(name)) {
                return world;
            }
        }
        return null;
    }

    @Override
    public World getWorld(UUID uid) {
        if (uid == null) {
            return null;
        }
        for (World world : getWorlds()) {
            if (uid.equals(world.getUID())) {
                return world;
            }
        }
        return null;
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return console;
    }

    @Override
    public void shutdown() {
        vanilla.halt(false);
    }

    @Override
    public boolean getOnlineMode() {
        return vanilla.usesAuthentication();
    }

    @Override
    public boolean getAllowEnd() {
        return true;
    }

    @Override
    public boolean getAllowNether() {
        return true;
    }

    @Override
    public boolean getAllowFlight() {
        return true;
    }

    @Override
    public boolean hasWhitelist() {
        return vanilla.getPlayerList().isUsingWhitelist();
    }

    @Override
    public boolean isHardcore() {
        return vanilla.isHardcore();
    }

    @Override
    public String getMotd() {
        return vanilla.getMotd();
    }

    @Override
    public Component motd() {
        return Component.text(getMotd() == null ? "" : getMotd());
    }

    @Override
    public File getPluginsFolder() {
        return pluginsFolder;
    }

    @Override
    public File getWorldContainer() {
        return worldContainer;
    }

    @Override
    public String getUpdateFolder() {
        return "update";
    }

    @Override
    public File getUpdateFolderFile() {
        return new File(pluginsFolder, "update");
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        return commandMap.dispatch(sender, commandLine);
    }

    @Override
    public boolean isPrimaryThread() {
        return vanilla.isSameThread();
    }

    @Override
    public int getMaxWorldSize() {
        return 29999984;
    }

    @Override
    public int getSpawnRadius() {
        return 16;
    }

    @Override
    public int broadcast(Component message) {
        int count = 0;
        for (Player player : runtime.players().all()) {
            player.sendMessage(message);
            count++;
        }
        logger.info(net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(message));
        return count;
    }

    @Override
    public int broadcastMessage(String message) {
        return broadcast(Component.text(message == null ? "" : message));
    }

    @Override
    public void setMaxPlayers(int arg0) {
    // no-op stub
    }

    @Override
    public java.lang.String getWorldType() {
    return "";
    }

    @Override
    public boolean getGenerateStructures() {
    return false;
    }

    @Override
    public boolean isLoggingIPs() {
    return false;
    }

    @Override
    public java.util.List<java.lang.String> getInitialEnabledPacks() {
    return List.of();
    }

    @Override
    public java.util.List<java.lang.String> getInitialDisabledPacks() {
    return List.of();
    }

    @Override
    public org.bukkit.packs.DataPackManager getDataPackManager() {
    return null;
    }

    @Override
    public org.bukkit.ServerTickManager getServerTickManager() {
    return null;
    }

    @Override
    public org.bukkit.packs.ResourcePack getServerResourcePack() {
    return null;
    }

    @Override
    public java.lang.String getResourcePack() {
    return "";
    }

    @Override
    public java.lang.String getResourcePackHash() {
    return "";
    }

    @Override
    public java.lang.String getResourcePackPrompt() {
    return "";
    }

    @Override
    public boolean isResourcePackRequired() {
    return false;
    }

    @Override
    public void setWhitelist(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isWhitelistEnforced() {
    return false;
    }

    @Override
    public void setWhitelistEnforced(boolean arg0) {
    // no-op stub
    }

    @Override
    public java.util.Set<org.bukkit.OfflinePlayer> getWhitelistedPlayers() {
    return Set.of();
    }

    @Override
    public void reloadWhitelist() {
    // no-op stub
    }

    @Override
    public long getConnectionThrottle() {
    return 0L;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
    return 0;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
    return 0;
    }

    @Override
    public int getTicksPerWaterSpawns() {
    return 0;
    }

    @Override
    public int getTicksPerWaterAmbientSpawns() {
    return 0;
    }

    @Override
    public int getTicksPerWaterUndergroundCreatureSpawns() {
    return 0;
    }

    @Override
    public int getTicksPerAmbientSpawns() {
    return 0;
    }

    @Override
    public int getTicksPerSpawns(org.bukkit.entity.SpawnCategory arg0) {
    return 0;
    }

    @Override
    public java.util.UUID getPlayerUniqueId(java.lang.String arg0) {
    return null;
    }

    @Override
    public boolean isTickingWorlds() {
    return false;
    }

    @Override
    public org.bukkit.World createWorld(org.bukkit.WorldCreator arg0) {
    return null;
    }

    @Override
    public boolean unloadWorld(java.lang.String arg0, boolean arg1) {
    return false;
    }

    @Override
    public boolean unloadWorld(org.bukkit.World arg0, boolean arg1) {
    return false;
    }

    @Override
    public org.bukkit.World getWorld(net.kyori.adventure.key.Key arg0) {
    return null;
    }

    @Override
    public org.bukkit.WorldBorder createWorldBorder() {
    return null;
    }

    @Override
    public org.bukkit.map.MapView getMap(int arg0) {
    return null;
    }

    @Override
    public org.bukkit.map.MapView createMap(org.bukkit.World arg0) {
    return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack createExplorerMap(org.bukkit.World arg0, org.bukkit.Location arg1, org.bukkit.StructureType arg2) {
    return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack createExplorerMap(org.bukkit.World arg0, org.bukkit.Location arg1, org.bukkit.StructureType arg2, int arg3, boolean arg4) {
    return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack createExplorerMap(org.bukkit.World arg0, org.bukkit.Location arg1, org.bukkit.generator.structure.StructureType arg2, org.bukkit.map.MapCursor.Type arg3, int arg4, boolean arg5) {
    return null;
    }

    @Override
    public void reload() {
    // no-op stub
    }

    @Override
    public void reloadData() {
    // no-op stub
    }

    @Override
    public void updateResources() {
    // no-op stub
    }

    @Override
    public void updateRecipes() {
    // no-op stub
    }

    @Override
    public org.bukkit.command.PluginCommand getPluginCommand(java.lang.String arg0) {
    return null;
    }

    @Override
    public void savePlayers() {
    // no-op stub
    }

    @Override
    public boolean addRecipe(org.bukkit.inventory.Recipe arg0) {
    return false;
    }

    @Override
    public boolean addRecipe(org.bukkit.inventory.Recipe arg0, boolean arg1) {
    return false;
    }

    @Override
    public java.util.List<org.bukkit.inventory.Recipe> getRecipesFor(org.bukkit.inventory.ItemStack arg0) {
    return List.of();
    }

    @Override
    public org.bukkit.inventory.Recipe getRecipe(org.bukkit.NamespacedKey arg0) {
    return null;
    }

    @Override
    public org.bukkit.inventory.Recipe getCraftingRecipe(org.bukkit.inventory.ItemStack[] arg0, org.bukkit.World arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack craftItem(org.bukkit.inventory.ItemStack[] arg0, org.bukkit.World arg1, org.bukkit.entity.Player arg2) {
    return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack craftItem(org.bukkit.inventory.ItemStack[] arg0, org.bukkit.World arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.ItemCraftResult craftItemResult(org.bukkit.inventory.ItemStack[] arg0, org.bukkit.World arg1, org.bukkit.entity.Player arg2) {
    return null;
    }

    @Override
    public org.bukkit.inventory.ItemCraftResult craftItemResult(org.bukkit.inventory.ItemStack[] arg0, org.bukkit.World arg1) {
    return null;
    }

    @Override
    public java.util.Iterator<org.bukkit.inventory.Recipe> recipeIterator() {
    return null;
    }

    @Override
    public void clearRecipes() {
    // no-op stub
    }

    @Override
    public void resetRecipes() {
    // no-op stub
    }

    @Override
    public boolean removeRecipe(org.bukkit.NamespacedKey arg0) {
    return false;
    }

    @Override
    public boolean removeRecipe(org.bukkit.NamespacedKey arg0, boolean arg1) {
    return false;
    }

    @Override
    public java.util.Map<java.lang.String, java.lang.String[]> getCommandAliases() {
    return Map.of();
    }

    @Override
    public void setSpawnRadius(int arg0) {
    // no-op stub
    }

    @Override
    public boolean shouldSendChatPreviews() {
    return false;
    }

    @Override
    public boolean isEnforcingSecureProfiles() {
    return false;
    }

    @Override
    public boolean isAcceptingTransfers() {
    return false;
    }

    @Override
    public boolean getHideOnlinePlayers() {
    return false;
    }

    @Override
    public int broadcast(java.lang.String arg0, java.lang.String arg1) {
    return 0;
    }

    @Override
    public int broadcast(net.kyori.adventure.text.Component arg0, java.lang.String arg1) {
    return 0;
    }

    @Override
    public org.bukkit.OfflinePlayer getOfflinePlayer(java.lang.String arg0) {
    return null;
    }

    @Override
    public org.bukkit.OfflinePlayer getOfflinePlayerIfCached(java.lang.String arg0) {
    return null;
    }

    @Override
    public org.bukkit.OfflinePlayer getOfflinePlayer(java.util.UUID arg0) {
    return null;
    }

    @Override
    public org.bukkit.profile.PlayerProfile createPlayerProfile(java.util.UUID arg0, java.lang.String arg1) {
    return null;
    }

    @Override
    public org.bukkit.profile.PlayerProfile createPlayerProfile(java.util.UUID arg0) {
    return null;
    }

    @Override
    public org.bukkit.profile.PlayerProfile createPlayerProfile(java.lang.String arg0) {
    return null;
    }

    @Override
    public java.util.Set<java.lang.String> getIPBans() {
    return Set.of();
    }

    @Override
    public void banIP(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void unbanIP(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public void banIP(java.net.InetAddress arg0) {
    // no-op stub
    }

    @Override
    public void unbanIP(java.net.InetAddress arg0) {
    // no-op stub
    }

    @Override
    public java.util.Set<org.bukkit.OfflinePlayer> getBannedPlayers() {
    return Set.of();
    }

    @Override
    public <T extends org.bukkit.BanList<?>> T getBanList(org.bukkit.BanList.Type arg0) {
    return null;
    }

    @Override
    public <B extends org.bukkit.BanList<E>, E> B getBanList(io.papermc.paper.ban.BanListType<B> arg0) {
    return null;
    }

    @Override
    public java.util.Set<org.bukkit.OfflinePlayer> getOperators() {
    return Set.of();
    }

    @Override
    public org.bukkit.GameMode getDefaultGameMode() {
    return null;
    }

    @Override
    public void setDefaultGameMode(org.bukkit.GameMode arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.command.CommandSender createCommandSender(java.util.function.Consumer<? super net.kyori.adventure.text.Component> arg0) {
    return null;
    }

    @Override
    public org.bukkit.OfflinePlayer[] getOfflinePlayers() {
    return new org.bukkit.OfflinePlayer[0];
    }

    @Override
    public org.bukkit.help.HelpMap getHelpMap() {
    return null;
    }

    @Override
    public org.bukkit.inventory.Inventory createInventory(org.bukkit.inventory.InventoryHolder arg0, org.bukkit.event.inventory.InventoryType arg1) {
    return null;
    }

    @Override
    public org.bukkit.inventory.Inventory createInventory(org.bukkit.inventory.InventoryHolder arg0, org.bukkit.event.inventory.InventoryType arg1, net.kyori.adventure.text.Component arg2) {
    return null;
    }

    @Override
    public org.bukkit.inventory.Inventory createInventory(org.bukkit.inventory.InventoryHolder arg0, org.bukkit.event.inventory.InventoryType arg1, java.lang.String arg2) {
    return null;
    }

    @Override
    public org.bukkit.inventory.Inventory createInventory(org.bukkit.inventory.InventoryHolder arg0, int arg1) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public org.bukkit.inventory.Inventory createInventory(org.bukkit.inventory.InventoryHolder arg0, int arg1, net.kyori.adventure.text.Component arg2) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public org.bukkit.inventory.Inventory createInventory(org.bukkit.inventory.InventoryHolder arg0, int arg1, java.lang.String arg2) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public org.bukkit.inventory.Merchant createMerchant(net.kyori.adventure.text.Component arg0) {
    return null;
    }

    @Override
    public org.bukkit.inventory.Merchant createMerchant(java.lang.String arg0) {
    return null;
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
    return 0;
    }

    @Override
    public int getMonsterSpawnLimit() {
    return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
    return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
    return 0;
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
    return 0;
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
    return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
    return 0;
    }

    @Override
    public int getSpawnLimit(org.bukkit.entity.SpawnCategory arg0) {
    return 0;
    }

    @Override
    public void motd(net.kyori.adventure.text.Component arg0) {
    // no-op stub
    }

    @Override
    public net.kyori.adventure.text.Component shutdownMessage() {
    return Component.empty();
    }

    @Override
    public void setMotd(java.lang.String arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.ServerLinks getServerLinks() {
    return null;
    }

    @Override
    public java.lang.String getShutdownMessage() {
    return "";
    }

    @Override
    public org.bukkit.entity.EntityFactory getEntityFactory() {
    return null;
    }

    @Override
    public org.bukkit.scoreboard.Criteria getScoreboardCriteria(java.lang.String arg0) {
    return null;
    }

    @Override
    public org.bukkit.util.CachedServerIcon getServerIcon() {
    return null;
    }

    @Override
    public org.bukkit.util.CachedServerIcon loadServerIcon(java.io.File arg0) throws java.lang.IllegalArgumentException, java.lang.Exception {
    return null;
    }

    @Override
    public org.bukkit.util.CachedServerIcon loadServerIcon(java.awt.image.BufferedImage arg0) throws java.lang.IllegalArgumentException, java.lang.Exception {
    return null;
    }

    @Override
    public void setIdleTimeout(int arg0) {
    // no-op stub
    }

    @Override
    public int getIdleTimeout() {
    return 0;
    }

    @Override
    public org.bukkit.generator.ChunkGenerator.ChunkData createChunkData(org.bukkit.World arg0) {
    return null;
    }

    @Override
    public org.bukkit.boss.BossBar createBossBar(java.lang.String arg0, org.bukkit.boss.BarColor arg1, org.bukkit.boss.BarStyle arg2, org.bukkit.boss.BarFlag... arg3) {
    return null;
    }

    @Override
    public org.bukkit.boss.KeyedBossBar createBossBar(org.bukkit.NamespacedKey arg0, java.lang.String arg1, org.bukkit.boss.BarColor arg2, org.bukkit.boss.BarStyle arg3, org.bukkit.boss.BarFlag... arg4) {
    return null;
    }

    @Override
    public java.util.Iterator<org.bukkit.boss.KeyedBossBar> getBossBars() {
    return null;
    }

    @Override
    public org.bukkit.boss.KeyedBossBar getBossBar(org.bukkit.NamespacedKey arg0) {
    return null;
    }

    @Override
    public boolean removeBossBar(org.bukkit.NamespacedKey arg0) {
    return false;
    }

    @Override
    public org.bukkit.entity.Entity getEntity(java.util.UUID arg0) {
    return null;
    }

    @Override
    public double[] getTPS() {
    return new double[0];
    }

    @Override
    public long[] getTickTimes() {
    return new long[0];
    }

    @Override
    public double getAverageTickTime() {
    return 0.0;
    }

    @Override
    public org.bukkit.advancement.Advancement getAdvancement(org.bukkit.NamespacedKey arg0) {
    return null;
    }

    @Override
    public java.util.Iterator<org.bukkit.advancement.Advancement> advancementIterator() {
    return null;
    }

    @Override
    public org.bukkit.block.data.BlockData createBlockData(org.bukkit.Material arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.data.BlockData createBlockData(org.bukkit.Material arg0, java.util.function.Consumer<? super org.bukkit.block.data.BlockData> arg1) {
    return null;
    }

    @Override
    public org.bukkit.block.data.BlockData createBlockData(java.lang.String arg0) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public org.bukkit.block.data.BlockData createBlockData(org.bukkit.Material arg0, java.lang.String arg1) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public <T extends org.bukkit.Keyed> org.bukkit.Tag<T> getTag(java.lang.String arg0, org.bukkit.NamespacedKey arg1, java.lang.Class<T> arg2) {
    return null;
    }

    @Override
    public <T extends org.bukkit.Keyed> java.lang.Iterable<org.bukkit.Tag<T>> getTags(java.lang.String arg0, java.lang.Class<T> arg1) {
    return null;
    }

    @Override
    public java.util.List<org.bukkit.entity.Entity> selectEntities(org.bukkit.command.CommandSender arg0, java.lang.String arg1) throws java.lang.IllegalArgumentException {
    return List.of();
    }

    @Override
    public org.bukkit.structure.StructureManager getStructureManager() {
    return null;
    }

    @Override
    public <T extends org.bukkit.Keyed> org.bukkit.Registry<T> getRegistry(java.lang.Class<T> arg0) {
    return null;
    }

    @Override
    public void reloadPermissions() {
    // no-op stub
    }

    @Override
    public boolean reloadCommandAliases() {
    return false;
    }

    @Override
    public boolean suggestPlayerNamesWhenNullTabCompletions() {
    return false;
    }

    @Override
    public java.lang.String getPermissionMessage() {
    return "";
    }

    @Override
    public net.kyori.adventure.text.Component permissionMessage() {
    return Component.empty();
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfile(java.util.UUID arg0) {
    return null;
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfile(java.lang.String arg0) {
    return null;
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfile(java.util.UUID arg0, java.lang.String arg1) {
    return null;
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfileExact(java.util.UUID arg0, java.lang.String arg1) {
    return null;
    }

    @Override
    public int getCurrentTick() {
    return 0;
    }

    @Override
    public boolean isStopping() {
    return false;
    }

    @Override
    public com.destroystokyo.paper.entity.ai.MobGoals getMobGoals() {
    return null;
    }

    @Override
    public io.papermc.paper.datapack.DatapackManager getDatapackManager() {
    return null;
    }

    @Override
    public org.bukkit.potion.PotionBrewer getPotionBrewer() {
    return null;
    }

    @Override
    public io.papermc.paper.threadedregions.scheduler.RegionScheduler getRegionScheduler() {
    return null;
    }

    @Override
    public io.papermc.paper.threadedregions.scheduler.AsyncScheduler getAsyncScheduler() {
    return null;
    }

    @Override
    public io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler getGlobalRegionScheduler() {
    return null;
    }

    @Override
    public boolean isOwnedByCurrentRegion(org.bukkit.World arg0, io.papermc.paper.math.Position arg1) {
    return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(org.bukkit.World arg0, io.papermc.paper.math.Position arg1, int arg2) {
    return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(org.bukkit.Location arg0) {
    return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(org.bukkit.Location arg0, int arg1) {
    return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(org.bukkit.World arg0, int arg1, int arg2) {
    return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(org.bukkit.World arg0, int arg1, int arg2, int arg3) {
    return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(org.bukkit.entity.Entity arg0) {
    return false;
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
    public java.lang.Iterable<? extends net.kyori.adventure.audience.Audience> audiences() {
    return List.of();
    }

    @Override
    public org.bukkit.loot.LootTable getLootTable(org.bukkit.NamespacedKey key) {
        return null;
    }

    @Override
    public org.bukkit.inventory.ItemFactory getItemFactory() {
        return itemFactory;
    }

    @Override
    public org.bukkit.scheduler.BukkitScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public org.bukkit.plugin.messaging.Messenger getMessenger() {
        return messenger;
    }

    @Override
    public org.bukkit.plugin.ServicesManager getServicesManager() {
        return services;
    }

    @Override
    public org.bukkit.scoreboard.ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override
    public org.bukkit.Warning.WarningState getWarningState() {
        return org.bukkit.Warning.WarningState.DEFAULT;
    }



    @Override public void allowPausing(org.bukkit.plugin.Plugin plugin, boolean value) {}
    @Override public int getPauseWhenEmptyTime() { return 60; }
    @Override public void setPauseWhenEmptyTime(int seconds) {}
    @Override public boolean isPaused() { return false; }
    @Override public boolean isGlobalTickThread() { return true; }
    @Override public void restart() { vanilla.halt(false); }

    @Override public boolean isOwnedByCurrentRegion(org.bukkit.World w, int x, int y, int z, int sq) { return true; }
    @Override public org.bukkit.inventory.Merchant createMerchant() { return null; }

}
