/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.mixin;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.lavacore.bridge.LavaServer;
import io.github.lavacore.scheduler.LavaScheduler;
import java.util.function.BooleanSupplier;
@Mixin(MinecraftServer.class)
public class MinecraftServerTickMixin {
    @Inject(method="tickServer", at=@At("TAIL"))
    private void lava$pulseScheduler(BooleanSupplier haveTime, CallbackInfo ci) {
        LavaServer server = LavaServer.getInstance();
        if (server != null && server.getScheduler() instanceof LavaScheduler s) s.pulse();
    }
}
