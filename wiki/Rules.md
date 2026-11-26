# Rules

English | [简体中文](Rules_zh.md)

All rules belong to the `dan` category and are changed with `/carpet <rule> <value>`. `/carpet list dan` prints the category. A rule change applies immediately unless a section says otherwise.

## TNTchunkloading

While a primed TNT moves horizontally faster than the threshold, it adds an 80 gt chunk ticket to its current chunk every tick. The ticket has the same flags as an ender pearl ticket and covers a radius of 2 chunks. Setting the rule to `false` disables it; any non-negative number is accepted, the listed options are only for tab completion.

- Type: `String`
- Default: `false`
- Options: `false`, or any non-negative number (`0`, `0.5`, `1.0`, `2.0` are suggested)
- Categories: `dan`

```text
/carpet TNTchunkloading 0.5
```

## fishingTreasureNoOpenWater

`false` keeps vanilla behaviour: treasure requires an open water area. `true` ignores the open water requirement, so treasure can be caught anywhere the bobber lands; the treasure chance stays at the vanilla 5% weight. `imba` uses the treasure loot table only: no fish and no junk.

- Type: `String`
- Default: `false`
- Options: `false`, `true`, `imba`
- Categories: `dan`

```text
/carpet fishingTreasureNoOpenWater true
```

## hoebedrockbreaking

A hoe can break bedrock. The block drops nothing, including with Silk Touch, and costs 1 durability per block (Unbreaking applies). Breaking time matches a pickaxe mining stone: the hoe's tier speed, Efficiency, Haste and the block break speed attribute are used against stone's hardness 1.5 with the correct-tool divisor. A diamond hoe with Efficiency V and Haste II breaks one bedrock per tick.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## crispydeepslate

The `minecraft:deepslate` block is mined as if its hardness were 1.5 instead of 3.0. Deepslate ores keep their own hardness, and the block still drops the same item.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## TNTdrag

Controls the air drag factor applied by `PrimedTnt.tick()`. `float` keeps vanilla 26.2+ behaviour, where the factor is the float `0.98F` widened to double. `doublefloat` uses the exact double literal `0.98D`, matching 26.2 and earlier.

- Type: `String`
- Default: `float`
- Options: `float`, `doublefloat`
- Categories: `dan`

## mergeTNTdebug

Enables `/mergeTNT <pos> [n] [m]`. It spawns one primed TNT that carries `n` TNT equivalents and explodes `n` times at its own position; every extra pass is a separate 4.0F explosion, the same repetition Carpet's `mergeTNT` performs. It never reads or writes the counters of `mergeTNT`, `mergeTNTPro` or `mergeTNTMax`, so it works with any of them enabled, disabled or absent.

`pos` uses vanilla coordinates: absolute (`0 70 0`), relative (`~ ~1 ~`) or local (`^ ^ ^`), with the vanilla tab completion. `n` defaults to 1 and has no upper limit. `m` is the fuse in ticks and defaults to 80. The spawned TNT starts with zero velocity, so it stays at the requested coordinates until gravity acts. The count is stored in memory only; if the TNT is merged into another TNT by a merge rule, its count is lost.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

```text
/carpet mergeTNTdebug true
/mergeTNT 0 70 0 5 80
/mergeTNT ~ ~1 ~ 5 80
```

## botnoclip

No entity can push a Carpet fake player, including players, mobs and other fake players. A bot therefore stays where it was placed. Bots still push players, so a bot and a player cannot fully overlap.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## botToolSwap

When a fake player picks up a tool, it compares it with the tool in the main hand. Only the same kind of tool is compared: pickaxes, axes, shovels, hoes and swords by item tag, shears by item. If exactly one of the two has Mending, that one wins. Otherwise the tool with the higher total enchantment level wins, with every curse level counted as -10.

The rule also replaces a held tool whose remaining durability is at 10% or less (at least 1 point) with the same tool that has the most durability in the inventory.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## botmemo

Enables `/botmemo <name> save` and `/botmemo <name> run`. `save` stores the bot's position, rotation and current actions. `run` restores them; if the bot is offline, it is spawned at the saved position first. Actions are applied after the bot has fully joined, so a restored `use continuous` keeps the vanilla 4 gt cooldown.

Memos are written to `config/carpet-dan-addition/botmemo.json` and survive server restarts. Inventory, health, effects and mounts are not stored.

- Type: `Boolean`
- Default: `true`
- Options: `true`, `false`
- Categories: `dan`

```text
/botmemo hai_dan save
/botmemo hai_dan run
```

## botChineseName

Allows Carpet's `/player` command to accept quoted Unicode names:

```text
/player "测试" spawn
```

The name argument is always the vanilla string type. When the rule is off, `/player` commands containing non-ASCII characters are rejected at execution, so toggling the rule takes effect immediately. Quotes are required because the unquoted argument still only accepts ASCII. If the mod is installed on the client, tab completion inserts the quotes as well.

- Type: `Boolean`
- Default: `true`
- Options: `true`, `false`
- Categories: `dan`

## deathchest

When a player dies, the items go into chests at the death spot instead of dropping: one chest for up to 27 stacks, two stacked chests for more. Chests are not protected, do not carry an owner label, and do not store experience. Creative and spectator players and `keepInventory` are skipped.

If the death point is outside the world height limits, the nearest valid height is used. If no air space is found, blocks are overwritten as a last resort.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## leashableVillagers

Villagers and wandering traders can be leashed and pulled like other leashable mobs.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## villagerneverstoptrading

Freezes the used count of every trade, so a trade never locks and demand never grows; prices stay at their current value. Applies to villagers and wandering traders. Each trade performs constant checks only, with no commands or NBT rewriting.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## noBatSpawning

Sets Carpet's ambient spawn rate to 0, so bats stop spawning naturally. Existing bats, spawn eggs, spawners and commands are unaffected. Phantoms are not covered; use the vanilla `/gamerule spawn_phantoms false`.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`

## noAquaticSpawning

Sets Carpet's spawn rate to 0 for `water_creature` (fish, dolphins), `water_ambient` (squid), `underground_water_creature` and `axolotls`. Existing mobs, spawn eggs, spawners and commands are unaffected.

- Type: `Boolean`
- Default: `false`
- Options: `true`, `false`
- Categories: `dan`
