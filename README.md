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

| Type | Behaviour |
|------|-----------|
| `RAM` | Farthest ally → nearest target |
| `REVERSED_RAM` | Closest target → nearest ally |
| `RANDOMIZED_RAM` | Farthest ally → random target |
| `FAKE_RAM` | Same as RAM but marked as fake |
| `RANDOMIZED_FAKE_RAM` | Same as RANDOMIZED_RAM but marked as fake |
| `NOBLE` | Closest target → nearest ally (with noble) |
| `FAKE_NOBLE` | Same as NOBLE but marked as fake |
| `DEMOLITION` | Farthest ally → nearest target (demolition) |
| `RANDOMIZED_DEMOLITION` | Farthest ally → random target (demolition) |
