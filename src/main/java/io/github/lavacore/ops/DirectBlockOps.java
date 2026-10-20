/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.ops;

import io.github.lavacore.performance.LavaMetrics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Direct block operations against a Level + BlockPos.
 */
public final class DirectBlockOps {
    private DirectBlockOps() {}

    public static BlockState getState(Level level, BlockPos pos) {
        LavaMetrics.recordDirect("BLOCK_LOOKUP");
        return level.getBlockState(pos);
    }

    public static Block getBlock(Level level, BlockPos pos) {
        LavaMetrics.recordDirect("BLOCK_LOOKUP");
        return level.getBlockState(pos).getBlock();
    }

    public static boolean setState(Level level, BlockPos pos, BlockState state, int flags) {
        LavaMetrics.recordDirect("BLOCK_SET");
        return level.setBlock(pos, state, flags);
    }

    public static boolean setState(Level level, BlockPos pos, BlockState state) {
        return setState(level, pos, state, 3);
    }

    public static boolean isAir(Level level, BlockPos pos) {
        LavaMetrics.recordDirect("BLOCK_LOOKUP");
        return level.getBlockState(pos).isAir();
    }
}
