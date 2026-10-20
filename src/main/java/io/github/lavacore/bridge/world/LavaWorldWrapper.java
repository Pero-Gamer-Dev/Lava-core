/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.bridge.world;
import java.util.Map;
import java.util.Set;
import java.util.Optional;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.ops.DirectWorldOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adapts a live Mojang-mapped Level onto org.bukkit.World.
 */
public class LavaWorldWrapper implements org.bukkit.World {

    private final Level level;
    private final String name;
    private final UUID uid;

    private LavaWorldWrapper(Level level) {
        this.level = level;
        this.name = level.dimension().location().toString();
        this.uid = UUID.nameUUIDFromBytes(this.name.getBytes(StandardCharsets.UTF_8));
    }

    public static LavaWorldWrapper of(Level level) {
        LavaWorldHolder holder = (LavaWorldHolder) level;
        LavaWorldWrapper existing = holder.lava$getBukkitWorld();
        if (existing != null) {
            return existing;
        }
        LavaWorldWrapper created = new LavaWorldWrapper(level);
        holder.lava$setBukkitWorld(created);
        return created;
    }

    public Level getHandle() {
        return level;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return new LavaBlockWrapper(level, new BlockPos(x, y, z));
    }

    @Override
    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUID() {
        return uid;
    }

    @Override
    public List<Player> getPlayers() {
        LavaServer server = LavaServer.getInstance();
        if (server == null) {
            return List.of();
        }
        List<Player> out = new ArrayList<>();
        for (Player player : server.getOnlinePlayers()) {
            if (this.equals(player.getWorld())) {
                out.add(player);
            }
        }
        return out;
    }

    @Override
    public boolean isVoidDamageEnabled() {
    return false;
    }

    @Override
    public void setVoidDamageEnabled(boolean arg0) {
    // no-op stub
    }

    @Override
    public float getVoidDamageAmount() {
    return 0.0f;
    }

    @Override
    public void setVoidDamageAmount(float arg0) {
    // no-op stub
    }

    @Override
    public double getVoidDamageMinBuildHeightOffset() {
    return 0.0;
    }

    @Override
    public void setVoidDamageMinBuildHeightOffset(double arg0) {
    // no-op stub
    }

    @Override
    public int getEntityCount() {
    return 0;
    }

    @Override
    public int getTileEntityCount() {
    return 0;
    }

    @Override
    public int getTickableTileEntityCount() {
    return 0;
    }

    @Override
    public int getChunkCount() {
    return 0;
    }

    @Override
    public int getPlayerCount() {
    return 0;
    }

    @Override
    public boolean hasStructureAt(io.papermc.paper.math.Position arg0, org.bukkit.generator.structure.Structure arg1) {
    return false;
    }

    @Override
    public org.bukkit.block.Block getHighestBlockAt(int arg0, int arg1) {
    return null;
    }

    @Override
    public org.bukkit.block.Block getHighestBlockAt(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.Block getHighestBlockAt(int arg0, int arg1, org.bukkit.HeightMap arg2) {
    return null;
    }

    @Override
    public org.bukkit.block.Block getHighestBlockAt(org.bukkit.Location arg0, org.bukkit.HeightMap arg1) {
    return null;
    }

    @Override
    public org.bukkit.Chunk getChunkAt(int arg0, int arg1) {
    return null;
    }

    @Override
    public org.bukkit.Chunk getChunkAt(int arg0, int arg1, boolean arg2) {
    return null;
    }

    @Override
    public org.bukkit.Chunk getChunkAt(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.Chunk getChunkAt(org.bukkit.block.Block arg0) {
    return null;
    }

    @Override
    public boolean isChunkLoaded(org.bukkit.Chunk arg0) {
    return false;
    }

    @Override
    public org.bukkit.Chunk[] getLoadedChunks() {
    return new org.bukkit.Chunk[0];
    }

    @Override
    public void loadChunk(org.bukkit.Chunk arg0) {
    // no-op stub
    }

    @Override
    public boolean isChunkLoaded(int arg0, int arg1) {
    return false;
    }

    @Override
    public boolean isChunkGenerated(int arg0, int arg1) {
    return false;
    }

    @Override
    public boolean isChunkInUse(int arg0, int arg1) {
    return false;
    }

    @Override
    public void loadChunk(int arg0, int arg1) {
    // no-op stub
    }

    @Override
    public boolean loadChunk(int arg0, int arg1, boolean arg2) {
    return false;
    }

    @Override
    public boolean unloadChunk(org.bukkit.Chunk arg0) {
    return false;
    }

    @Override
    public boolean unloadChunk(int arg0, int arg1) {
    return false;
    }

    @Override
    public boolean unloadChunk(int arg0, int arg1, boolean arg2) {
    return false;
    }

    @Override
    public boolean unloadChunkRequest(int arg0, int arg1) {
    return false;
    }

    @Override
    public boolean regenerateChunk(int arg0, int arg1) {
    return false;
    }

    @Override
    public boolean refreshChunk(int arg0, int arg1) {
    return false;
    }

    @Override
    public java.util.Collection<org.bukkit.entity.Player> getPlayersSeeingChunk(org.bukkit.Chunk arg0) {
    return List.of();
    }

    @Override
    public java.util.Collection<org.bukkit.entity.Player> getPlayersSeeingChunk(int arg0, int arg1) {
    return List.of();
    }

    @Override
    public boolean isChunkForceLoaded(int arg0, int arg1) {
    return false;
    }

    @Override
    public void setChunkForceLoaded(int arg0, int arg1, boolean arg2) {
    // no-op stub
    }

    @Override
    public java.util.Collection<org.bukkit.Chunk> getForceLoadedChunks() {
    return List.of();
    }

    @Override
    public boolean addPluginChunkTicket(int arg0, int arg1, org.bukkit.plugin.Plugin arg2) {
    return false;
    }

    @Override
    public boolean removePluginChunkTicket(int arg0, int arg1, org.bukkit.plugin.Plugin arg2) {
    return false;
    }

    @Override
    public void removePluginChunkTickets(org.bukkit.plugin.Plugin arg0) {
    // no-op stub
    }

    @Override
    public java.util.Collection<org.bukkit.plugin.Plugin> getPluginChunkTickets(int arg0, int arg1) {
    return List.of();
    }

    @Override
    public java.util.Map<org.bukkit.plugin.Plugin, java.util.Collection<org.bukkit.Chunk>> getPluginChunkTickets() {
    return Map.of();
    }

    @Override
    public java.util.Collection<org.bukkit.Chunk> getIntersectingChunks(org.bukkit.util.BoundingBox arg0) {
    return List.of();
    }

    @Override
    public org.bukkit.entity.Item dropItem(org.bukkit.Location arg0, org.bukkit.inventory.ItemStack arg1) {
    return null;
    }

    @Override
    public org.bukkit.entity.Item dropItem(org.bukkit.Location arg0, org.bukkit.inventory.ItemStack arg1, java.util.function.Consumer<? super org.bukkit.entity.Item> arg2) {
    return null;
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(org.bukkit.Location arg0, org.bukkit.inventory.ItemStack arg1) {
    return null;
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(org.bukkit.Location arg0, org.bukkit.inventory.ItemStack arg1, java.util.function.Consumer<? super org.bukkit.entity.Item> arg2) {
    return null;
    }

    @Override
    public org.bukkit.entity.Arrow spawnArrow(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, float arg2, float arg3) {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.AbstractArrow> T spawnArrow(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, float arg2, float arg3, java.lang.Class<T> arg4) {
    return null;
    }

    @Override
    public boolean generateTree(org.bukkit.Location arg0, org.bukkit.TreeType arg1) {
    return false;
    }

    @Override
    public boolean generateTree(org.bukkit.Location arg0, org.bukkit.TreeType arg1, org.bukkit.BlockChangeDelegate arg2) {
    return false;
    }

    @Override
    public org.bukkit.entity.LightningStrike strikeLightning(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.entity.LightningStrike strikeLightningEffect(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.Location findLightningRod(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.Location findLightningTarget(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public java.util.List<org.bukkit.entity.Entity> getEntities() {
    return List.of();
    }

    @Override
    public java.util.List<org.bukkit.entity.LivingEntity> getLivingEntities() {
    return List.of();
    }

    @Override
    public <T extends org.bukkit.entity.Entity> java.util.Collection<T> getEntitiesByClass(java.lang.Class<T>... arg0) {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Entity> java.util.Collection<T> getEntitiesByClass(java.lang.Class<T> arg0) {
    return null;
    }

    @Override
    public java.util.Collection<org.bukkit.entity.Entity> getEntitiesByClasses(java.lang.Class<?>... arg0) {
    return List.of();
    }

    @Override
    public java.util.concurrent.CompletableFuture<org.bukkit.Chunk> getChunkAtAsync(int arg0, int arg1, boolean arg2, boolean arg3) {
    return CompletableFuture.completedFuture(null);
    }

    @Override
    public java.util.Collection<org.bukkit.entity.Entity> getNearbyEntities(org.bukkit.Location arg0, double arg1, double arg2, double arg3) {
    return List.of();
    }

    @Override
    public org.bukkit.entity.Entity getEntity(java.util.UUID arg0) {
    return null;
    }

    @Override
    public java.util.Collection<org.bukkit.entity.Entity> getNearbyEntities(org.bukkit.Location arg0, double arg1, double arg2, double arg3, java.util.function.Predicate<? super org.bukkit.entity.Entity> arg4) {
    return List.of();
    }

    @Override
    public java.util.Collection<org.bukkit.entity.Entity> getNearbyEntities(org.bukkit.util.BoundingBox arg0) {
    return List.of();
    }

    @Override
    public java.util.Collection<org.bukkit.entity.Entity> getNearbyEntities(org.bukkit.util.BoundingBox arg0, java.util.function.Predicate<? super org.bukkit.entity.Entity> arg1) {
    return List.of();
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceEntities(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceEntities(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2, double arg3) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceEntities(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2, java.util.function.Predicate<? super org.bukkit.entity.Entity> arg3) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceEntities(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2, double arg3, java.util.function.Predicate<? super org.bukkit.entity.Entity> arg4) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceEntities(io.papermc.paper.math.Position arg0, org.bukkit.util.Vector arg1, double arg2, double arg3, java.util.function.Predicate<? super org.bukkit.entity.Entity> arg4) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceBlocks(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceBlocks(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2, org.bukkit.FluidCollisionMode arg3) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceBlocks(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2, org.bukkit.FluidCollisionMode arg3, boolean arg4) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTraceBlocks(io.papermc.paper.math.Position arg0, org.bukkit.util.Vector arg1, double arg2, org.bukkit.FluidCollisionMode arg3, boolean arg4, java.util.function.Predicate<? super org.bukkit.block.Block> arg5) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTrace(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2, org.bukkit.FluidCollisionMode arg3, boolean arg4, double arg5, java.util.function.Predicate<? super org.bukkit.entity.Entity> arg6) {
    return null;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTrace(io.papermc.paper.math.Position arg0, org.bukkit.util.Vector arg1, double arg2, org.bukkit.FluidCollisionMode arg3, boolean arg4, double arg5, java.util.function.Predicate<? super org.bukkit.entity.Entity> arg6, java.util.function.Predicate<? super org.bukkit.block.Block> arg7) {
    return null;
    }

    @Override
    public org.bukkit.Location getSpawnLocation() {
        if (level instanceof ServerLevel sl) {
            BlockPos pos = DirectWorldOps.getSharedSpawnPos(sl);
            float angle = DirectWorldOps.getSharedSpawnAngle(sl);
            return new Location(this, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, angle, 0f);
        }
        return new Location(this, 0, 64, 0);
    }

    @Override
    public boolean setSpawnLocation(org.bukkit.Location arg0) {
    return false;
    }

    @Override
    public boolean setSpawnLocation(int arg0, int arg1, int arg2, float arg3) {
    return false;
    }

    @Override
    public boolean setSpawnLocation(int arg0, int arg1, int arg2) {
    return false;
    }

    @Override
    public long getTime() {
        return DirectWorldOps.getDayTime(level) % 24000L;
    }

    @Override
    public void setTime(long arg0) {
        if (level instanceof ServerLevel sl) {
            long full = DirectWorldOps.getGameTime(sl);
            long dayBase = full - (full % 24000L);
            DirectWorldOps.setDayTime(sl, dayBase + (arg0 % 24000L));
        }
    }

    @Override
    public long getFullTime() {
        return DirectWorldOps.getDayTime(level);
    }

    @Override
    public void setFullTime(long arg0) {
        if (level instanceof ServerLevel sl) {
            DirectWorldOps.setDayTime(sl, arg0);
        }
    }

    @Override
    public boolean isDayTime() {
    return false;
    }

    @Override
    public long getGameTime() {
        return DirectWorldOps.getGameTime(level);
    }

    @Override
    public boolean hasStorm() {
        return DirectWorldOps.isRaining(level);
    }

    @Override
    public void setStorm(boolean arg0) {
        if (level instanceof ServerLevel sl) {
            DirectWorldOps.setRaining(sl, arg0);
        }
    }

    @Override
    public int getWeatherDuration() {
    return 0;
    }

    @Override
    public void setWeatherDuration(int arg0) {
    // no-op stub
    }

    @Override
    public boolean isThundering() {
        return DirectWorldOps.isThundering(level);
    }

    @Override
    public void setThundering(boolean arg0) {
        if (level instanceof ServerLevel sl) {
            DirectWorldOps.setThundering(sl, arg0);
        }
    }

    @Override
    public int getThunderDuration() {
    return 0;
    }

    @Override
    public void setThunderDuration(int arg0) {
    // no-op stub
    }

    @Override
    public boolean isClearWeather() {
    return false;
    }

    @Override
    public void setClearWeatherDuration(int arg0) {
    // no-op stub
    }

    @Override
    public int getClearWeatherDuration() {
    return 0;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3) {
    return false;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4) {
    return false;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4, boolean arg5) {
    return false;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4, boolean arg5, org.bukkit.entity.Entity arg6) {
    return false;
    }

    @Override
    public boolean createExplosion(org.bukkit.Location arg0, float arg1) {
    return false;
    }

    @Override
    public boolean createExplosion(org.bukkit.Location arg0, float arg1, boolean arg2) {
    return false;
    }

    @Override
    public boolean createExplosion(org.bukkit.entity.Entity arg0, org.bukkit.Location arg1, float arg2, boolean arg3, boolean arg4, boolean arg5) {
    return false;
    }

    @Override
    public boolean createExplosion(org.bukkit.Location arg0, float arg1, boolean arg2, boolean arg3) {
    return false;
    }

    @Override
    public boolean createExplosion(org.bukkit.Location arg0, float arg1, boolean arg2, boolean arg3, org.bukkit.entity.Entity arg4) {
    return false;
    }

    @Override
    public boolean getPVP() {
    return false;
    }

    @Override
    public void setPVP(boolean arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.generator.ChunkGenerator getGenerator() {
    return null;
    }

    @Override
    public org.bukkit.generator.BiomeProvider getBiomeProvider() {
    return null;
    }

    @Override
    public void save() {
    // no-op stub
    }

    @Override
    public java.util.List<org.bukkit.generator.BlockPopulator> getPopulators() {
    return List.of();
    }

    @Override
    public <T extends org.bukkit.entity.LivingEntity> T spawn(org.bukkit.Location arg0, java.lang.Class<T> arg1, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason arg2, boolean arg3, java.util.function.Consumer<? super T> arg4) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public org.bukkit.entity.FallingBlock spawnFallingBlock(org.bukkit.Location arg0, org.bukkit.material.MaterialData arg1) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public org.bukkit.entity.FallingBlock spawnFallingBlock(org.bukkit.Location arg0, org.bukkit.block.data.BlockData arg1) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public org.bukkit.entity.FallingBlock spawnFallingBlock(org.bukkit.Location arg0, org.bukkit.Material arg1, byte arg2) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public void playEffect(org.bukkit.Location arg0, org.bukkit.Effect arg1, int arg2) {
    // no-op stub
    }

    @Override
    public void playEffect(org.bukkit.Location arg0, org.bukkit.Effect arg1, int arg2, int arg3) {
    // no-op stub
    }

    @Override
    public <T> void playEffect(org.bukkit.Location arg0, org.bukkit.Effect arg1, T arg2) {
    }

    @Override
    public <T> void playEffect(org.bukkit.Location arg0, org.bukkit.Effect arg1, T arg2, int arg3) {
    }

    @Override
    public org.bukkit.ChunkSnapshot getEmptyChunkSnapshot(int arg0, int arg1, boolean arg2, boolean arg3) {
    return null;
    }

    @Override
    public void setSpawnFlags(boolean arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public boolean getAllowAnimals() {
    return false;
    }

    @Override
    public boolean getAllowMonsters() {
    return false;
    }

    @Override
    public org.bukkit.block.Biome getBiome(int arg0, int arg1) {
    return null;
    }

    @Override
    public void setBiome(int arg0, int arg1, org.bukkit.block.Biome arg2) {
    // no-op stub
    }

    @Override
    public double getTemperature(int arg0, int arg1) {
    return 0.0;
    }

    @Override
    public double getTemperature(int arg0, int arg1, int arg2) {
    return 0.0;
    }

    @Override
    public double getHumidity(int arg0, int arg1) {
    return 0.0;
    }

    @Override
    public double getHumidity(int arg0, int arg1, int arg2) {
    return 0.0;
    }

    @Override
    public int getLogicalHeight() {
    return 0;
    }

    @Override
    public boolean isNatural() {
    return false;
    }

    @Override
    public boolean isBedWorks() {
    return false;
    }

    @Override
    public boolean hasSkyLight() {
    return false;
    }

    @Override
    public boolean hasCeiling() {
    return false;
    }

    @Override
    public boolean isPiglinSafe() {
    return false;
    }

    @Override
    public boolean isRespawnAnchorWorks() {
    return false;
    }

    @Override
    public boolean hasRaids() {
    return false;
    }

    @Override
    public boolean isUltraWarm() {
    return false;
    }

    @Override
    public int getSeaLevel() {
    return 0;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
    return false;
    }

    @Override
    public void setKeepSpawnInMemory(boolean arg0) {
    // no-op stub
    }

    @Override
    public boolean isAutoSave() {
    return false;
    }

    @Override
    public void setAutoSave(boolean arg0) {
    // no-op stub
    }

    @Override
    public void setDifficulty(org.bukkit.Difficulty arg0) {
    // no-op stub
    }

    @Override
    public org.bukkit.Difficulty getDifficulty() {
    return null;
    }

    @Override
    public int getViewDistance() {
    return 0;
    }

    @Override
    public int getSimulationDistance() {
    return 0;
    }

    @Override
    public java.io.File getWorldFolder() {
    return null;
    }

    @Override
    public org.bukkit.WorldType getWorldType() {
    return null;
    }

    @Override
    public boolean canGenerateStructures() {
    return false;
    }

    @Override
    public boolean isHardcore() {
    return false;
    }

    @Override
    public void setHardcore(boolean arg0) {
    // no-op stub
    }

    @Override
    public long getTicksPerAnimalSpawns() {
    return 0L;
    }

    @Override
    public void setTicksPerAnimalSpawns(int arg0) {
    // no-op stub
    }

    @Override
    public long getTicksPerMonsterSpawns() {
    return 0L;
    }

    @Override
    public void setTicksPerMonsterSpawns(int arg0) {
    // no-op stub
    }

    @Override
    public long getTicksPerWaterSpawns() {
    return 0L;
    }

    @Override
    public void setTicksPerWaterSpawns(int arg0) {
    // no-op stub
    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
    return 0L;
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int arg0) {
    // no-op stub
    }

    @Override
    public long getTicksPerWaterUndergroundCreatureSpawns() {
    return 0L;
    }

    @Override
    public void setTicksPerWaterUndergroundCreatureSpawns(int arg0) {
    // no-op stub
    }

    @Override
    public long getTicksPerAmbientSpawns() {
    return 0L;
    }

    @Override
    public void setTicksPerAmbientSpawns(int arg0) {
    // no-op stub
    }

    @Override
    public long getTicksPerSpawns(org.bukkit.entity.SpawnCategory arg0) {
    return 0L;
    }

    @Override
    public void setTicksPerSpawns(org.bukkit.entity.SpawnCategory arg0, int arg1) {
    // no-op stub
    }

    @Override
    public int getMonsterSpawnLimit() {
    return 0;
    }

    @Override
    public void setMonsterSpawnLimit(int arg0) {
    // no-op stub
    }

    @Override
    public int getAnimalSpawnLimit() {
    return 0;
    }

    @Override
    public void setAnimalSpawnLimit(int arg0) {
    // no-op stub
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
    return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int arg0) {
    // no-op stub
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
    return 0;
    }

    @Override
    public void setWaterUndergroundCreatureSpawnLimit(int arg0) {
    // no-op stub
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
    return 0;
    }

    @Override
    public void setWaterAmbientSpawnLimit(int arg0) {
    // no-op stub
    }

    @Override
    public int getAmbientSpawnLimit() {
    return 0;
    }

    @Override
    public void setAmbientSpawnLimit(int arg0) {
    // no-op stub
    }

    @Override
    public int getSpawnLimit(org.bukkit.entity.SpawnCategory arg0) {
    return 0;
    }

    @Override
    public void setSpawnLimit(org.bukkit.entity.SpawnCategory arg0, int arg1) {
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
    public java.lang.String[] getGameRules() {
    return new java.lang.String[0];
    }

    @Override
    public java.lang.String getGameRuleValue(java.lang.String arg0) {
    return "";
    }

    @Override
    public boolean setGameRuleValue(java.lang.String arg0, java.lang.String arg1) {
    return false;
    }

    @Override
    public boolean isGameRule(java.lang.String arg0) {
    return false;
    }

    @Override
    public <T> T getGameRuleValue(org.bukkit.GameRule<T> arg0) {
    return null;
    }

    @Override
    public <T> T getGameRuleDefault(org.bukkit.GameRule<T> arg0) {
    return null;
    }

    @Override
    public <T> boolean setGameRule(org.bukkit.GameRule<T> arg0, T arg1) {
    return false;
    }

    @Override
    public org.bukkit.WorldBorder getWorldBorder() {
    return null;
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
    public <T> void spawnParticle(org.bukkit.Particle arg0, java.util.List<org.bukkit.entity.Player> arg1, org.bukkit.entity.Player arg2, double arg3, double arg4, double arg5, int arg6, double arg7, double arg8, double arg9, double arg10, T arg11, boolean arg12) {
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, org.bukkit.Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, T arg7, boolean arg8) {
    }

    @Override
    public <T> void spawnParticle(org.bukkit.Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8, T arg9, boolean arg10) {
    }

    @Override
    public org.bukkit.Location locateNearestStructure(org.bukkit.Location arg0, org.bukkit.StructureType arg1, int arg2, boolean arg3) {
    return null;
    }

    @Override
    public org.bukkit.util.StructureSearchResult locateNearestStructure(org.bukkit.Location arg0, org.bukkit.generator.structure.StructureType arg1, int arg2, boolean arg3) {
    return null;
    }

    @Override
    public org.bukkit.util.StructureSearchResult locateNearestStructure(org.bukkit.Location arg0, org.bukkit.generator.structure.Structure arg1, int arg2, boolean arg3) {
    return null;
    }

    @Override
    public double getCoordinateScale() {
    return 0.0;
    }

    @Override
    public boolean isFixedTime() {
    return false;
    }

    @Override
    public java.util.Collection<org.bukkit.Material> getInfiniburn() {
    return List.of();
    }

    @Override
    public void sendGameEvent(org.bukkit.entity.Entity arg0, org.bukkit.GameEvent arg1, org.bukkit.util.Vector arg2) {
    // no-op stub
    }

    @Override
    public org.bukkit.World.Spigot spigot() {
    return null;
    }

    @Override
    public org.bukkit.util.BiomeSearchResult locateNearestBiome(org.bukkit.Location arg0, int arg1, org.bukkit.block.Biome... arg2) {
    return null;
    }

    @Override
    public org.bukkit.util.BiomeSearchResult locateNearestBiome(org.bukkit.Location arg0, int arg1, int arg2, int arg3, org.bukkit.block.Biome... arg4) {
    return null;
    }

    @Override
    public org.bukkit.Raid locateNearestRaid(org.bukkit.Location arg0, int arg1) {
    return null;
    }

    @Override
    public org.bukkit.Raid getRaid(int arg0) {
    return null;
    }

    @Override
    public java.util.List<org.bukkit.Raid> getRaids() {
    return List.of();
    }

    @Override
    public org.bukkit.boss.DragonBattle getEnderDragonBattle() {
    return null;
    }

    @Override
    public java.util.Set<org.bukkit.FeatureFlag> getFeatureFlags() {
    return Set.of();
    }

    @Override
    public void setViewDistance(int arg0) {
    // no-op stub
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
    public java.util.Collection<org.bukkit.generator.structure.GeneratedStructure> getStructures(int arg0, int arg1) {
    return List.of();
    }

    @Override
    public java.util.Collection<org.bukkit.generator.structure.GeneratedStructure> getStructures(int arg0, int arg1, org.bukkit.generator.structure.Structure arg2) {
    return List.of();
    }

    @Override
    public org.bukkit.block.Biome getBiome(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.Biome getBiome(int arg0, int arg1, int arg2) {
    return null;
    }

    @Override
    public org.bukkit.block.Biome getComputedBiome(int arg0, int arg1, int arg2) {
    return null;
    }

    @Override
    public void setBiome(org.bukkit.Location arg0, org.bukkit.block.Biome arg1) {
    // no-op stub
    }

    @Override
    public void setBiome(int arg0, int arg1, int arg2, org.bukkit.block.Biome arg3) {
    // no-op stub
    }

    @Override
    public org.bukkit.block.BlockState getBlockState(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.BlockState getBlockState(int arg0, int arg1, int arg2) {
    return null;
    }

    @Override
    public io.papermc.paper.block.fluid.FluidData getFluidData(int arg0, int arg1, int arg2) {
    return null;
    }

    @Override
    public org.bukkit.block.data.BlockData getBlockData(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.data.BlockData getBlockData(int arg0, int arg1, int arg2) {
    return null;
    }

    @Override
    public org.bukkit.Material getType(org.bukkit.Location arg0) {
    return null;
    }

    @Override
    public org.bukkit.Material getType(int arg0, int arg1, int arg2) {
    return null;
    }

    @Override
    public void setBlockData(org.bukkit.Location arg0, org.bukkit.block.data.BlockData arg1) {
    // no-op stub
    }

    @Override
    public void setBlockData(int arg0, int arg1, int arg2, org.bukkit.block.data.BlockData arg3) {
    // no-op stub
    }

    @Override
    public void setType(org.bukkit.Location arg0, org.bukkit.Material arg1) {
    // no-op stub
    }

    @Override
    public void setType(int arg0, int arg1, int arg2, org.bukkit.Material arg3) {
    // no-op stub
    }

    @Override
    public boolean generateTree(org.bukkit.Location arg0, java.util.Random arg1, org.bukkit.TreeType arg2) {
    return false;
    }

    @Override
    public boolean generateTree(org.bukkit.Location arg0, java.util.Random arg1, org.bukkit.TreeType arg2, java.util.function.Consumer<? super org.bukkit.block.BlockState> arg3) {
    return false;
    }

    @Override
    public boolean generateTree(org.bukkit.Location arg0, java.util.Random arg1, org.bukkit.TreeType arg2, java.util.function.Predicate<? super org.bukkit.block.BlockState> arg3) {
    return false;
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(org.bukkit.Location arg0, org.bukkit.entity.EntityType arg1) {
    return null;
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(org.bukkit.Location arg0, org.bukkit.entity.EntityType arg1, boolean arg2) {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Entity> T createEntity(org.bukkit.Location arg0, java.lang.Class<T> arg1) {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Entity> T spawn(org.bukkit.Location arg0, java.lang.Class<T> arg1) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Entity> T spawn(org.bukkit.Location arg0, java.lang.Class<T> arg1, java.util.function.Consumer<? super T> arg2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason arg3) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public <T extends org.bukkit.entity.Entity> T spawn(org.bukkit.Location arg0, java.lang.Class<T> arg1, boolean arg2, java.util.function.Consumer<? super T> arg3) throws java.lang.IllegalArgumentException {
    return null;
    }

    @Override
    public int getHighestBlockYAt(int arg0, int arg1) {
    return 0;
    }

    @Override
    public int getHighestBlockYAt(org.bukkit.Location arg0) {
    return 0;
    }

    @Override
    public int getHighestBlockYAt(int arg0, int arg1, org.bukkit.HeightMap arg2) {
    return 0;
    }

    @Override
    public int getHighestBlockYAt(org.bukkit.Location arg0, org.bukkit.HeightMap arg1) {
    return 0;
    }

    @Override
    public <T extends org.bukkit.entity.Entity> T addEntity(T arg0) {
    return null;
    }

    @Override
    public io.papermc.paper.world.MoonPhase getMoonPhase() {
    return null;
    }

    @Override
    public org.bukkit.NamespacedKey getKey() {
    return null;
    }

    @Override
    public boolean lineOfSightExists(org.bukkit.Location arg0, org.bukkit.Location arg1) {
    return false;
    }

    @Override
    public boolean hasCollisionsIn(org.bukkit.util.BoundingBox arg0) {
    return false;
    }

    @Override
    public net.kyori.adventure.key.Key key() {
    return null;
    }

    @Override
    public org.bukkit.World.Environment getEnvironment() {
    return null;
    }

    @Override
    public long getSeed() {
    return 0L;
    }

    @Override
    public int getMinHeight() {
        return DirectWorldOps.getMinBuildHeight(level);
    }

    @Override
    public int getMaxHeight() {
        return DirectWorldOps.getMaxBuildHeight(level);
    }

    @Override
    public org.bukkit.generator.BiomeProvider vanillaBiomeProvider() {
    return null;
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
    public org.bukkit.persistence.PersistentDataContainer getPersistentDataContainer() {
    return null;
    }

    @Override
    public java.lang.Iterable<? extends net.kyori.adventure.audience.Audience> audiences() {
    return List.of();
    }


    @Override public void save(boolean flush) {
        if (level instanceof net.minecraft.server.level.ServerLevel sl) sl.save(null, flush, false);
    }
    @Override public org.bukkit.util.RayTraceResult rayTrace(java.util.function.Consumer<io.papermc.paper.raytracing.PositionedRayTraceConfigurationBuilder> c) { return null; }
    @Override public void getChunksAtAsync(int a,int b,int c,int d,boolean gen, Runnable cb) { if (cb!=null) cb.run(); }

    @Override public void getChunkAtAsync(int x, int z, boolean gen, boolean urgent, java.util.function.Consumer<? super org.bukkit.Chunk> cb) {
        if (cb != null) cb.accept(getChunkAt(x, z));
    }

}
