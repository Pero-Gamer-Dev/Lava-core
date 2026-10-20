/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.plugin;

import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Topological sort of plugins by depend / softdepend / loadbefore.
 */
public final class LavaDependencyResolver {
    public record Candidate(File file, PluginDescriptionFile description) {}

    private final Logger logger;

    public LavaDependencyResolver(Logger logger) {
        this.logger = logger;
    }

    public List<Candidate> resolve(List<Candidate> input) {
        Map<String, Candidate> byName = new LinkedHashMap<>();
        Map<String, String> providesToName = new HashMap<>();
        for (Candidate c : input) {
            String name = c.description().getName();
            byName.put(name.toLowerCase(), c);
            providesToName.put(name.toLowerCase(), name.toLowerCase());
            for (String p : c.description().getProvides()) {
                providesToName.put(p.toLowerCase(), name.toLowerCase());
            }
        }

        // Remove candidates with missing hard dependencies
        List<Candidate> loadable = new ArrayList<>();
        for (Candidate c : input) {
            List<String> missing = new ArrayList<>();
            for (String dep : c.description().getDepend()) {
                if (!providesToName.containsKey(dep.toLowerCase())) {
                    missing.add(dep);
                }
            }
            if (!missing.isEmpty()) {
                logger.severe("[Lava] Failed to load " + c.description().getName()
                        + " — missing dependency: " + String.join(", ", missing));
                continue;
            }
            loadable.add(c);
        }

        // Build edges: dependency must come before dependant
        Map<String, Set<String>> edges = new HashMap<>(); // name -> set of names that must load first
        Map<String, Integer> indegree = new HashMap<>();
        for (Candidate c : loadable) {
            String name = c.description().getName().toLowerCase();
            edges.putIfAbsent(name, new HashSet<>());
            indegree.putIfAbsent(name, 0);
        }
        for (Candidate c : loadable) {
            String name = c.description().getName().toLowerCase();
            for (String dep : c.description().getDepend()) {
                String d = providesToName.get(dep.toLowerCase());
                if (d != null && !d.equals(name)) {
                    if (edges.computeIfAbsent(d, k -> new HashSet<>()).add(name)) {
                        indegree.merge(name, 1, Integer::sum);
                    }
                }
            }
            for (String dep : c.description().getSoftDepend()) {
                String d = providesToName.get(dep.toLowerCase());
                if (d != null && !d.equals(name) && edges.containsKey(d)) {
                    if (edges.computeIfAbsent(d, k -> new HashSet<>()).add(name)) {
                        indegree.merge(name, 1, Integer::sum);
                    }
                }
            }
            for (String before : c.description().getLoadBefore()) {
                // c must load before `before`
                String b = providesToName.get(before.toLowerCase());
                if (b != null && !b.equals(name) && edges.containsKey(b)) {
                    if (edges.computeIfAbsent(name, k -> new HashSet<>()).add(b)) {
                        indegree.merge(b, 1, Integer::sum);
                    }
                }
            }
        }

        // Kahn topological sort
        List<Candidate> ordered = new ArrayList<>();
        List<String> queue = new ArrayList<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) queue.add(e.getKey());
        }
        Map<String, Candidate> lookup = new HashMap<>();
        for (Candidate c : loadable) {
            lookup.put(c.description().getName().toLowerCase(), c);
        }
        while (!queue.isEmpty()) {
            String n = queue.remove(0);
            Candidate c = lookup.get(n);
            if (c != null) ordered.add(c);
            for (String next : edges.getOrDefault(n, Set.of())) {
                int v = indegree.merge(next, -1, Integer::sum);
                if (v == 0) queue.add(next);
            }
        }
        if (ordered.size() < loadable.size()) {
            logger.warning("[Lava] Dependency cycle detected — appending remaining plugins unsorted");
            for (Candidate c : loadable) {
                if (!ordered.contains(c)) ordered.add(c);
            }
        }
        return ordered;
    }
}
