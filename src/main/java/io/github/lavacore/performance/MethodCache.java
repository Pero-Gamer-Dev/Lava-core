/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.performance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reflection fallback cache (1.0.7 role).
 * Prefer Direct*Ops for hot paths. This class is for optional integrations,
 * version differences, experimental APIs, and diagnostics — not player health,
 * food, world time, or block lookup.
 */
public final class MethodCache {
    private static final Map<String, Method> METHODS = new ConcurrentHashMap<>();
    private static final Map<String, Field> FIELDS = new ConcurrentHashMap<>();
    private static final AtomicLong RESOLVES = new AtomicLong();
    private static final AtomicLong HITS = new AtomicLong();

    private MethodCache() {}

    public static Method find(Class<?> clazz, String name, int paramCount) {
        String key = clazz.getName() + "#" + name + "#" + paramCount;
        Method cached = METHODS.get(key);
        if (cached != null) {
            HITS.incrementAndGet();
            LavaMetrics.recordReflection("METHOD_CACHE");
            return cached;
        }
        RESOLVES.incrementAndGet();
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == paramCount) {
                m.setAccessible(true);
                METHODS.put(key, m);
                LavaMetrics.recordReflection("METHOD_CACHE");
                return m;
            }
        }
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == paramCount) {
                m.setAccessible(true);
                METHODS.put(key, m);
                LavaMetrics.recordReflection("METHOD_CACHE");
                return m;
            }
        }
        return null;
    }

    public static Method findContains(Class<?> clazz, String fragment, int paramCount) {
        String key = clazz.getName() + "~" + fragment + "#" + paramCount;
        Method cached = METHODS.get(key);
        if (cached != null) {
            HITS.incrementAndGet();
            LavaMetrics.recordReflection("METHOD_CACHE");
            return cached;
        }
        RESOLVES.incrementAndGet();
        for (Method m : clazz.getMethods()) {
            if (m.getName().contains(fragment) && m.getParameterCount() == paramCount) {
                m.setAccessible(true);
                METHODS.put(key, m);
                LavaMetrics.recordReflection("METHOD_CACHE");
                return m;
            }
        }
        return null;
    }

    public static Field field(Class<?> clazz, String name) {
        String key = clazz.getName() + "." + name;
        Field cached = FIELDS.get(key);
        if (cached != null) {
            HITS.incrementAndGet();
            LavaMetrics.recordReflection("FIELD_CACHE");
            return cached;
        }
        RESOLVES.incrementAndGet();
        Class<?> c = clazz;
        while (c != null) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                FIELDS.put(key, f);
                LavaMetrics.recordReflection("FIELD_CACHE");
                return f;
            } catch (NoSuchFieldException ignored) {
                c = c.getSuperclass();
            }
        }
        return null;
    }

    public static long resolves() { return RESOLVES.get(); }
    public static long hits() { return HITS.get(); }
}
