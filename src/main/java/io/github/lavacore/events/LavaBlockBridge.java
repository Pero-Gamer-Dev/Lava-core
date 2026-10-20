/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.events;

import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import io.github.lavacore.bridge.world.LavaWorldWrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public final class LavaBlockBridge {
    private LavaBlockBridge() {}

    public static void registerEventBuses() {
        try {
            Class<?> eventsClass = Class.forName("net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents");
            Object before = eventsClass.getField("BEFORE").get(null);
            Class<?> callbackClass = Class.forName("net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents$Before");
            Object callback = Proxy.newProxyInstance(
                    callbackClass.getClassLoader(),
                    new Class<?>[]{callbackClass},
                    (proxy, method, args) -> {
                        if (method.getDeclaringClass() == Object.class) {
                            return switch (method.getName()) {
                                case "toString" -> "LavaBlockBridge";
                                case "hashCode" -> System.identityHashCode(proxy);
                                case "equals" -> proxy == args[0];
                                default -> null;
                            };
                        }
                        return Boolean.valueOf(onBeforeBlockBreakReflect(args));
                    });
            Method register = null;
            for (Method m : before.getClass().getMethods()) {
                if ("register".equals(m.getName()) && m.getParameterCount() == 1) {
                    register = m;
                    break;
                }
            }
            if (register == null) {
                for (Method m : before.getClass().getDeclaredMethods()) {
                    if ("register".equals(m.getName()) && m.getParameterCount() == 1) {
                        register = m;
                        break;
                    }
                }
            }
            if (register == null) {
                System.out.println("[Lava] Block-break bridge: no register() found — skipped.");
                return;
            }
            register.setAccessible(true);
            register.invoke(before, callback);
            System.out.println("[Lava] Block-break bridge registered (fabric-api).");
        } catch (ClassNotFoundException e) {
            System.out.println("[Lava] fabric-api not installed — block-break Bukkit events disabled (server OK).");
        } catch (Throwable t) {
            System.out.println("[Lava] Block-break bridge skipped: " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    private static boolean onBeforeBlockBreakReflect(Object[] args) {
        try {
            if (Bukkit.getServer() == null || args == null || args.length < 3) return true;
            Object nmsPlayer = args[1];
            Object nmsPos = args[2];
            Object nmsWorld = args[0];
            UUID uuid = (UUID) nmsPlayer.getClass().getMethod("getUUID").invoke(nmsPlayer);
            var bukkitPlayer = Bukkit.getPlayer(uuid);
            if (bukkitPlayer == null) return true;
            int x = ((Number) nmsPos.getClass().getMethod("getX").invoke(nmsPos)).intValue();
            int y = ((Number) nmsPos.getClass().getMethod("getY").invoke(nmsPos)).intValue();
            int z = ((Number) nmsPos.getClass().getMethod("getZ").invoke(nmsPos)).intValue();
            Method of = LavaWorldWrapper.class.getMethod("of", Class.forName("net.minecraft.world.level.Level"));
            Object worldWrapper = of.invoke(null, nmsWorld);
            Object bukkitBlock = worldWrapper.getClass()
                    .getMethod("getBlockAt", int.class, int.class, int.class)
                    .invoke(worldWrapper, x, y, z);
            BlockBreakEvent event = new BlockBreakEvent((org.bukkit.block.Block) bukkitBlock, bukkitPlayer);
            Bukkit.getPluginManager().callEvent(event);
            return !event.isCancelled();
        } catch (Throwable t) {
            return true;
        }
    }
}
