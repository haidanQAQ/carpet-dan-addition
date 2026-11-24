# Carpet Dan Addition

**English** | [简体中文](README.zh_cn.md)

[![Release](https://img.shields.io/github/v/release/haidanQAQ/carpet-dan-addition?label=release)](https://github.com/haidanQAQ/carpet-dan-addition/releases)
[![License](https://img.shields.io/github/license/haidanQAQ/carpet-dan-addition)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-26.3-62b47a)](https://www.minecraft.net/)
[![Issues](https://img.shields.io/github/issues/haidanQAQ/carpet-dan-addition)](https://github.com/haidanQAQ/carpet-dan-addition/issues)

A small [Carpet](https://github.com/gnembon/fabric-carpet) extension for **Minecraft 26.3**, collecting a few vanilla-friendly rules that are handy for single-player worlds and small technical servers.

> **Vibe-coding notice**: this project was built with AI assistance (vibe coding). It has been smoke-tested on a local 26.3 server, but not exhaustively. Use it at your own risk, and feel free to open an issue if something breaks.

## Requirements

| | |
| --- | --- |
| Minecraft | **26.3** only |
| Fabric Loader | 0.20.1 or newer |
| Java | 25 |
| Dependencies | [Fabric API](https://modrinth.com/mod/fabric-api), [Carpet](https://modrinth.com/mod/carpet) (26.3 build) |

## Rules

Every rule lives in the `dan` category. Run `/carpet list dan` in game to see them all.

| Rule | Default | Summary |
| --- | --- | --- |
| [`fishingTreasureNoOpenWater`](#fishingtreasurenoopenwater) | `false` | Pre-1.16 fishing: no 5×4×5 open-water requirement |
| [`hoebedrockbreaking`](#hoebedrockbreaking) | `false` | Hoes can break bedrock; no drops, 1 durability each |
| [`botnoclip`](#botnoclip) | `false` | Nothing can push Carpet fake players |
| [`botToolSwap`](#bottoolswap) | `false` | Bots auto-equip a better tool they pick up |
| [`noBatSpawning`](#nobatspawning) | `false` | Bats stop spawning naturally |
| [`botmemo`](#botmemo) | `true` | `/botmemo` saves and restores a bot's state |
| [`botChineseName`](#botchinesename) | `true` | `/player` accepts quoted Unicode names |
| [`deathchest`](#deathchest) | `false` | Items go into chests at the death spot |

### `fishingTreasureNoOpenWater`

Restores the pre-1.16 fishing feel: treasure can be caught without a 5×4×5 open-water area.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: treasure requires open water |
| `true` | Ignore the open-water check; treasure chance stays vanilla (5%) |
| `imba` | Only the treasure loot table: no fish, no junk |

Details:

- In 26.3 vanilla fishing uses one loot pool with fish/junk/treasure entries; treasure is a 5% entry gated by the `in_open_water` condition. `true` only removes that condition, so you still get fish and junk normally.
- `imba` redirects the fishing loot table to the treasure table, so every catch is treasure; Luck of the Sea does nothing in this mode.
- Works anywhere, including a 1×1 puddle.

### `hoebedrockbreaking`

Lets a hoe break bedrock.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bedrock is unbreakable |
| `true` | Hoes can break bedrock |

Details:

- Bedrock drops nothing, not even with Silk Touch (bedrock has no loot table).
- Each block costs 1 durability; Unbreaking applies as usual.
- Breaking time equals a pickaxe mining stone; with a diamond hoe or better, Efficiency V and Haste II it breaks in 1 game tick.
- Only hoes trigger it; other tools still cannot break bedrock.

### `botnoclip`

No entity can push Carpet fake players (bots) around, so a placed bot stays put.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bots and players push each other |
| `true` | Players, mobs and other bots cannot push bots |

Details:

- **This is intended behaviour and will not change**: with `true`, bots are excluded from the pushable list.
- **Known limitation**: bots still push players, so the two cannot fully overlap. Half of the pushing is computed client-side, where a bot looks like an ordinary player entity; the server can only block its own half.

### `botToolSwap`

When a bot picks up a tool, it equips it if it is better than the current main-hand tool.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bots keep whatever is in the main hand |
| `true` | Auto-switch to a better picked-up tool |

Order of comparison:

1. **Mending**: if exactly one of the two has Mending, switch to that one and stop.
2. **Total enchantment level**: if neither or both have Mending, switch to the one with the higher sum of enchantment levels, where every curse level counts as **-10**.

Details:

- Only tools are considered: items with durability that are not equipment such as armour or elytra.
- Only Carpet fake players are handled; real players are untouched.
- An empty main hand is left alone: the picked-up tool simply stays in the inventory.
- Items are never vacuumed off the ground: vanilla pickup delay runs first, and the comparison only happens after the item has really entered the inventory.
- The old tool goes back into the bot's inventory; it is dropped only when the inventory is full.

### `noBatSpawning`

Stops bats from spawning naturally.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bats spawn in dark places |
| `true` | No natural bat spawning |

Details:

- Only the vanilla bat spawn check is disabled.
- Spawn eggs, spawners and `/summon` still work, and bats already in the world are untouched.
- Phantoms are not covered by this rule; use the vanilla gamerule instead: `/gamerule spawn_phantoms false`.

### `botmemo`

Saves a fake player's position, rotation and current actions, and restores them later.

| Value | Behaviour |
| --- | --- |
| `true` (default) | `/botmemo` is available |
| `false` | The command is disabled |

Details:

- `/botmemo <name> save` remembers where the bot is, which way it faces and what it is doing (`use`, `attack`, `move`, ...). Saving again overwrites the old memo.
- `/botmemo <name> run` teleports the bot back and restores the saved actions.
- If the bot is offline, `run` spawns it at the saved position (same dimension, rotation and game mode) and then restores the actions.
- Memos live in memory only and are lost on restart. Inventory, mount, health and potion effects are not saved.
- Unicode names must be quoted, e.g. `/botmemo "测试" save`.

### `botChineseName`

Allows Carpet's `/player` command to accept quoted Unicode names.

| Value | Behaviour |
| --- | --- |
| `true` (default) | `/player "测试" spawn` works |
| `false` | Unicode `/player` commands are rejected |

Details:

- Names must be quoted; unquoted Unicode is still rejected by the vanilla command parser.
- Toggling the rule takes effect immediately; no restart is needed.
- The argument type is still vanilla `brigadier:string`, so vanilla clients understand the quoted form; no client mod is required.

### `deathchest`

Puts a dead player's items into chests at the death spot instead of scattering them on the ground.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: items drop on the ground |
| `true` | Items are moved into chests at the death location |

Details:

- Up to 27 stacks go into one chest; 28–54 stacks go into two chests stacked vertically.
- Items with Curse of Vanishing are destroyed as usual. XP is not stored. Chests are not protected and are not marked as owned.
- Free spots are searched upwards first, then downwards; only air counts as free (water and lava do not).
- If the player dies outside the world height, the search starts from the nearest height limit (top-down above the world, bottom-up below it).
- If no free spot exists, blocks are overwritten in place to make room; block entities are never overwritten and the search keeps going up.
- Chests are ordinary chests, so hoppers, double chests and `/data` work normally.

## Commands

### Unicode bot names

Carpet's `/player` command normally accepts only ASCII names. With the `botChineseName` rule on (default), the name argument is swapped to a quoted string, so Unicode names work when they are written **in quotes**:

```
/player "测试" spawn
/player "测试" kill
```

Unquoted names are still restricted by the vanilla command parser, so `/player 测试 spawn` is rejected. Vanilla clients can parse the quoted form, so no client mod is needed.

Turn the feature off with `/carpet botChineseName false`; Unicode `/player` commands are then rejected with a message, and turning it back on takes effect immediately.

### Dropping everything a bot holds

This addon does not add a rule for it, because Carpet already ships the command:

```
/player <name> dropStack all
```

`dropStack all` empties every inventory slot of the bot and throws each full stack out. `/player <name> drop all` is the one-item-per-slot variant.

### Bot memo

```
/botmemo <name> save
/botmemo <name> run
```

`save` remembers the bot's position, rotation and actions; `run` restores them and spawns the bot if it is offline. Requires the `botmemo` rule (on by default). Unicode names need quotes.

## Installation

1. Minecraft **26.3** + Fabric Loader **0.20.1** or newer.
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) and [Carpet](https://modrinth.com/mod/carpet) (26.3 build).
3. Drop `carpet-dan-addition-*.jar` into `mods/`.
4. Start the server or a single-player world.

Server-side installation is enough; in single-player just drop it into `mods/`. In multiplayer, installing it on the client too gives consistent breaking progress.

## Usage

```
/carpet list dan
/carpet fishingTreasureNoOpenWater true
/carpet fishingTreasureNoOpenWater imba
/carpet fishingTreasureNoOpenWater false
/carpet hoebedrockbreaking true
/carpet botnoclip true
/carpet botToolSwap true
/carpet noBatSpawning true
/carpet botmemo true
/carpet botChineseName true
/player "测试" spawn
/botmemo "测试" save
/botmemo "测试" run
/player hai_dan dropStack all
```

Rule values are case-insensitive (`true` / `TRUE` both work). `/carpet setDefault` makes a value persistent; a plain `/carpet <rule> <value>` is temporary and resets on restart.

## Building

Requires **JDK 25**:

```bash
./gradlew build
# output: build/libs/carpet-dan-addition-1.5.1+mc26.3.jar
```

## Compatibility

- Minecraft 26.3 only (the Carpet 26.3 API is not compatible with older versions).
- No known conflicts with Lithium, FerriteCore or similar optimisation mods.
- Rules are server-authoritative; in single-player they apply to the integrated server.

## License

MIT. See [LICENSE](LICENSE).
