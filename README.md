# Carpet Dan Addition

A [Carpet](https://github.com/gnembon/fabric-carpet) addon for **Minecraft 26.3** by **hai_dan**.
It brings back the pre-1.16 fishing feel and collects small quality-of-life rules under the `dan` category.

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

实现方式：

- `true` / `imba`：让 `FishingHookPredicate#matches` 恒为 `true`，`in_open_water` 条件永远通过。
- `imba`：把 `FishingHook#retrieve` 中的战利品表从 `gameplay/fishing` 重定向到 `gameplay/fishing/treasure`。

### 安装

1. Minecraft **26.3** + Fabric Loader **0.20.1**（或更高）
2. 安装 [Fabric API](https://modrinth.com/mod/fabric-api) 与 [Carpet](https://modrinth.com/mod/carpet)（26.3 构建）
3. 把 `carpet-dan-addition-*.jar` 放进 `mods/`
4. 启动服务器或单人世界

服务端安装即可生效；客户端装不装都不影响联机。

### 使用

```
/carpet list dan
/carpet fishingTreasureNoOpenWater true
/carpet fishingTreasureNoOpenWater imba
/carpet fishingTreasureNoOpenWater false
```

规则值不区分大小写（`true` / `TRUE` 都可以）。`/carpet list dan` 列出 dan 分类下的全部规则。

### 构建

需要 **JDK 25**：

```bash
./gradlew build
# 产物：build/libs/carpet-dan-addition-1.0.0+mc26.3.jar
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

How it works:

- `true` / `imba`: forces `FishingHookPredicate#matches` to return `true`, so the `in_open_water` condition always passes.
- `imba`: redirects the loot table in `FishingHook#retrieve` from `gameplay/fishing` to `gameplay/fishing/treasure`.

### Installation

1. Minecraft **26.3** + Fabric Loader **0.20.1** (or newer)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) and [Carpet](https://modrinth.com/mod/carpet) (26.3 build)
3. Drop `carpet-dan-addition-*.jar` into `mods/`
4. Start the server or a single-player world

Server-side installation is enough; clients do not need the mod to join.

### Usage

```
/carpet list dan
/carpet fishingTreasureNoOpenWater true
/carpet fishingTreasureNoOpenWater imba
/carpet fishingTreasureNoOpenWater false
```

Rule values are case-insensitive (`true` / `TRUE` both work). `/carpet list dan` lists every rule in the dan category.

### Building

Requires **JDK 25**:

```bash
./gradlew build
# output: build/libs/carpet-dan-addition-1.0.0+mc26.3.jar
```

### Compatibility

- Minecraft 26.3 only (the Carpet 26.3 API is not compatible with older versions).
- No known conflicts with Lithium, FerriteCore or similar optimisation mods.
- Rules are server-authoritative; in single-player they apply to the integrated server.

### License

MIT
