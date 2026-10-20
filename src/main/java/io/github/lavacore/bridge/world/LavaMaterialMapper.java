/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
// ============================================================================
// ============================================================================
package io.github.lavacore.bridge.world;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;

import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Maps a native Minecraft block to its Bukkit Material.
 *
 * The whole registry is walked once, at class-load, instead of doing a
 * registry-key lookup + string-normalize + Material.matchMaterial call on
 * every single getType() query. NMS Block instances are registry singletons
 * (there's exactly one Blocks.STONE shared everywhere), so caching by
 * identity is safe, and turns every later lookup into a single hashmap get.
 *
 * Building the table eagerly also means any block that fails to auto-map is
 * discovered and logged once at startup, rather than surfacing later as a
 * runtime exception the first time some plugin happens to touch that block.
 */
public final class LavaMaterialMapper {

    private static final Logger LOGGER = Logger.getLogger("Lava");

    private static final Map<Block, Material> CACHE = buildCache();

    private LavaMaterialMapper() {}

    private static Map<Block, Material> buildCache() {
        Map<Block, Material> cache = new IdentityHashMap<>();
        int unmapped = 0;

        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
            Material material = Material.matchMaterial(key.getPath().toUpperCase(Locale.ROOT));
            if (material != null) {
                cache.put(block, material);
            } else {
                unmapped++;
            }
        }

        if (unmapped > 0) {
            // Not fatal -- a handful of blocks won't line up automatically
            // (renamed on one side or the other across versions). Log it so
            // it's visible at boot instead of surfacing as a surprise NPE
            // deep in some plugin's block-handling code later.
            LOGGER.warning("[Lava] " + unmapped + " block(s) had no automatic Bukkit Material match. "
                    + "Add explicit overrides here if plugins report errors on unusual blocks.");
        }

        return cache;
    }

    public static Material toBukkit(Block nmsBlock) {
        Material material = CACHE.get(nmsBlock);
        if (material == null) {
            throw new IllegalStateException(
                    "No Bukkit Material mapping for block '"
                            + BuiltInRegistries.BLOCK.getKey(nmsBlock) + "' -- add an explicit override.");
        }
        return material;
    }
}
