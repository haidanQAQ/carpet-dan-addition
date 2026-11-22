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

开启后玩家推不动 Carpet 假人（bot），假人会保持原位（默认关）。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：玩家和假人会互相挤开 |
| `true` | 玩家推不动假人；但假人对玩家仍有推力 |

细节：

- **已知限制**：开启后玩家走路不会推走假人，但假人仍会把玩家挤开，双方不能完全重叠。
- 原因是推挤有一半在客户端本地计算：客户端把假人当成普通玩家实体，服务端只能拦住自己这一侧的推力，无法在不影响其他玩家逻辑的前提下彻底消除。
- 适合“摆好假人后不希望它被碰歪”的场景；如果你能接受玩家之间也完全无推挤，可以再做一个更强的全局版本。
- 规则名全小写 `botnoclip`。

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
```

规则值不区分大小写（`true` / `TRUE` 都可以）。`/carpet list dan` 列出 dan 分类下的全部规则。

### 构建

需要 **JDK 25**：

```bash
./gradlew build
# 产物：build/libs/carpet-dan-addition-1.1.0+mc26.3.jar
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

Stops players from pushing Carpet fake players (bots) around, so a placed bot stays put (off by default).

| Value | Behaviour |
| --- | --- |
| `false` (default) | Vanilla: players and bots push each other |
| `true` | Players cannot push bots; bots can still push players |

Details:

- **Known limitation**: players no longer shove bots away, but bots can still push players, so the two cannot fully overlap.
- Half of the pushing is computed client-side: the client sees a bot as an ordinary player entity, so the server can only block the server-side half. Removing it completely would mean disabling all player-to-player pushing, which changes other behaviour.
- Useful when you place a bot at a precise spot and do not want it nudged out of position. If you are fine with disabling all player-to-player pushing, a stronger global variant could be added later.
- The rule name is all lowercase: `botnoclip`.

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
```

Rule values are case-insensitive (`true` / `TRUE` both work). `/carpet list dan` lists every rule in the dan category.

### Building

Requires **JDK 25**:

```bash
./gradlew build
# output: build/libs/carpet-dan-addition-1.1.0+mc26.3.jar
```

### Compatibility

- Minecraft 26.3 only (the Carpet 26.3 API is not compatible with older versions).
- No known conflicts with Lithium, FerriteCore or similar optimisation mods.
- Rules are server-authoritative; in single-player they apply to the integrated server.

### License

MIT
