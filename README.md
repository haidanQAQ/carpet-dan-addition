# Carpet Dan Addition

A [Carpet](https://github.com/gnembon/fabric-carpet) addon for **Minecraft 26.3** by **hai_dan**.
It brings back the pre-1.16 fishing feel and collects small quality-of-life rules under the `dan` category.

> **Vibe coding 澹版槑**锛氭湰椤圭洰鐨勪唬鐮佸拰鏂囨。鏄湪 AI 杈呭姪涓嬪畬鎴愮殑锛坴ibe coding锛夈€傚凡鍦ㄦ湰鏈?26.3 鏈嶅姟绔仛杩囧熀鏈姛鑳介獙璇侊紝浣嗘病鏈夎鐩栨墍鏈夊満鏅紱浣跨敤鍓嶈鑷璇勪及椋庨櫓锛屽嚭闂娆㈣繋鎻?issue銆?>
> **Vibe-coding notice**: this project was built with AI assistance (vibe coding). It has been smoke-tested on a local 26.3 server, but not exhaustively. Use it at your own risk, and feel free to open an issue if something breaks.

> 鐩墠鍙敮鎸?Minecraft 26.3 / Minecraft 26.3 only.

---

## 涓枃

### 绠€浠?
Carpet Dan Addition锛圕DA锛夋槸 hai_dan 鐨?Carpet 闄勫睘妯＄粍銆傛墍鏈夎鍒欓兘鏀惧湪 `/carpet` 鐨?**dan** 鍒嗙被涓嬶紝榛樿鍊间繚鎸佸師鐗堣涓恒€?
### 鍔熻兘

#### `fishingTreasureNoOpenWater`

杩樺師 1.16 涔嬪墠鐨勯挀楸兼墜鎰燂細涓嶉渶瑕?5脳4脳5 鐨勫紑鏀炬按鍩熶篃鑳介挀鍒板疂钘忋€?
| 鍊?| 琛屼负 |
| --- | --- |
| `false`锛堥粯璁わ級 | 鍘熺増 1.16+锛氬彧鏈夋诞鏍囧浜庡紑鏀炬按鍩熸椂鎵嶅彲鑳介挀鍒板疂钘?|
| `true` | 蹇界暐寮€鏀炬按鍩熼檺鍒讹紝瀹濊棌浠嶆寜鍘熺増鏉冮噸锛堢害 5%锛屾捣涔嬬湻椤?III 绾?11%锛?|
| `imba` | 鍙娇鐢ㄥ疂钘忔垬鍒╁搧琛細100% 瀹濊棌锛屾病鏈夐奔鍜屽瀮鍦撅紝娴蜂箣鐪烽【涓嶅奖鍝嶇粨鏋?|

绠€鍗曡锛?
- `true` / `imba`锛氫笉绠℃诞鏍囧懆鍥存湁娌℃湁 5脳4脳5 鐨勫紑鏀炬按鍩燂紝閮藉綋鎴愨€滅鍚堟潯浠垛€濓紝鎵€浠ュ疂钘忚兘姝ｅ父鍙備笌鎶藉彇銆?- `imba`锛氬共鑴嗚烦杩囬奔鍜屽瀮鍦撅紝鏀剁鏃剁洿鎺ュ幓瀹濊棌琛ㄩ噷鎶斤紝鎵€浠ユ瘡涓€绔块兘鏄疂钘忋€?
#### `hoebedrockbreaking`

鍏佽鐢ㄩ攧澶存寲鍩哄博锛堥粯璁ゅ叧锛夈€?
| 鍊?| 琛屼负 |
| --- | --- |
| `false`锛堥粯璁わ級 | 鍘熺増锛氬熀宀╀笉鍙牬鍧?|
| `true` | 閿勫ご鍙互鎸栧熀宀?|

缁嗚妭锛?
- 涓嶄骇鐢熸帀钀界墿锛岀簿鍑嗛噰闆嗕篃娌℃湁鐢ㄣ€?- 姣忔寲鎺?1 鍧楁秷鑰楅攧澶?1 鐐硅€愪箙锛岃€愪箙闄勯瓟姝ｅ父鐢熸晥銆?- 鎸栨帢鑰楁椂绛変簬鈥滈晲瀛愭寲鐭冲ご鈥濓細閽荤煶閿?+ 鏁堢巼 V + 淇℃爣鎬ヨ揩 II 鍙互鍦?1 涓父鎴忓埢鍐呮寲鎺夈€?- 瑙勫垯鍚嶅氨鏄叏灏忓啓鐨?`hoebedrockbreaking`銆?
#### `botnoclip`

寮€鍚悗浠讳綍瀹炰綋閮芥帹涓嶅姩 Carpet 鍋囦汉锛坆ot锛夛紝鍋囦汉浼氫繚鎸佸師浣嶏紙榛樿鍏筹級銆?
| 鍊?| 琛屼负 |
| --- | --- |
| `false`锛堥粯璁わ級 | 鍘熺増锛氱帺瀹跺拰鍋囦汉浼氫簰鐩告尋寮€ |
| `true` | 鐜╁鍜岀敓鐗╅兘鎺ㄤ笉鍔ㄥ亣浜猴紱浣嗗亣浜哄鐜╁浠嶆湁鎺ㄥ姏 |

缁嗚妭锛?
- **杩欐槸璁捐琛屼负锛屼笉浼氬啀鏀?*锛歚true` 鏃跺亣浜轰細琚帓闄ゅ湪鈥滃彲琚帹鍔ㄧ殑瀹炰綋鈥濅箣澶栵紝鐜╁銆佺敓鐗┿€佸叾浠栧亣浜洪兘鎺ㄤ笉鍔ㄥ畠锛屼繚璇佹憜濂界殑鍋囦汉缁濆涓嶈纰版銆?- **宸茬煡闄愬埗**锛氬亣浜鸿嚜宸变粛浼氭妸鐜╁鎸ゅ紑锛屽弻鏂逛笉鑳藉畬鍏ㄩ噸鍙犮€傚師鍥犳槸鎺ㄦ尋鏈変竴鍗婂湪瀹㈡埛绔湰鍦拌绠楋紝瀹㈡埛绔妸鍋囦汉褰撴垚鏅€氱帺瀹跺疄浣擄紝鏈嶅姟绔彧鑳芥嫤浣忚嚜宸辫繖涓€渚х殑鎺ㄥ姏锛涜褰诲簳娑堥櫎灏卞緱鍏ㄥ眬鍏抽棴鐜╁涔嬮棿鐨勬帹鎸わ紝浼氭敼鍙樺叾浠栬涓猴紝鎵€浠ヤ笉鍋氥€?- 閫傚悎鈥滄憜濂藉亣浜哄悗涓嶅笇鏈涘畠琚姝€濈殑鍦烘櫙銆?- 瑙勫垯鍚嶅叏灏忓啓 `botnoclip`銆?
### 瀹夎

1. Minecraft **26.3** + Fabric Loader **0.20.1**锛堟垨鏇撮珮锛?2. 瀹夎 [Fabric API](https://modrinth.com/mod/fabric-api) 涓?[Carpet](https://modrinth.com/mod/carpet)锛?6.3 鏋勫缓锛?3. 鎶?`carpet-dan-addition-*.jar` 鏀捐繘 `mods/`
4. 鍚姩鏈嶅姟鍣ㄦ垨鍗曚汉涓栫晫

鏈嶅姟绔畨瑁呭嵆鍙敓鏁堬紱鍗曚汉娓告垙瑁呰繘 `mods/` 鍗冲彲銆傝仈鏈烘椂寤鸿瀹㈡埛绔篃瑁呬笂锛岀牬鍧忚繘搴︽樉绀烘洿涓€鑷淬€?
### 浣跨敤

```
/carpet list dan
/carpet fishingTreasureNoOpenWater true
/carpet fishingTreasureNoOpenWater imba
/carpet fishingTreasureNoOpenWater false
/carpet hoebedrockbreaking true
/carpet botnoclip true
```

瑙勫垯鍊间笉鍖哄垎澶у皬鍐欙紙`true` / `TRUE` 閮藉彲浠ワ級銆俙/carpet list dan` 鍒楀嚭 dan 鍒嗙被涓嬬殑鍏ㄩ儴瑙勫垯銆?
### 鏋勫缓

闇€瑕?**JDK 25**锛?
```bash
./gradlew build
# 浜х墿锛歜uild/libs/carpet-dan-addition-1.1.3+mc26.3.jar
```

### 鍏煎鎬?
- 浠呮敮鎸?Minecraft 26.3锛圕arpet 26.3 鐨?API 涓庢棫鐗堟湰涓嶅吋瀹癸級銆?- 涓?Lithium銆丗erriteCore 绛変紭鍖栨ā缁勬棤宸茬煡鍐茬獊銆?- 瑙勫垯浠ユ湇鍔＄涓哄噯锛涘崟浜烘父鎴忎綔鐢ㄤ簬鍐呯疆鏈嶅姟绔€?
### 璁稿彲

MIT

---

## English

### About

Carpet Dan Addition (CDA) is a Carpet addon by hai_dan. Every rule lives in the **dan** category inside `/carpet`, and the defaults keep vanilla behaviour.

### Features

#### `fishingTreasureNoOpenWater`

Restores the pre-1.16 fishing feel: treasure can be caught without a 5脳4脳5 open water area.

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
# output: build/libs/carpet-dan-addition-1.1.3+mc26.3.jar
```

### Compatibility

- Minecraft 26.3 only (the Carpet 26.3 API is not compatible with older versions).
- No known conflicts with Lithium, FerriteCore or similar optimisation mods.
- Rules are server-authoritative; in single-player they apply to the integrated server.

### License

MIT
