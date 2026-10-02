package io.github.lavacore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LavaBlockBridge implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Handle block break events
    }
}
