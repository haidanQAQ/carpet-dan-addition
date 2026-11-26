# 规则

[English](Rules.md) | 简体中文

所有规则都属于 `dan` 分类，用 `/carpet <规则> <值>` 修改。`/carpet list dan` 可以列出该分类。除非某条规则单独说明，修改立即生效。

## TNTchunkloading

已点燃的 TNT 水平速度超过阈值时，每 tick 会给它所在区块加一张 80 gt 的加载票。加载票的 flags 与末影珍珠票相同，覆盖半径 2 个区块。设为 `false` 关闭；任意非负数字都可以用，列出的可选值只用于 Tab 补全。

- Type: `String`
- Default: `false`
- Options: `false`，或任意非负数字（建议值 `0`、`0.5`、`1.0`、`2.0`）
- Categories: `dan`

```text
/carpet TNTchunkloading 0.5
```

## fishingTreasureNoOpenWater

`false` 保持原版：钓宝藏需要开放水域。`true` 忽略开放水域限制，浮标落在哪里都可能出宝藏，宝藏概率仍然是原版的 5% 权重。`imba` 只使用宝藏战利品表，不出鱼也不出垃圾。

- Type: `String`
- Default: `false`
- Options: `false`、`true`、`imba`
- Categories: `dan`

```text
/carpet fishingTreasureNoOpenWater true
```

## hoebedrockbreaking

用锄头可以破坏基岩。不掉落物品，精准采集也不掉；每块消耗 1 点耐久，受耐久附魔影响。挖掘时间与镐子挖石头一致：使用锄头的材质速度、效率、急迫和方块破坏速度属性，对石头硬度 1.5 和正确工具除数计算。钻石锄带效率 V 和急迫 II 时，每 tick 可以挖掉一块。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## crispydeepslate

把 `minecraft:deepslate` 方块的硬度按 1.5 计算，而不是 3.0。深板岩矿石保留自己的硬度，方块掉落物不变。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## TNTdrag

控制 `PrimedTnt.tick()` 每 tick 应用的空气阻尼系数。`float` 保留 26.2+ 原版行为，系数是单精度 `0.98F` 再扩宽成 double；`doublefloat` 使用双精度字面量 `0.98D`，与 26.2 及更早版本一致。

- Type: `String`
- Default: `float`
- Options: `float`、`doublefloat`
- Categories: `dan`

## mergeTNTdebug

开启 `/mergeTNT <pos> [n] [m]`。生成一枚携带 `n` 个 TNT 当量的点燃 TNT，在自身位置爆炸 `n` 次；多出来的每一次都是独立的 4.0F 爆炸，与 Carpet `mergeTNT` 的重复方式一致。它不读也不写 `mergeTNT`、`mergeTNTPro`、`mergeTNTMax` 的计数，所以这些规则开着、关着或没装都能用。

`pos` 使用原版坐标写法：绝对坐标（`0 70 0`）、相对坐标（`~ ~1 ~`）或局部坐标（`^ ^ ^`），带原版 Tab 补全。`n` 默认 1，不设上限。`m` 是引信 tick 数，默认 80。生成的 TNT 初始速度为 0，会在指定坐标停留到重力生效。当量只存在内存里；如果这枚 TNT 被合并规则并进别的 TNT，当量会丢失。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

```text
/carpet mergeTNTdebug true
/mergeTNT 0 70 0 5 80
/mergeTNT ~ ~1 ~ 5 80
```

## botnoclip

任何实体都推不动 Carpet 假人，包括玩家、生物和其他假人，放好的假人会保持原位。假人对玩家仍有推力，所以假人和玩家不能完全重叠。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## botToolSwap

假人捡起工具时，会和主手工具比较。只比较同类工具：镐、斧、锹、锄、剑按物品标签判断，剪刀按物品判断。如果两件中只有一件有经验修补，优先换那一件；否则比较附魔总等级，诅咒每级记 -10，等级高的胜出。

另外，主手工具剩余耐久只剩 10% 或更少（至少 1 点）时，会换成背包里同种工具中耐久最高的一件。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## botmemo

开启 `/botmemo <名字> save` 与 `/botmemo <名字> run`。`save` 记录假人的位置、朝向和当前动作；`run` 恢复这些状态，假人不在线时会先在记录的位置生成。动作在假人完整加入后才应用，所以恢复的 `use continuous` 仍然保留原版 4 gt 冷却。

记录写到 `config/carpet-dan-addition/botmemo.json`，服务端重启后仍然存在。背包、血量、药水效果和坐骑不会被保存。

- Type: `Boolean`
- Default: `true`
- Options: `true`、`false`
- Categories: `dan`

```text
/botmemo hai_dan save
/botmemo hai_dan run
```

## botChineseName

允许 Carpet 的 `/player` 命令使用带引号的中文名：

```text
/player "测试" spawn
```

名字参数始终是原版的字符串类型。规则关闭时，包含非 ASCII 字符的 `/player` 命令会在执行时被拒绝，所以切换规则立即生效。引号必须加，因为不加引号时参数仍然只接受 ASCII。客户端也安装本 mod 时，Tab 补全会自动补上引号。

- Type: `Boolean`
- Default: `true`
- Options: `true`、`false`
- Categories: `dan`

## deathchest

玩家死亡后，物品会装进死亡点的箱子，而不是掉在地上：最多 27 组装 1 个箱子，更多时上下叠 2 个。箱子不受保护、不标主人、不存经验。创造和旁观模式玩家、以及开启 `keepInventory` 时跳过。

如果死亡点在世界高度限制之外，会使用最近的有效高度；找不到空气位置时，最后会覆盖方块。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## leashableVillagers

村民和流浪商人可以像其他可拴生物一样被拴绳拴住并拉着走。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## villagerneverstoptrading

冻结每笔交易的已用次数，交易不会锁定，需求也不会增长，价格停在当前值。对村民和流浪商人生效。每次交易只做常数次判断，不使用命令，也不改 NBT。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## noBatSpawning

把 Carpet 的 ambient 刷怪速率设为 0，蝙蝠不再自然生成。已有的蝙蝠、刷怪蛋、刷怪笼和命令不受影响。幻翼不在此规则范围内，用原版 `/gamerule spawn_phantoms false`。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`

## noAquaticSpawning

把 `water_creature`（鱼、海豚）、`water_ambient`（鱿鱼）、`underground_water_creature` 和 `axolotls` 的 Carpet 刷怪速率设为 0。已有的生物、刷怪蛋、刷怪笼和命令不受影响。

- Type: `Boolean`
- Default: `false`
- Options: `true`、`false`
- Categories: `dan`
