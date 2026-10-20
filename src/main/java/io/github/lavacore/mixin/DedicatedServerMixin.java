/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
// ============================================================================
// ============================================================================
package io.github.lavacore.mixin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// NOTE: corrected from the original draft's "net.minecraft.server.DedicatedServer" --
// in official Mojang mappings this class lives in the .dedicated subpackage.
// Verify against your exact mapped sources (./gradlew genSources) before building.
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.github.lavacore.LavaCoreInitializer;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(
        method = "initServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadLevel()V",
            shift = At.Shift.AFTER
        )
    )
    private void onLevelLoadedSetupLava(CallbackInfoReturnable<Boolean> cir) {
        DedicatedServer currentServerInstance = (DedicatedServer) (Object) this;
        LavaCoreInitializer.initializeLavaEngine(currentServerInstance);
    }
}
