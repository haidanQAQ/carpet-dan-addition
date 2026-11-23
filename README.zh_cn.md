# Carpet Dan Addition

[English](README.md) | **简体中文**

[![Release](https://img.shields.io/github/v/release/haidanQAQ/carpet-dan-addition?label=release)](https://github.com/haidanQAQ/carpet-dan-addition/releases)
[![License](https://img.shields.io/github/license/haidanQAQ/carpet-dan-addition)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-26.3-62b47a)](https://www.minecraft.net/)
[![Issues](https://img.shields.io/github/issues/haidanQAQ/carpet-dan-addition)](https://github.com/haidanQAQ/carpet-dan-addition/issues)

一个面向 **Minecraft 26.3** 的小型 [Carpet](https://github.com/gnembon/fabric-carpet) 扩展，收集了几条对原版玩法影响很小、适合单人生电和中小型技术服的规则。

> **Vibe coding 声明**：本项目的代码和文档是在 AI 辅助下完成的（vibe coding）。已在本机 26.3 服务端做过基本功能验证，但没有覆盖所有场景；使用前请自行评估风险，出问题欢迎提 issue。

## 环境要求

| | |
| --- | --- |
| Minecraft | **仅 26.3** |
| Fabric Loader | 0.20.1 或更高 |
| Java | 25 |
| 前置模组 | [Fabric API](https://modrinth.com/mod/fabric-api)、[Carpet](https://modrinth.com/mod/carpet)（26.3 构建） |

## 规则一览

所有规则都在 `dan` 分类下，游戏内用 `/carpet list dan` 查看。

| 规则 | 默认值 | 作用 |
| --- | --- | --- |
| [`fishingTreasureNoOpenWater`](#fishingtreasurenoopenwater) | `false` | 还原 1.16 前钓鱼：不需要 5×4×5 开放水域 |
| [`hoebedrockbreaking`](#hoebedrockbreaking) | `false` | 锄头可以破坏基岩，无掉落，每块 1 耐久 |
| [`botnoclip`](#botnoclip) | `false` | 任何实体都推不动假人 |
| [`botToolSwap`](#bottoolswap) | `false` | 假人捡到更好的工具自动换上 |
| [`noPhantomBatSpawning`](#nophantombatspawning) | `false` | 不再自然生成幻翼与蝙蝠 |
| [`botmemo`](#botmemo) | `true` | 保存/恢复假人的位置、朝向和动作 |

### `fishingTreasureNoOpenWater`

还原 1.16 之前的钓鱼手感：不需要 5×4×5 的开放水域也能钓到宝藏。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：宝藏需要开放水域 |
| `true` | 忽略开放水域限制，宝藏概率保持原版 5% |
| `imba` | 只走宝藏表：不出鱼、不出垃圾 |

细节：

- 26.3 原版钓鱼是一张表里的三个权重条目（鱼 / 垃圾 / 宝藏），宝藏条目带 `in_open_water` 条件；`true` 只去掉这个条件，所以鱼和垃圾照常出。
- `imba` 把钓鱼战利品表直接重定向到宝藏表，每一竿都是宝藏；这个模式下海之眷顾无效。
- 任意水域都生效，包括 1×1 的小水坑。

### `hoebedrockbreaking`

用锄头可以破坏基岩。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：基岩不可破坏 |
| `true` | 锄头可以破坏基岩 |

细节：

- 基岩没有战利品表，所以什么都不掉，精准采集也不掉。
- 每块消耗 1 点耐久，耐久附魔正常生效。
- 挖掘时间等于镐子挖石头；钻石锄或更好，配合效率 V 和信标急迫 II，可以 1 游戏刻破坏。
- 只有锄头触发；其他工具仍然挖不动基岩。

### `botnoclip`

任何实体都推不动 Carpet 假人（bot），放好的假人会保持原位。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：假人和玩家会互相挤 |
| `true` | 玩家、生物、其他假人都推不动假人 |

细节：

- **这是设计行为，不会再改**：`true` 时假人被排除在“可被推动的实体”之外。
- **已知限制**：假人仍会把玩家挤开，双方不能完全重叠。原因是推挤有一半在客户端本地计算，客户端把假人当成普通玩家实体，服务端只能拦住自己这一侧的推力。

### `botToolSwap`

假人捡起工具时，如果捡到的比主手上的更好，就自动换到主手。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：假人保持主手物品 |
| `true` | 捡到更好的工具自动换上 |

判断顺序：

1. **经验修补**：两件里只有一件有时，直接换上带经验修补的那件，不再比第 2 步。
2. **附魔总等级**：两件都没有、或都有经验修补时，比较附魔等级之和，诅咒类每级记 **-10**。

细节：

- 只对“工具”生效：有耐久、且不是盔甲/鞘翅这类装备的物品。
- 只处理 Carpet 假人，不影响真实玩家。
- 主手为空时不判断：工具照常留在背包里，不会自动上手。
- 不会无视拾取延迟把地上的物品吸过来：掉落物要等原版拾取延迟结束、真正进入背包后才会触发判断。
- 旧工具会放回假人背包；背包满了才掉在脚边。

### `noPhantomBatSpawning`

禁止幻翼与蝙蝠自然生成。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版行为：失眠后夜晚生成幻翼，黑暗洞穴生成蝙蝠 |
| `true` | 不再自然生成幻翼和蝙蝠 |

细节：

- 同时关掉两条原版生成路径：幻翼生成器（玩家连续几天不睡觉后才会尝试生成）和蝙蝠的生成检查。
- 刷怪蛋和 `/summon` 仍然可用，已经存在的生物不受影响。
- 刷怪笼生成的蝙蝠不受影响；只拦自然生成。

### `botmemo`

记录假人的位置、朝向和当前动作，之后一键恢复。

| 值 | 行为 |
| --- | --- |
| `true`（默认） | 可以使用 `/botmemo` |
| `false` | 命令被禁用 |

细节：

- `/botmemo <名字> save` 记住假人在哪、朝哪、正在做什么（`use`、`attack`、`move` 等）；再次 save 会覆盖旧记录。
- `/botmemo <名字> run` 把假人传送回去并恢复保存的动作。
- 假人不在线时，`run` 会直接在记录的位置生成它（同维度、同朝向、同游戏模式），再恢复动作。
- 记录只在内存里，重启服务器后丢失；不保存背包、坐骑、血量和药水效果。
- 中文名要加引号，例如 `/botmemo "测试" save`。

## 命令

### 中文名假人

Carpet 的 `/player` 原本只接受 ASCII 名字。本模组把名字参数换成了可加引号的字符串，所以中文名要**带引号**写：

```
/player "测试" spawn
/player "测试" kill
```

不加引号的中文仍会被原版命令解析器拒绝（`/player 测试 spawn` 会报错）。引号形式是原版命令语法，原版客户端也能正常解析，不需要装客户端模组。

### 让假人丢出全部物品

这个功能不需要单独加规则，Carpet 本身就有命令：

```
/player <名字> dropStack all
```

`dropStack all` 会把假人背包每个格子里的整组物品都丢出来；`/player <名字> drop all` 则是每格只丢 1 个。

### 假人状态记忆

```
/botmemo <名字> save
/botmemo <名字> run
```

`save` 记录假人的位置、朝向和当前动作；`run` 恢复这些状态，假人不在线时会直接生成。需要 `botmemo` 规则（默认开启）。中文名要加引号。

## 安装

1. Minecraft **26.3** + Fabric Loader **0.20.1** 或更高。
2. 安装 [Fabric API](https://modrinth.com/mod/fabric-api) 与 [Carpet](https://modrinth.com/mod/carpet)（26.3 构建）。
3. 把 `carpet-dan-addition-*.jar` 放进 `mods/`。
4. 启动服务器或单人世界。

服务端安装即可生效；单人游戏装进 `mods/` 即可。联机时建议客户端也装上，破坏进度显示更一致。

## 使用

```
/carpet list dan
/carpet fishingTreasureNoOpenWater true
/carpet fishingTreasureNoOpenWater imba
/carpet fishingTreasureNoOpenWater false
/carpet hoebedrockbreaking true
/carpet botnoclip true
/carpet botToolSwap true
/carpet noPhantomBatSpawning true
/carpet botmemo true
/player "测试" spawn
/botmemo "测试" save
/botmemo "测试" run
/player hai_dan dropStack all
```

规则值不区分大小写（`true` / `TRUE` 都可以）。`/carpet setDefault` 会持久化，普通的 `/carpet <规则> <值>` 是临时的、重启后恢复默认。

## 构建

需要 **JDK 25**：

```bash
./gradlew build
# 产物：build/libs/carpet-dan-addition-1.5.0+mc26.3.jar
```

## 兼容性

- 仅支持 Minecraft 26.3（Carpet 26.3 的 API 与旧版本不兼容）。
- 与 Lithium、FerriteCore 等优化模组无已知冲突。
- 规则以服务端为准；单人游戏作用于内置服务端。

## 许可

MIT，见 [LICENSE](LICENSE)。
