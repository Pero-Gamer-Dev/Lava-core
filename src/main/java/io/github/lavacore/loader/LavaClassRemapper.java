/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.loader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import java.util.HashMap;
import java.util.Map;
public final class LavaClassRemapper {
    private static final Remapper REMAPPER = new Remapper() {
        private final Map<String,String> map = build();
        @Override public String map(String n) {
            if (n == null) return null;
            for (var e : map.entrySet()) if (n.startsWith(e.getKey())) return e.getValue()+n.substring(e.getKey().length());
            return n;
        }
        private Map<String,String> build() {
            Map<String,String> m = new HashMap<>();
            String dest = "org/bukkit/craftbukkit/v1_21_R3/";
            for (String s : new String[]{"v1_21_R4","v1_21_R3","v1_21_R2","v1_21_R1","v1_20_R4","v1_20_R3","v1_20_R2","v1_20_R1"})
                m.put("org/bukkit/craftbukkit/"+s+"/", dest);
            return m;
        }
    };
    private LavaClassRemapper() {}
    public static byte[] remap(byte[] raw) {
        try {
            ClassReader r = new ClassReader(raw);
            ClassWriter w = new ClassWriter(0);
            r.accept(new ClassRemapper(w, REMAPPER), 0);
            return w.toByteArray();
        } catch (Throwable t) { return raw; }
    }
}
