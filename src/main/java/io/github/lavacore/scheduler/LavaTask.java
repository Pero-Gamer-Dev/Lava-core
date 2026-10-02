package io.github.lavacore.scheduler;

import org.bukkit.scheduler.BukkitTask;

public class LavaTask {
    private final BukkitTask task;

    public LavaTask(BukkitTask task) {
        this.task = task;
    }

    public BukkitTask getTask() {
        return task;
    }
}
