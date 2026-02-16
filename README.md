# TW-Action-Organizer

A Tribal Wars attack coordination tool that assigns ally villages to targets using distance-based algorithms and generates formatted PM messages with departure times and in-game links.

## Requirements

- **JDK 21+**
- **Gradle** (wrapper included)

## Build & Run

```bash
# Run the desktop app
./gradlew :desktop:run

# Build native distributables (DMG/MSI/DEB)
./gradlew :desktop:packageDistributionForCurrentOS

# Run core tests
./gradlew :core:test
```

## Workflow

1. **Set world** — enter a TW server domain (e.g. `pl150.plemiona.pl`). Village and player data is fetched automatically.
2. **Load resources** — paste ally village coordinates for concrete, fake, and/or demolition pools.
3. **Add target groups** — paste target coordinates, set attacks per village, and choose an assigner type.
4. **Execute** — the engine assigns resources to targets based on distance algorithms.
5. **Review** — view assignments in the table or on the map, then use `PMFormatter` to generate player messages.

## Assignment Types

Every target group is assigned one of the types below. The type controls two things: **which village is picked first** (the "pivot") and **how its counterpart is chosen**.

All algorithms use a **reference point** — the averaged coordinates of all targets in the group. Distances are squared euclidean (no sqrt, consistent ordering).

### Concrete attacks (consume from the concrete resource pool)

These run **sequentially** — each assigner sees only the resources left over from the previous one. Execution order: `NOBLE` → `REVERSED_RAM` → `RANDOMIZED_RAM` → `RAM`.

| Type | Pivot | Pairing | When to use |
|------|-------|---------|-------------|
| **`RAM`** | Farthest ally from reference point | Nearest target to that ally | Default choice. Sends far-away villages first, producing short travel times for the remaining pool. |
| **`REVERSED_RAM`** | Closest target to reference point | Nearest ally to that target | Prioritises specific targets. Guarantees the closest targets get the best (nearest) ally villages. Good for high-value targets. |
| **`RANDOMIZED_RAM`** | Farthest ally from reference point | Random target | Spreads attacks unpredictably across all targets. Useful for masking the real nuke wave among noise. |
| **`NOBLE`** | Closest target to reference point | Nearest ally within noble range | Noble train. Only assigns villages whose owner still has nobles available, and only if the distance falls within the world's noble range. Players' noble counts are decremented per assignment. |

### Fake attacks (consume from the fake resource pool)

Fake RAM assigners run **in parallel** — they each get the full fake resource pool independently.

| Type | Pivot | Pairing | When to use |
|------|-------|---------|-------------|
| **`FAKE_RAM`** | Farthest ally from reference point | Nearest target to that ally | Ordered fakes that look realistic (short distances). |
| **`RANDOMIZED_FAKE_RAM`** | Farthest ally from reference point | Random target | Scattered fakes. Makes it harder for the defender to distinguish real attacks from fakes. |
| **`FAKE_NOBLE`** | Closest target to reference point | Nearest ally within noble range | Fake noble trains. Same noble-range and per-player-noble-count constraints as real `NOBLE`, but marked as fake. Runs after all other assigners. |

### Demolition attacks (consume from the demolition resource pool)

Demolition assigners run **sequentially** with shared resources, same as concrete.

| Type | Pivot | Pairing | When to use |
|------|-------|---------|-------------|
| **`DEMOLITION`** | Farthest ally from reference point | Nearest target to that ally | Catapult-focused attacks aimed at destroying buildings. |
| **`RANDOMIZED_DEMOLITION`** | Farthest ally from reference point | Random target | Randomised demolition spread. |

### Execution order

The executor processes groups in this order to ensure resource sharing works correctly:

1. **Fake RAM** (`FAKE_RAM`, `RANDOMIZED_FAKE_RAM`) — parallel, independent fake pool
2. **Demolition** (`DEMOLITION`, `RANDOMIZED_DEMOLITION`) — sequential, shared demolition pool
3. **Concrete** (`NOBLE`, `REVERSED_RAM`, `RANDOMIZED_RAM`, `RAM`) — sequential, shared concrete pool
4. **Fake Noble** (`FAKE_NOBLE`) — parallel, uses fake pool, runs last because it needs the world's noble range
