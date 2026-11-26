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
| [`botToolSwap`](#bottoolswap) | `false` | 假人捡到更好的工具自动换上，快坏时换同类新工具 |
| [`noBatSpawning`](#nobatspawning) | `false` | 不再自然生成蝙蝠 |
| [`noAquaticSpawning`](#noaquaticspawning) | `false` | 不再自然生成鱼、鱿鱼和美西螈 |
| [`botmemo`](#botmemo) | `true` | 保存/恢复假人的位置、朝向和动作 |
| [`botChineseName`](#botchinesename) | `true` | `/player` 接受带引号的中文名 |
| [`deathchest`](#deathchest) | `false` | 死亡后物品放进死亡点的箱子 |
| [`leashableVillagers`](#leashablevillagers) | `false` | 村民和流浪商人可以被拴绳拴住 |
| [`villagerneverstoptrading`](#villagerneverstoptrading) | `false` | 交易永不锁定；已用次数冻结，价格不再上涨 |
| [`crispydeepslate`](#crispydeepslate) | `false` | 深板岩按硬度 1.5 挖 |
| [`TNTdrag`](#tntdrag) | `float` | 点燃 TNT 的空气阻尼：`float`（26.2+）或 `doublefloat`（26.2 前） |

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

假人捡起工具时，如果捡到的比主手上的更好，就自动换到主手；主手工具快坏时，也会换成背包里同种工具中耐久最高的一件。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：假人保持主手物品 |
| `true` | 捡到更好的工具自动换上，快坏时换同类新工具 |

判断顺序：

1. **经验修补**：两件里只有一件有时，直接换上带经验修补的那件，不再比第 2 步。
2. **附魔总等级**：两件都没有、或都有经验修补时，比较附魔等级之和，诅咒类每级记 **-10**。
3. **材料等级**：总分相同（通常是两件都没附魔）时，比最大耐久，木 < 石 < 铁 < 钻石 < 下界合金。

细节：

- 只对“工具”生效：有耐久、且不是盔甲/鞘翅这类装备的物品。
- 只在同类工具之间比较：镐对镐、斧对斧，锹、锄、剑、剪刀同理；弓不会把钓竿换掉。
- 只处理 Carpet 假人，不影响真实玩家。
- 主手为空时不判断：工具照常留在背包里，不会自动上手。
- 不会无视拾取延迟把地上的物品吸过来：掉落物要等原版拾取延迟结束、真正进入背包后才会触发判断。
- 旧工具会放回假人背包；背包满了才掉在脚边。
- 主手工具剩余耐久只剩 10% 或更少（至少 1 点）时，会换成背包里同种工具中耐久最高的一件；新工具必须比旧的至少多 10% 耐久才换。工具刚好坏掉时，会立刻装备背包里最好的一件。

### `noBatSpawning`

禁止蝙蝠自然生成。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版行为：蝙蝠在黑暗处生成 |
| `true` | 不再自然生成蝙蝠 |

细节：

- 开启时把 ambient 刷怪速率设为 0（关掉再开会再写一次）。原版 ambient 类别只有蝙蝠；开启期间手动 `/spawn rates ambient ...` 不会被覆盖。
- 刷怪蛋、刷怪笼和 `/summon` 仍然可用，已经存在的蝙蝠不受影响。
- 幻翼不在这条规则范围内，请用原版游戏规则：`/gamerule spawn_phantoms false`。

### `noAquaticSpawning`

调用 Carpet 自己的刷怪速率表（和 `/spawn rates` 写的是同一张表），把水生生物的刷怪尝试设为 0。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版行为：水生生物自然生成 |
| `true` | 不再自然生成 water_creature、water_ambient、underground_water_creature 和 axolotls |

细节：

- 类别：`water_creature`（鱼、海豚）、`water_ambient`（鱿鱼、发光鱿鱼）、`underground_water_creature`、`axolotls`。
- 规则开启时只写一次（关掉再打开会再写一次）；开启期间手动 `/spawn rates ...` 不会被覆盖，和平时一样用。关闭规则时还原开启前记录的值。
- 只影响自然生成：已有的生物、刷怪蛋、刷怪笼、鱼桶和 `/summon` 都不受影响。
- 海豚和鱼同属 `water_creature`，所以也会一起关闭。

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
- 记录写在 `config/carpet-dan-addition/botmemo.json`，重启服务器后仍然保留；不保存背包、坐骑、血量和药水效果。
- 中文名要加引号，例如 `/botmemo "测试" save`。

### `botChineseName`

允许 Carpet 的 `/player` 命令接受带引号的中文名。

| 值 | 行为 |
| --- | --- |
| `true`（默认） | `/player "测试" spawn` 可用 |
| `false` | 拒绝带中文名的 `/player` 命令 |

细节：

- 名字必须加引号；不加引号的中文仍会被原版命令解析器拒绝。
- 切换规则立即生效，不需要重启。
- 参数类型仍是原版 `brigadier:string`，原版客户端能正常解析，不需要装客户端模组。

### `deathchest`

死亡后把物品放进死亡点的箱子里，而不是散落在地上。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：物品掉在地上 |
| `true` | 物品放进死亡位置的箱子 |

细节：

- 最多 27 组进一个箱子；28–54 组放两个箱子，沿 Y 轴上下叠放。
- 带消失诅咒的物品照常销毁；不保存经验；箱子没有保护，也不标记主人。
- 空位先向上找、再向下找；只认空气，水和岩浆都不算空位。
- 死在限高之外时，从最近的高度限制开始找（上方从上往下，下方从下往上）。
- 找不到空位就覆盖原地方块；遇到方块实体不覆盖，继续向上找。
- 箱子是普通箱子，漏斗、大箱子、`/data` 都能正常用。

### `leashableVillagers`

让村民和流浪商人可以被拴绳拴住并拉着走。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：村民不能被拴绳拴住 |
| `true` | 村民和流浪商人可以被拴住并拉动 |

细节：

- 只改“能否拴”这一处判断；拴绳的物理、距离限制、断裂、拴栅栏等行为与其他可拴生物一致。
- 村民和流浪商人（都继承 `AbstractVillager`）都生效。
- 需要用剪刀剪断拴绳时，和原版其他被拴生物一样操作。
- 村民仍会正常寻路，走远时会被绳子拉回来。

### `villagerneverstoptrading`

取消村民和流浪商人的交易次数上限。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：交易达到次数上限后锁定，价格随需求上涨 |
| `true` | 交易次数无限：已用次数冻结，报价永不锁定，价格停在当前值 |

细节：

- 对村民、流浪商人以及任何使用原版 `MerchantOffer` 的商人都生效。
- `true` 冻结已用次数，交易不会锁定，靠补货更新 `demand` 的流程也不会再跑，所以价格停在当前值（治愈僵尸村民的折扣仍然有效）。
- 算力：每次交易只做几次常数判断，O(1)。不执行命令、不改 NBT——不像数据包方案那样在每笔交易时对附近所有村民执行函数、重写 `Offers.Recipes[].uses`。
- 价格仍受报价自身的基础价与倍率限制。

### `crispydeepslate`

把深板岩方块按硬度 1.5（石头）来挖。

| 值 | 行为 |
| --- | --- |
| `false`（默认） | 原版：深板岩硬度 3.0 |
| `true` | 深板岩按石头速度挖（硬度 1.5） |

细节：

- 只影响 `minecraft:deepslate` 方块，深板岩矿石保留自己的硬度，掉落物不变。
- 进度公式和原版一致，工具、效率、急迫、挖掘疲劳、水下/空中惩罚都照常生效。
- 没装本 addon 的客户端仍按原速度显示破坏动画，服务端会提前打掉方块；单人和集成服两端一致。

### `TNTdrag`

控制已点燃 TNT 每 tick 的空气阻尼精度。

| 值 | 行为 |
| --- | --- |
| `float`（默认） | 26.2+ 原版：`PrimedTnt.tick()` 用 `Entity#getAirDrag()` 的 `0.98F` 扩宽成 double |
| `doublefloat` | 26.2 之前：缩放参数使用精确的双精度字面量 `0.98D` |

细节：

- mixin 只替换 `PrimedTnt.tick()` 里那一次 `Vec3.scale(double)` 的参数；重力和水平 `0.7` / `-0.5` 摩擦走 `multiply(DDD)`，不受影响。
- 差别很小（0.98F ≈ 0.9800000190734863，0.98D = 0.98），但长引信会累积，这正是 26.2 之前的原版行为。

## 命令

### 中文名假人

Carpet 的 `/player` 原本只接受 ASCII 名字。`botChineseName` 规则开启时（默认），名字参数会被换成可加引号的字符串，所以中文名要**带引号**写：

```
/player "测试" spawn
/player "测试" kill
```

不加引号的中文仍会被原版命令解析器拒绝（`/player 测试 spawn` 会报错）。引号形式是原版命令语法，原版客户端也能正常解析，不需要装客户端模组。

用 `/carpet botChineseName false` 可以关闭：关闭后带中文名的 `/player` 命令会被拒绝并提示，改回 `true` 立即生效。

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
/carpet noBatSpawning true
/carpet noAquaticSpawning true
/carpet botmemo true
/carpet botChineseName true
/carpet leashableVillagers true
/carpet villagerneverstoptrading true
/carpet crispydeepslate true
/carpet TNTdrag doublefloat
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
# 产物：build/libs/carpet-dan-addition-1.5.10+mc26.3.jar
```

## 兼容性

- 仅支持 Minecraft 26.3（Carpet 26.3 的 API 与旧版本不兼容）。
- 与 Lithium、FerriteCore 等优化模组无已知冲突。
- 规则以服务端为准；单人游戏作用于内置服务端。

## 许可

MIT，见 [LICENSE](LICENSE)。
