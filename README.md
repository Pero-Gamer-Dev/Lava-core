# 🌋 Lava-core (Fabric Server Mod)

Lava-core is a high-performance Fabric dedicated-server mod designed for **Minecraft 1.21.4**. It emulates a Bukkit/Paper server environment directly inside the Fabric ecosystem, allowing server administrators to run traditional Paper plugins alongside Fabric mods without replacing the server JAR.

## 🚀 Key Features (1.0.1-BETA)

*   **Native Plugin Loading:** Loads legacy `plugin.yml` Java plugins (EssentialsX-style) reliably without Paper's modern loader complexity.
*   **Lifecycle Management:** Full implementation of plugin enable/disable states, built-in permissions, and global command mapping.
*   **Threading Engine:** Functional sync and async task scheduler to handle plugins safely across threads without stalling server ticks.
*   **Player Mappings:** Full synchronization for live player elements including teleportation, gamemodes, health, food, ops, and kick handling.
*   **Event Pipeline:** Active handlers for block breaking, custom commands, player chats, joins, and quits.
*   **Internal Aliases:** Features internal `CraftServer` and `CraftPlayer` aliases alongside runtime version remapping to ensure plugin compatibility.

## ⚠️ Ecosystem Architecture Notice

Lava-core keeps a strict boundary between the modding layer and the plugin layer. Networking and protocol-level modifications **must** live in the `mods/` directory. 
* *Example:* **ViaVersion** belongs in the `mods/` folder as **ViaFabric**, not as a Bukkit `.jar` inside the plugins folder.

# Lava Core 1.0.1-BETA

A Minecraft Bukkit plugin framework for version 1.21.4

## Building

```bash
./gradlew build
```

## License

See LICENSE file for details.
