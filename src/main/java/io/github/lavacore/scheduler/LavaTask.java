/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.scheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
public final class LavaTask implements BukkitTask, Runnable {
    final int id; final Plugin owner; final boolean sync; final Runnable runnable; final long period;
    long nextTick; volatile boolean cancelled;
    LavaTask(int id, Plugin owner, boolean sync, Runnable runnable, long nextTick, long period) {
        this.id=id; this.owner=owner; this.sync=sync; this.runnable=runnable; this.nextTick=nextTick; this.period=period;
    }
    @Override public int getTaskId(){return id;}
    @Override public Plugin getOwner(){return owner;}
    @Override public boolean isSync(){return sync;}
    @Override public boolean isCancelled(){return cancelled;}
    @Override public void cancel(){cancelled=true;}
    @Override public void run(){ if(!cancelled) runnable.run(); }
}
