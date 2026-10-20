/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.runtime;

/**
 * Declares which runtime paths are DIRECT, REFLECTIVE_FALLBACK, or UNSUPPORTED.
 * Populated at startup; consulted at runtime so hot paths stay direct.
 */
public final class RuntimeCapabilities {

    public enum Mode {
        DIRECT,
        REFLECTIVE_FALLBACK,
        UNSUPPORTED,
        PARTIAL
    }

    private Mode playerHealth = Mode.DIRECT;
    private Mode playerFood = Mode.DIRECT;
    private Mode playerXp = Mode.DIRECT;
    private Mode playerTeleport = Mode.DIRECT;
    private Mode playerGameMode = Mode.DIRECT;
    private Mode worldTime = Mode.DIRECT;
    private Mode worldWeather = Mode.DIRECT;
    private Mode worldSpawn = Mode.DIRECT;
    private Mode worldHeight = Mode.DIRECT;
    private Mode blockLookup = Mode.DIRECT;
    private Mode blockSetType = Mode.DIRECT;
    private Mode inventoryBridge = Mode.PARTIAL;
    private Mode fabricEventBridge = Mode.DIRECT;
    private Mode paperRegionScheduler = Mode.UNSUPPORTED;
    private Mode paperNms = Mode.UNSUPPORTED;

    public Mode playerHealth() { return playerHealth; }
    public Mode playerFood() { return playerFood; }
    public Mode playerXp() { return playerXp; }
    public Mode playerTeleport() { return playerTeleport; }
    public Mode playerGameMode() { return playerGameMode; }
    public Mode worldTime() { return worldTime; }
    public Mode worldWeather() { return worldWeather; }
    public Mode worldSpawn() { return worldSpawn; }
    public Mode worldHeight() { return worldHeight; }
    public Mode blockLookup() { return blockLookup; }
    public Mode blockSetType() { return blockSetType; }
    public Mode inventoryBridge() { return inventoryBridge; }
    public Mode fabricEventBridge() { return fabricEventBridge; }
    public Mode paperRegionScheduler() { return paperRegionScheduler; }
    public Mode paperNms() { return paperNms; }

    public void setPlayerHealth(Mode m) { this.playerHealth = m; }
    public void setPlayerFood(Mode m) { this.playerFood = m; }
    public void setPlayerXp(Mode m) { this.playerXp = m; }
    public void setPlayerTeleport(Mode m) { this.playerTeleport = m; }
    public void setPlayerGameMode(Mode m) { this.playerGameMode = m; }
    public void setWorldTime(Mode m) { this.worldTime = m; }
    public void setWorldWeather(Mode m) { this.worldWeather = m; }
    public void setWorldSpawn(Mode m) { this.worldSpawn = m; }
    public void setWorldHeight(Mode m) { this.worldHeight = m; }
    public void setBlockLookup(Mode m) { this.blockLookup = m; }
    public void setBlockSetType(Mode m) { this.blockSetType = m; }
    public void setInventoryBridge(Mode m) { this.inventoryBridge = m; }
    public void setFabricEventBridge(Mode m) { this.fabricEventBridge = m; }

    /** Snapshot for diagnostics. */
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player health=").append(playerHealth)
          .append(" food=").append(playerFood)
          .append(" xp=").append(playerXp)
          .append(" teleport=").append(playerTeleport)
          .append(" gamemode=").append(playerGameMode).append('\n');
        sb.append("World time=").append(worldTime)
          .append(" weather=").append(worldWeather)
          .append(" spawn=").append(worldSpawn)
          .append(" height=").append(worldHeight).append('\n');
        sb.append("Block lookup=").append(blockLookup)
          .append(" setType=").append(blockSetType).append('\n');
        sb.append("Inventory=").append(inventoryBridge)
          .append(" Fabric events=").append(fabricEventBridge)
          .append(" Region scheduler=").append(paperRegionScheduler)
          .append(" Paper NMS=").append(paperNms);
        return sb.toString();
    }
}
