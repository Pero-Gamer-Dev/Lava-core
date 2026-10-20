/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Holds tasks due at a specific tick. Pulse only drains the current tick's bucket.
 */
public final class TaskBucket {
    private final long tick;
    private final ConcurrentLinkedQueue<LavaTask> tasks = new ConcurrentLinkedQueue<>();

    public TaskBucket(long tick) {
        this.tick = tick;
    }

    public long tick() {
        return tick;
    }

    public void add(LavaTask task) {
        tasks.offer(task);
    }

    public List<LavaTask> drain() {
        List<LavaTask> out = new ArrayList<>();
        LavaTask t;
        while ((t = tasks.poll()) != null) {
            out.add(t);
        }
        return out;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
