# Carpet Dan Addition

A [Carpet](https://github.com/gnembon/fabric-carpet) addon for **Minecraft 26.3** by **hai_dan**.
It brings back the pre-1.16 fishing feel and collects small quality-of-life rules under the `dan` category.

> **Vibe coding 声明**：本项目的代码和文档是在 AI 辅助下完成的（vibe coding）。已在本机 26.3 服务端做过基本功能验证，但没有覆盖所有场景；使用前请自行评估风险，出问题欢迎提 issue。
>
> **Vibe-coding notice**: this project was built with AI assistance (vibe coding). It has been smoke-tested on a local 26.3 server, but not exhaustively. Use it at your own risk, and feel free to open an issue if something breaks.

> 目前只支持 Minecraft 26.3 / Minecraft 26.3 only.

---

## 中文

### 简介

Carpet Dan Addition（CDA）是 hai_dan 的 Carpet 附属模组。所有规则都放在 `/carpet` 的 **dan** 分类下，默认值保持原版行为。

### 功能

#### `fishingTreasureNoOpenWater`

还原 1.16 之前的钓鱼手感：不需要 5×4×5 的开放水域也能钓到宝藏。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版 1.16+：只有浮标处于开放水域时才可能钓到宝藏 |
| `true` | 忽略开放水域限制，宝藏仍按原版权重（约 5%，海之眷顾 III 约 11%） |
| `imba` | 只使用宝藏战利品表：100% 宝藏，没有鱼和垃圾，海之眷顾不影响结果 |

简单说：

- `true` / `imba`：不管浮标周围有没有 5×4×5 的开放水域，都当成“符合条件”，所以宝藏能正常参与抽取。
- `imba`：干脆跳过鱼和垃圾，收竿时直接去宝藏表里抽，所以每一竿都是宝藏。

#### `hoebedrockbreaking`

允许用锄头挖基岩（默认关）。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：基岩不可破坏 |
| `true` | 锄头可以挖基岩 |

细节：

- 不产生掉落物，精准采集也没有用。
- 每挖掉 1 块消耗锄头 1 点耐久，耐久附魔正常生效。
- 挖掘耗时等于“镐子挖石头”：钻石锄 + 效率 V + 信标急迫 II 可以在 1 个游戏刻内挖掉。
- 规则名就是全小写的 `hoebedrockbreaking`。

#### `botnoclip`

开启后任何实体都推不动 Carpet 假人（bot），假人会保持原位（默认关）。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：玩家和假人会互相挤开 |
| `true` | 玩家和生物都推不动假人；但假人对玩家仍有推力 |

细节：

- **这是设计行为，不会再改**：`true` 时假人会被排除在“可被推动的实体”之外，玩家、生物、其他假人都推不动它，保证摆好的假人绝对不被碰歪。
- **已知限制**：假人自己仍会把玩家挤开，双方不能完全重叠。原因是推挤有一半在客户端本地计算，客户端把假人当成普通玩家实体，服务端只能拦住自己这一侧的推力；要彻底消除就得全局关闭玩家之间的推挤，会改变其他行为，所以不做。
- 适合“摆好假人后不希望它被碰歪”的场景。
- 规则名全小写 `botnoclip`。

#### `botToolSwap`

假人捡起工具时，如果捡到的比主手上的更好，就自动换到主手（默认关）。物品按原版规则先进入背包，再在同一 tick 换到主手。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：假人保持主手物品 |
| `true` | 主手已有工具时，捡到更好的工具自动换上 |

判断顺序：

1. **经验修补**：两件里只有一件有时，直接换上带经验修补的那件，不再比第 2 步；
2. **附魔总等级**：两件都没有、或都有经验修补时，比较附魔等级之和，诅咒类每级记 **-10**。

细节：

- 只对“工具”生效：有耐久、且不是盔甲/鞘翅这类装备的物品。
- 只处理 Carpet 假人（`/player` 生成的 bot），不影响真实玩家。
- 主手为空时不判断：工具照常留在背包里，不会自动上手。
- 不会无视拾取延迟把地上的物品吸过来：掉落物要等原版拾取延迟结束、真正进入背包后才会触发判断。
- 旧工具会放回假人背包；背包满了才掉在脚边。
- 规则名 `botToolSwap`。

### 安装

1. Minecraft **26.3** + Fabric Loader **0.20.1**（或更高）
2. 安装 [Fabric API](https://modrinth.com/mod/fabric-api) 与 [Carpet](https://modrinth.com/mod/carpet)（26.3 构建）
3. 把 `carpet-dan-addition-*.jar` 放进 `mods/`
4. 启动服务器或单人世界

服务端安装即可生效；单人游戏装进 `mods/` 即可。联机时建议客户端也装上，破坏进度显示更一致。

### 使用

```
/carpet list dan
/carpet fishingTreasureNoOpenWater true
/carpet fishingTreasureNoOpenWater imba
/carpet fishingTreasureNoOpenWater false
/carpet hoebedrockbreaking true
/carpet botnoclip true
/carpet botToolSwap true
```

规则值不区分大小写（`true` / `TRUE` 都可以）。`/carpet list dan` 列出 dan 分类下的全部规则。

### 构建

需要 **JDK 25**：

```bash
./gradlew build
# 产物：build/libs/carpet-dan-addition-1.2.0+mc26.3.jar
```

### 兼容性

- 仅支持 Minecraft 26.3（Carpet 26.3 的 API 与旧版本不兼容）。
- 与 Lithium、FerriteCore 等优化模组无已知冲突。
- 规则以服务端为准；单人游戏作用于内置服务端。

### 许可

MIT

---

## English

### About

Carpet Dan Addition (CDA) is a Carpet addon by hai_dan. Every rule lives in the **dan** category inside `/carpet`, and the defaults keep vanilla behaviour.

### Features

#### `fishingTreasureNoOpenWater`

Restores the pre-1.16 fishing feel: treasure can be caught without a 5×4×5 open water area.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla 1.16+: treasure requires the bobber to be in open water |
| `true` | Ignores the open water requirement; treasure keeps its vanilla weight (about 5%, ~11% with Luck of the Sea III) |
| `imba` | Uses the treasure loot table only: 100% treasure, no fish and no junk; Luck of the Sea has no effect |

In plain terms:

- `true` / `imba`: the bobber is always treated as sitting in open water, so treasure can be rolled anywhere.
- `imba`: skips fish and junk entirely and rolls straight from the treasure table, so every catch is treasure.

#### `hoebedrockbreaking`

Lets a hoe mine bedrock (off by default).

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bedrock cannot be broken |
| `true` | A hoe can break bedrock |

Details:

- No drops, not even with Silk Touch.
- Each broken block costs 1 durability; Unbreaking applies normally.
- Breaking time matches a pickaxe mining stone: a diamond hoe with Efficiency V and Haste II breaks it in 1 game tick.
- The rule name is all lowercase: `hoebedrockbreaking`.

#### `botnoclip`

With this on, no entity can push Carpet fake players (bots) around, so a placed bot stays put (off by default).

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: players and bots push each other |
| `true` | Players and mobs cannot push bots; bots can still push players |

Details:

- **This is intended behaviour and will not change**: with `true`, bots are excluded from the pushable list, so players, mobs and other bots cannot shove them anywhere. A placed bot stays exactly where you put it.
- **Known limitation**: bots still push players, so the two cannot fully overlap. Half of the pushing is computed client-side, where a bot looks like an ordinary player entity; the server can only block its own half. Removing it entirely would mean disabling all player-to-player pushing, which changes other behaviour, so it is left as is.
- Useful when you place a bot at a precise spot and do not want it nudged out of position.
- The rule name is all lowercase: `botnoclip`.

#### `botToolSwap`

When a bot picks up a tool, it equips it if it is better than the current main-hand tool (off by default). The item first enters the inventory through vanilla pickup, then moves to the main hand in the same tick.

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: bots keep whatever is in the main hand |
| `true` | Auto-switch to a better picked-up tool |

Order of comparison:

1. **Mending**: if exactly one of the two has Mending, switch to that one and stop;
2. **Total enchantment level**: if neither or both have Mending, switch to the one with the higher sum of enchantment levels, where every curse level counts as **-10**.

Details:

- Only "tools" are considered: items with durability that are not equipment such as armour or elytra.
- Only Carpet fake players (`/player` bots) are handled; real players are untouched.
- An empty main hand is left alone: the picked-up tool simply stays in the inventory.
- Items are never vacuumed off the ground: vanilla pickup delay and targeting run first, and the comparison only happens after the item has really entered the inventory.
- The old tool goes back into the bot's inventory; it is dropped only when the inventory is full.
- Rule name: `botToolSwap`.

### Installation

1. Minecraft **26.3** + Fabric Loader **0.20.1** (or newer)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) and [Carpet](https://modrinth.com/mod/carpet) (26.3 build)
3. Drop `carpet-dan-addition-*.jar` into `mods/`
4. Start the server or a single-player world

Server-side installation is enough; in single-player just drop it into `mods/`. In multiplayer, installing it on the client too gives consistent breaking progress.

### Usage

```
/carpet list dan
/carpet fishingTreasureNoOpenWater true
/carpet fishingTreasureNoOpenWater imba
/carpet fishingTreasureNoOpenWater false
/carpet hoebedrockbreaking true
/carpet botnoclip true
/carpet botToolSwap true
```

Rule values are case-insensitive (`true` / `TRUE` both work). `/carpet list dan` lists every rule in the dan category.

### Building

Requires **JDK 25**:

```bash
./gradlew build
# output: build/libs/carpet-dan-addition-1.2.0+mc26.3.jar
```

### Compatibility

- Minecraft 26.3 only (the Carpet 26.3 API is not compatible with older versions).
- No known conflicts with Lithium, FerriteCore or similar optimisation mods.
- Rules are server-authoritative; in single-player they apply to the integrated server.

### License

MIT
