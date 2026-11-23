package com.haidan.carpetdan;

import carpet.api.settings.Rule;

public final class CarpetDanSettings {
    private CarpetDanSettings() {
    }

    /**
     * false - vanilla 1.16+ behaviour: treasure only with an open water area.
     * true  - old behaviour: ignore the open water requirement, treasure chance stays 5%.
     * imba  - only the treasure loot table: no fish, no junk.
     */
    @Rule(categories = {"dan"}, options = {"false", "true", "imba"})
    public static String fishingTreasureNoOpenWater = "false";

    /**
     * false - vanilla: bedrock cannot be broken.
     * true  - a hoe can break bedrock: no drops, 1 durability per block (Unbreaking applies),
     *         and the breaking time equals a pickaxe mining stone.
     */
    @Rule(categories = {"dan"})
    public static boolean hoebedrockbreaking = false;

    /**
     * false - vanilla: bots and players push each other.
     * true  - Carpet fake players (bots) and players no longer push each other,
     *         so a bot stays where it was placed when a player walks into it.
     */
    @Rule(categories = {"dan"})
    public static boolean botnoclip = false;

    /**
     * false - vanilla: bots keep whatever they are holding.
     * true  - when a bot picks up a tool, compare it with the main hand tool:
     *         1) if exactly one of them has Mending, switch to that one;
     *         2) otherwise switch if the picked tool has a higher total enchantment
     *            level, where every curse level counts as -10.
     */
    @Rule(categories = {"dan"})
    public static boolean botToolSwap = false;

    /**
     * false - vanilla: phantoms and bats spawn naturally.
     * true  - natural spawning of phantoms and bats is disabled; spawn eggs and
     *         commands still work.
     */
    @Rule(categories = {"dan"})
    public static boolean noPhantomBatSpawning = false;

    public static boolean ignoreOpenWater() {
        return !"false".equalsIgnoreCase(fishingTreasureNoOpenWater);
    }

    public static boolean onlyTreasure() {
        return "imba".equalsIgnoreCase(fishingTreasureNoOpenWater);
    }

    public static boolean hoeBedrockBreaking() {
        return hoebedrockbreaking;
    }

    public static boolean botNoClip() {
        return botnoclip;
    }

    public static boolean botToolSwapEnabled() {
        return botToolSwap;
    }

    public static boolean noPhantomBatSpawningEnabled() {
        return noPhantomBatSpawning;
    }
}
