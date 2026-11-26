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
| [`botToolSwap`](#bottoolswap) | `false` | Bots auto-equip better tools and replace nearly broken ones |
| [`noBatSpawning`](#nobatspawning) | `false` | Bats stop spawning naturally |
| [`noAquaticSpawning`](#noaquaticspawning) | `false` | Fish, squid and axolotls stop spawning naturally |
| [`botmemo`](#botmemo) | `true` | `/botmemo` saves and restores a bot's state |
| [`botChineseName`](#botchinesename) | `true` | `/player` accepts quoted Unicode names |
| [`deathchest`](#deathchest) | `false` | Items go into chests at the death spot |
| [`leashableVillagers`](#leashablevillagers) | `false` | Villagers and wandering traders can be leashed |
| [`villagerneverstoptrading`](#villagerneverstoptrading) | `false` | Trades never lock; uses frozen, so prices no longer rise |
| [`crispydeepslate`](#crispydeepslate) | `false` | Deepslate is mined as if its hardness were 1.5 |
| [`TNTdrag`](#tntdrag) | `float` | Primed TNT air drag: `float` (26.2+) or `doublefloat` (pre-26.2) |
| [`TNTchunkloading`](#tntchunkloading) | `false` | Fast primed TNT keeps its chunk force-loaded (80 gt pearl-level ticket) |

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

When a bot picks up a tool, it equips it if it is better than the current main-hand tool. It also replaces a nearly broken held tool with the same tool that has the most durability in its inventory.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bots keep whatever is in the main hand |
| `true` | Auto-switch to a better picked-up tool, and swap nearly broken tools |

Order of comparison:

1. **Mending**: if exactly one of the two has Mending, switch to that one and stop.
2. **Total enchantment level**: if neither or both have Mending, switch to the one with the higher sum of enchantment levels, where every curse level counts as **-10**.
3. **Material tier**: if the totals are equal (often both plain), switch to the one with the higher max durability — wood < stone < iron < diamond < netherite.

Details:

- Only tools are considered: items with durability that are not equipment such as armour or elytra.
- Only tools of the same kind are compared: pickaxes with pickaxes, axes with axes, and likewise shovels, hoes, swords and shears. A bow will never replace a fishing rod.
- Only Carpet fake players are handled; real players are untouched.
- An empty main hand is left alone: the picked-up tool simply stays in the inventory.
- Items are never vacuumed off the ground: vanilla pickup delay runs first, and the comparison only happens after the item has really entered the inventory.
- The old tool goes back into the bot's inventory; it is dropped only when the inventory is full.
- When the held tool's remaining durability drops to 10% or less (at least 1 point), it is swapped for the same tool with the most durability in the inventory; the replacement must have at least 10% more durability than the current one. If the tool breaks, the best replacement is equipped immediately.

### `noBatSpawning`

Stops bats from spawning naturally.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bats spawn in dark places |
| `true` | No natural bat spawning |

Details:

- Sets the ambient spawn rate to 0 once when enabled (and again after off→on). The ambient category currently contains only bats; a manual `/spawn rates ambient ...` while the rule is on is left alone.
- Spawn eggs, spawners and `/summon` still work, and bats already in the world are untouched.
- Phantoms are not covered by this rule; use the vanilla gamerule instead: `/gamerule spawn_phantoms false`.

### `noAquaticSpawning`

Stops aquatic mobs from spawning naturally by setting their Carpet spawn rate to 0 — the same table `/spawn rates` writes to.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: aquatic mobs spawn naturally |
| `true` | No natural spawning for water creatures, water ambient, underground water creatures and axolotls |

Details:

- Categories: `water_creature` (fish, dolphins), `water_ambient` (squid, glow squid), `underground_water_creature` and `axolotls`.
- The rates are written once when the rule is enabled (and again after you switch it off and on). A manual `/spawn rates ...` while the rule is on is left alone — it will just work like it normally does. Turning the rule off restores the values captured when it was enabled.
- Only natural spawning is affected: existing mobs, spawn eggs, spawners, buckets and `/summon` still work.
- Dolphins share the `water_creature` category with fish, so they are disabled too.

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
- Memos are written to `config/carpet-dan-addition/botmemo.json` and survive a server restart. Inventory, mount, health and potion effects are not saved.
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

### `leashableVillagers`

Lets you attach a lead to villagers and wandering traders and pull them around.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: villagers cannot be leashed |
| `true` | Villagers and wandering traders can be leashed and pulled |

Details:

- The only change is the attachment check: leashing physics, distance limits, lead breaking and fence knots behave like any other leashable mob.
- Works for both villagers and wandering traders (both extend `AbstractVillager`).
- Use shears to cut the lead, as with other leashed mobs.
- Villagers still pathfind normally; the lead pulls them when you walk away.

### `villagerneverstoptrading`

Removes the trade-use limit for villagers and wandering traders.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: a trade locks after its max uses, and prices rise with demand |
| `true` | Infinite trades: the used count is frozen, so offers never lock and prices stay where they are |

Details:

- Applies to villagers, wandering traders and any merchant that uses vanilla `MerchantOffer`s.
- `true` freezes the used count, so a trade never locks and the restock loop that raises `demand` never runs. Prices stay at their current value (the discount from curing a zombie villager still applies).
- Cost: three cheap checks per trade, O(1). It does no command execution and rewrites no NBT, unlike the advancement/datapack approach that resets `Offers.Recipes[].uses` for every villager near the player on every trade.
- Prices are still limited by the offer's own base cost and price multiplier.

### `crispydeepslate`

Mines the deepslate block as if its hardness were stone's 1.5 instead of 3.0.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: deepslate hardness 3.0 |
| `true` | Deepslate is mined at stone speed (hardness 1.5) |

Details:

- Only the `minecraft:deepslate` block is affected; deepslate ores keep their own hardness and drops are unchanged.
- The progress formula is the same as vanilla, so tools, Efficiency, Haste, Mining Fatigue and water/air penalties all behave as usual.
- Clients that do not run the addon keep showing the vanilla breaking animation while the server breaks the block earlier; single-player and integrated servers are consistent.

### `TNTdrag`

Controls the air drag precision applied to primed TNT each tick.

| Value | Behaviour |
| --- | --- |
| `float` (default) | Vanilla 26.2+: `PrimedTnt.tick()` scales its velocity by `Entity#getAirDrag()`, a float `0.98F` widened to double |
| `doublefloat` | Pre-26.2 precision: the scale argument is the exact double literal `0.98D` |

Details:

- The mixin targets only the `Vec3.scale(double)` call inside `PrimedTnt.tick()`; gravity and the horizontal `0.7` / `-0.5` friction use `multiply(DDD)` and are untouched.
- The difference is tiny (float 0.98F ≈ 0.9800000190734863 vs 0.98D), but it accumulates over long fuses and is the exact behaviour vanilla had before 26.2.

### `TNTchunkloading`

While a primed TNT moves horizontally faster than the configured threshold, it keeps the chunk it occupies force-loaded.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Off; no chunk-loading logic runs |
| non-negative number | Horizontal speed threshold in blocks/tick (e.g. `0`, `0.5`, `1.0`, `2.0`) |

Details:

- Checks `Math.abs(motion.x) > threshold || Math.abs(motion.z) > threshold` after `PrimedTnt.tick()`, on the server only, once per tick.
- The ticket is a custom `TicketType` with an 80 gt timeout and exactly the flag bits of `TicketType.ENDER_PEARL`, so loading radius, simulation level and keep-dimension-active behaviour match an ender pearl. It is placed with radius `2`, the same radius `ServerPlayer.placeEnderPearlTicket` uses.
- `strict = false` with a custom `Validator<String>`: `false` (any case) or any finite non-negative double is accepted; `-1`, `NaN`, `Infinity` and junk are rejected. The parsed value is cached, so the tick path never calls `Double.parseDouble`; the cache refreshes only when Carpet changes the rule string.
- The listed options (`false`, `0.5`, `1.0`, `2.0`) are only tab-completion hints.

Why it exists: 26.2 changed primed TNT behaviour (see `TNTdrag`), and some machines that relied on a moving TNT staying loaded/being ticked as it crosses chunk borders stopped working. Renewing a pearl-level ticket on the moving TNT brings part of those machines back while the rule is on. It does not change TNT drag precision - use `TNTdrag` for that.

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
/carpet noAquaticSpawning true
/carpet botmemo true
/carpet botChineseName true
/carpet leashableVillagers true
/carpet villagerneverstoptrading true
/carpet crispydeepslate true
/carpet TNTdrag doublefloat
/carpet TNTchunkloading 1.0
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
# output: build/libs/carpet-dan-addition-1.5.11+mc26.3.jar
```

## Compatibility

- Minecraft 26.3 only (the Carpet 26.3 API is not compatible with older versions).
- No known conflicts with Lithium, FerriteCore or similar optimisation mods.
- Rules are server-authoritative; in single-player they apply to the integrated server.

## License

MIT. See [LICENSE](LICENSE).
