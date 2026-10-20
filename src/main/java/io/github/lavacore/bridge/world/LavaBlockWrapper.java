/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.bridge.world;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import io.github.lavacore.ops.DirectBlockOps;
import io.github.lavacore.runtime.RuntimeContext;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Live BlockPos + Level adapter for org.bukkit.block.Block.
 * Identity/location/type/relative methods are real; remaining methods are
 * compile-complete stubs that throw or return empty defaults.
 */
public class LavaBlockWrapper implements Block {

    private final Level level;
    private final BlockPos pos;

    public LavaBlockWrapper(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    private BlockState nmsState() {
        return DirectBlockOps.getState(level, pos);
    }

    @Override
    public org.bukkit.World getWorld() {
        RuntimeContext ctx = RuntimeContext.get();
        if (ctx != null) return ctx.worlds().get(level);
        return LavaWorldWrapper.of(level);
    }

    @Override
    public int getX() {
        return pos.getX();
    }

    @Override
    public int getY() {
        return pos.getY();
    }

    @Override
    public int getZ() {
        return pos.getZ();
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(pos.getX());
            loc.setY(pos.getY());
            loc.setZ(pos.getZ());
        }
        return loc;
    }

    @Override
    public Material getType() {
        return LavaMaterialMapper.toBukkit(nmsState().getBlock());
    }

    @Override
    public boolean isEmpty() {
        return nmsState().isAir();
    }

    @Override
    public Block getRelative(int modX, int modY, int modZ) {
        return new LavaBlockWrapper(level, pos.offset(modX, modY, modZ));
    }

    @Override
    public Block getRelative(BlockFace face) {
        return getRelative(face.getModX(), face.getModY(), face.getModZ());
    }

    @Override
    public Block getRelative(BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    @Override
    public void setType(Material material) {
        setType(material, true);
    }

    @Override
    public void setType(Material material, boolean applyPhysics) {
        if (material == null) {
            return;
        }
        ResourceLocation key = ResourceLocation.parse("minecraft:" + material.name().toLowerCase());
        net.minecraft.world.level.block.Block nms = BuiltInRegistries.BLOCK.getValue(key);
        if (nms == null) {
            return;
        }
        int flags = applyPhysics ? 3 : 2;
        level.setBlock(pos, nms.defaultBlockState(), flags);
    }

    @Override
    public byte getData() {
    return (byte) 0;
    }

    @Override
    public org.bukkit.block.data.BlockData getBlockData() {
    return null;
    }

    @Override
    public byte getLightLevel() {
    return (byte) 0;
    }

    @Override
    public byte getLightFromSky() {
    return (byte) 0;
    }

    @Override
    public byte getLightFromBlocks() {
    return (byte) 0;
    }

    @Override
    public boolean isValidTool(org.bukkit.inventory.ItemStack arg0) {
    return false;
    }

    @Override
    public org.bukkit.Chunk getChunk() {
    return null;
    }

    @Override
    public void setBlockData(org.bukkit.block.data.BlockData arg0) {
    // no-op stub
    }

    @Override
    public void setBlockData(org.bukkit.block.data.BlockData arg0, boolean arg1) {
    // no-op stub
    }

    @Override
    public org.bukkit.block.BlockFace getFace(org.bukkit.block.Block arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.BlockState getState() {
    return null;
    }

    @Override
    public org.bukkit.block.BlockState getState(boolean arg0) {
    return null;
    }

    @Override
    public org.bukkit.block.Biome getBiome() {
    return null;
    }

    @Override
    public org.bukkit.block.Biome getComputedBiome() {
    return null;
    }

    @Override
    public void setBiome(org.bukkit.block.Biome arg0) {
    // no-op stub
    }

    @Override
    public boolean isBlockPowered() {
    return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
    return false;
    }

    @Override
    public boolean isBlockFacePowered(org.bukkit.block.BlockFace arg0) {
    return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(org.bukkit.block.BlockFace arg0) {
    return false;
    }

    @Override
    public int getBlockPower(org.bukkit.block.BlockFace arg0) {
    return 0;
    }

    @Override
    public int getBlockPower() {
    return 0;
    }

    @Override
    public boolean isLiquid() {
    return false;
    }

    @Override
    public boolean isBuildable() {
    return false;
    }

    @Override
    public boolean isBurnable() {
    return false;
    }

    @Override
    public boolean isReplaceable() {
    return false;
    }

    @Override
    public boolean isSolid() {
    return false;
    }

    @Override
    public boolean isCollidable() {
    return false;
    }

    @Override
    public double getTemperature() {
    return 0.0;
    }

    @Override
    public double getHumidity() {
    return 0.0;
    }

    @Override
    public org.bukkit.block.PistonMoveReaction getPistonMoveReaction() {
    return null;
    }

    @Override
    public boolean breakNaturally() {
    return false;
    }

    @Override
    public boolean breakNaturally(org.bukkit.inventory.ItemStack arg0) {
    return false;
    }

    @Override
    public boolean breakNaturally(boolean arg0, boolean arg1) {
    return false;
    }

    @Override
    public boolean breakNaturally(org.bukkit.inventory.ItemStack arg0, boolean arg1, boolean arg2) {
    return false;
    }

    @Override
    public void tick() {
    // no-op stub
    }

    @Override
    public void fluidTick() {
    // no-op stub
    }

    @Override
    public void randomTick() {
    // no-op stub
    }

    @Override
    public boolean applyBoneMeal(org.bukkit.block.BlockFace arg0) {
    return false;
    }

    @Override
    public java.util.Collection<org.bukkit.inventory.ItemStack> getDrops() {
    return List.of();
    }

    @Override
    public java.util.Collection<org.bukkit.inventory.ItemStack> getDrops(org.bukkit.inventory.ItemStack arg0) {
    return List.of();
    }

    @Override
    public java.util.Collection<org.bukkit.inventory.ItemStack> getDrops(org.bukkit.inventory.ItemStack arg0, org.bukkit.entity.Entity arg1) {
    return List.of();
    }

    @Override
    public boolean isPreferredTool(org.bukkit.inventory.ItemStack arg0) {
    return false;
    }

    @Override
    public float getBreakSpeed(org.bukkit.entity.Player arg0) {
    return 0.0f;
    }

    @Override
    public boolean isPassable() {
    return false;
    }

    @Override
    public org.bukkit.util.RayTraceResult rayTrace(org.bukkit.Location arg0, org.bukkit.util.Vector arg1, double arg2, org.bukkit.FluidCollisionMode arg3) {
    return null;
    }

    @Override
    public org.bukkit.util.BoundingBox getBoundingBox() {
    return null;
    }

    @Override
    public org.bukkit.util.VoxelShape getCollisionShape() {
    return null;
    }

    @Override
    public boolean canPlace(org.bukkit.block.data.BlockData arg0) {
    return false;
    }

    @Override
    public com.destroystokyo.paper.block.BlockSoundGroup getSoundGroup() {
    return null;
    }

    @Override
    public org.bukkit.SoundGroup getBlockSoundGroup() {
    return null;
    }

    @Override
    public java.lang.String getTranslationKey() {
    return "";
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
    public java.lang.String translationKey() {
    return "";
    }

}
