/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.scheduler;

import io.github.lavacore.performance.LavaMetrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Scheduler 2.0 — TaskBucket for due-task lookup, separate async executor.
 */
public final class LavaScheduler implements BukkitScheduler {

    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, LavaTask> tasks = new ConcurrentHashMap<>();
    private final Map<Long, TaskBucket> buckets = new ConcurrentHashMap<>();
    private final ExecutorService async = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "lava-async-scheduler");
        t.setDaemon(true);
        return t;
    });
    private long currentTick = 0;

    public void pulse() {
        currentTick++;
        TaskBucket bucket = buckets.remove(currentTick);
        if (bucket == null) return;
        for (LavaTask task : bucket.drain()) {
            if (task.cancelled || !tasks.containsKey(task.id)) continue;
            LavaMetrics.recordSchedulerExec();
            if (task.sync) {
                try {
                    task.run();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else {
                async.execute(() -> {
                    try {
                        task.run();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            if (task.period > 0 && !task.cancelled) {
                task.nextTick = currentTick + task.period;
                scheduleInBucket(task);
            } else {
                tasks.remove(task.id);
            }
        }
    }

    private void scheduleInBucket(LavaTask task) {
        buckets.computeIfAbsent(task.nextTick, TaskBucket::new).add(task);
    }

    private BukkitTask sched(Plugin p, Runnable r, boolean sync, long delay, long period) {
        int id = nextId.getAndIncrement();
        long next = currentTick + Math.max(0, delay);
        LavaTask task = new LavaTask(id, p, sync, r, next, period);
        tasks.put(id, task);
        scheduleInBucket(task);
        return task;
    }

    @Override public int scheduleSyncDelayedTask(Plugin p, Runnable t, long d) { return sched(p, t, true, d, -1).getTaskId(); }
    @Override public int scheduleSyncDelayedTask(Plugin p, Runnable t) { return scheduleSyncDelayedTask(p, t, 0); }
    @Override public int scheduleSyncDelayedTask(Plugin p, BukkitRunnable t, long d) { return scheduleSyncDelayedTask(p, (Runnable) t, d); }
    @Override public int scheduleSyncDelayedTask(Plugin p, BukkitRunnable t) { return scheduleSyncDelayedTask(p, (Runnable) t); }
    @Override public int scheduleSyncRepeatingTask(Plugin p, Runnable t, long d, long per) { return sched(p, t, true, d, per).getTaskId(); }
    @Override public int scheduleSyncRepeatingTask(Plugin p, BukkitRunnable t, long d, long per) { return scheduleSyncRepeatingTask(p, (Runnable) t, d, per); }
    @Override public int scheduleAsyncDelayedTask(Plugin p, Runnable t, long d) { return sched(p, t, false, d, -1).getTaskId(); }
    @Override public int scheduleAsyncDelayedTask(Plugin p, Runnable t) { return scheduleAsyncDelayedTask(p, t, 0); }
    @Override public int scheduleAsyncRepeatingTask(Plugin p, Runnable t, long d, long per) { return sched(p, t, false, d, per).getTaskId(); }

    @Override public <T> java.util.concurrent.Future<T> callSyncMethod(Plugin p, java.util.concurrent.Callable<T> c) {
        java.util.concurrent.CompletableFuture<T> f = new java.util.concurrent.CompletableFuture<>();
        runTask(p, () -> {
            try { f.complete(c.call()); } catch (Exception e) { f.completeExceptionally(e); }
        });
        return f;
    }

    @Override public void cancelTask(int id) {
        LavaTask t = tasks.remove(id);
        if (t != null) t.cancel();
    }

    @Override public void cancelTasks(Plugin p) {
        Iterator<Map.Entry<Integer, LavaTask>> it = tasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, LavaTask> e = it.next();
            if (e.getValue().owner.equals(p)) {
                e.getValue().cancel();
                it.remove();
            }
        }
    }

    @Override public boolean isCurrentlyRunning(int id) { return false; }
    @Override public boolean isQueued(int id) { return tasks.containsKey(id); }
    @Override public List<BukkitWorker> getActiveWorkers() { return List.of(); }
    @Override public List<BukkitTask> getPendingTasks() { return new ArrayList<>(tasks.values()); }
    @Override public BukkitTask runTask(Plugin p, Runnable t) { return sched(p, t, true, 0, -1); }
    @Override public void runTask(Plugin p, Consumer<? super BukkitTask> t) { t.accept(sched(p, () -> {}, true, 0, -1)); }
    @Override public BukkitTask runTask(Plugin p, BukkitRunnable t) { return runTask(p, (Runnable) t); }
    @Override public BukkitTask runTaskAsynchronously(Plugin p, Runnable t) { return sched(p, t, false, 0, -1); }
    @Override public void runTaskAsynchronously(Plugin p, Consumer<? super BukkitTask> t) { t.accept(sched(p, () -> {}, false, 0, -1)); }
    @Override public BukkitTask runTaskAsynchronously(Plugin p, BukkitRunnable t) { return runTaskAsynchronously(p, (Runnable) t); }
    @Override public BukkitTask runTaskLater(Plugin p, Runnable t, long d) { return sched(p, t, true, d, -1); }
    @Override public void runTaskLater(Plugin p, Consumer<? super BukkitTask> t, long d) { t.accept(sched(p, () -> {}, true, d, -1)); }
    @Override public BukkitTask runTaskLater(Plugin p, BukkitRunnable t, long d) { return runTaskLater(p, (Runnable) t, d); }
    @Override public BukkitTask runTaskLaterAsynchronously(Plugin p, Runnable t, long d) { return sched(p, t, false, d, -1); }
    @Override public void runTaskLaterAsynchronously(Plugin p, Consumer<? super BukkitTask> t, long d) { t.accept(sched(p, () -> {}, false, d, -1)); }
    @Override public BukkitTask runTaskLaterAsynchronously(Plugin p, BukkitRunnable t, long d) { return runTaskLaterAsynchronously(p, (Runnable) t, d); }
    @Override public BukkitTask runTaskTimer(Plugin p, Runnable t, long d, long per) { return sched(p, t, true, d, per); }
    @Override public void runTaskTimer(Plugin p, Consumer<? super BukkitTask> t, long d, long per) { t.accept(sched(p, () -> {}, true, d, per)); }
    @Override public BukkitTask runTaskTimer(Plugin p, BukkitRunnable t, long d, long per) { return runTaskTimer(p, (Runnable) t, d, per); }
    @Override public BukkitTask runTaskTimerAsynchronously(Plugin p, Runnable t, long d, long per) { return sched(p, t, false, d, per); }
    @Override public void runTaskTimerAsynchronously(Plugin p, Consumer<? super BukkitTask> t, long d, long per) { t.accept(sched(p, () -> {}, false, d, per)); }
    @Override public BukkitTask runTaskTimerAsynchronously(Plugin p, BukkitRunnable t, long d, long per) { return runTaskTimerAsynchronously(p, (Runnable) t, d, per); }
    @Override public Executor getMainThreadExecutor(Plugin p) { return r -> sched(p, r, true, 0, -1); }

    public long currentTick() { return currentTick; }
}
