package io.github.lavacore.bridge.world;

import org.bukkit.block.Block;

public class LavaBlockWrapper {
    private final Block block;

    public LavaBlockWrapper(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
