# Lava-core build instructions (1.0.7-BETA)

## Prerequisites

- **JDK 21**
- Network on first build (Mojang jars, Fabric Loom, Paper API)
- Gradle wrapper included (`./gradlew`)

## Build

```bash
cd lava-core
./gradlew remapJar
```

Remapped mod:

```
build/libs/lava-core-1.0.7-BETA.jar
```

## What changed in 1.0.7

- **Direct Runtime**: Player/World/Block hot paths use direct Minecraft API (no reflection)
- **RuntimeContext** with PlayerRegistry, WorldRegistry, EntityRegistry
- **DirectPlayerOps / DirectWorldOps / DirectBlockOps**
- **Scheduler 2.0** with TaskBucket due-task lookup
- **Metrics 2.0**: direct vs reflection %, cache hits, `/lava performance|runtime|compatibility`
- MethodCache retained as reflective fallback only

## Notes

Requires Fabric dedicated server for Minecraft **1.21.4**.
