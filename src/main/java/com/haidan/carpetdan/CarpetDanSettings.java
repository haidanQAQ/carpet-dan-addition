package com.haidan.carpetdan;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.commands.CommandSourceStack;

public final class CarpetDanSettings {
    private CarpetDanSettings() {
    }

    /**
     * Cached threshold for {@code TNTchunkloading}. -1 means the rule is off. Kept in sync by
     * {@link TntChunkLoadingValidator} and by {@link #tntChunkLoadingThreshold()}, which only
     * re-parses when the rule string changes, so the TNT tick path never calls Double.parseDouble.
     */
    public static double tntChunkLoadingThreshold = -1.0D;
    private static String tntChunkLoadingLastSeen = null;

    /**
     * false  - off: no chunk-loading logic runs.
     * number - primed TNT whose horizontal speed exceeds this many blocks/tick places an 80 gt
     *          loading ticket (ender-pearl equivalent) on its current chunk every tick.
     *
     * <p>Any non-negative finite double is accepted, e.g. 0, 0.5, 1.25, 10. The listed options are
     * only for tab completion.</p>
     */
    @Rule(
            categories = {"dan"},
            options = {"false", "0.5", "1.0", "2.0"},
            strict = false,
            validators = TntChunkLoadingValidator.class)
    public static String TNTchunkloading = "false";

    public static final class TntChunkLoadingValidator extends Validator<String> {
        @Override
        public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
            if ("false".equalsIgnoreCase(newValue)) {
                tntChunkLoadingThreshold = -1.0D;
                return "false";
            }
            try {
                double value = Double.parseDouble(newValue);
                if (Double.isFinite(value) && value >= 0.0D) {
                    tntChunkLoadingThreshold = value;
                    return newValue;
                }
            } catch (NumberFormatException ignored) {
            }
            return null;
        }

        @Override
        public String description() {
            return "Must be 'false' or a non-negative number";
        }
    }

    /**
     * Hot path used by the TNT mixin. A String reference comparison is enough: Carpet only replaces
     * the field when the rule actually changes, so this parses at most once per change.
     */
    public static double tntChunkLoadingThreshold() {
        String value = TNTchunkloading;
        if (value != tntChunkLoadingLastSeen) {
            tntChunkLoadingLastSeen = value;
            tntChunkLoadingThreshold = parseTntChunkLoadingThreshold(value);
        }
        return tntChunkLoadingThreshold;
    }

    public static double parseTntChunkLoadingThreshold(String value) {
        if (value == null || "false".equalsIgnoreCase(value)) {
            return -1.0D;
        }
        try {
            double parsed = Double.parseDouble(value);
            return Double.isFinite(parsed) && parsed >= 0.0D ? parsed : -1.0D;
        } catch (NumberFormatException ignored) {
            return -1.0D;
        }
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
     * false - vanilla: deepslate has hardness 3.0, twice as slow to mine as stone.
     * true  - the deepslate block is mined as if its hardness were stone's 1.5.
     *         Deepslate ores keep their own hardness.
     */
    @Rule(categories = {"dan"})
    public static boolean crispydeepslate = false;

    /**
     * float       - vanilla 26.2+: PrimedTnt.tick() scales its velocity by Entity#getAirDrag(),
     *               a float 0.98F that is widened to double for the scale() call.
     * doublefloat - pre-26.2 precision: the scale argument is the exact double literal 0.98D.
     */
    @Rule(categories = {"dan"}, options = {"float", "doublefloat"})
    public static String TNTdrag = "float";

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
     *         Also, when the held tool has 10% durability or less left (minimum 1 point),
     *         swap it for the same tool with the most durability in the inventory.
     */
    @Rule(categories = {"dan"})
    public static boolean botToolSwap = false;

    /**
     * false - vanilla: bats spawn naturally in dark places.
     * true  - natural bat spawning is disabled; spawn eggs, spawners and
     *         commands still work. Phantoms are covered by the vanilla
     *         /gamerule spawn_phantoms.
     */
    @Rule(categories = {"dan"})
    public static boolean noBatSpawning = false;

    /**
     * false - vanilla: aquatic mobs keep spawning naturally.
     * true  - natural spawning of water_creature (fish, dolphins), water_ambient (squid),
     *         underground_water_creature and axolotls is disabled through Carpet's spawn rate
     *         table (the same thing /spawn rates writes to). Existing mobs, spawn eggs,
     *         spawners and commands are unaffected.
     */
    @Rule(categories = {"dan"})
    public static boolean noAquaticSpawning = false;

    /**
     * true  - /botmemo is available: save the position, rotation and actions of a bot,
     *         then restore them later (also spawning the bot if it is offline).
     * false - the command is disabled.
     */
    @Rule(categories = {"dan"})
    public static boolean botmemo = true;

    /**
     * true  - /player accepts quoted Unicode names such as "测试".
     * false - the vanilla name argument is kept (ASCII only).
     *
     * <p>The command tree is built once at startup, so a change takes effect after a restart
     * or reload.</p>
     */
    @Rule(categories = {"dan"})
    public static boolean botChineseName = true;

    /**
     * false - vanilla: a dead player's items are scattered on the ground.
     * true  - a dead player's items are stored in one or two chests at the death spot.
     *         No protection, no owner label, no experience storage. Chests that do not fit
     *         inside the world height limits are overwritten into the ground as a last resort.
     */
    @Rule(categories = {"dan"})
    public static boolean deathchest = false;

    /**
     * false - vanilla: villagers and wandering traders cannot be leashed.
     * true  - a lead can be attached to villagers and wandering traders, and they
     *         can be pulled around like other leashable mobs.
     */
    @Rule(categories = {"dan"})
    public static boolean leashableVillagers = false;

    /**
     * false - vanilla: a trade locks after its max uses.
     * true  - villagers and wandering traders never run out of trades: the used count is
     *         frozen, so every offer stays available forever and demand never grows.
     */
    @Rule(categories = {"dan"})
    public static boolean villagerneverstoptrading = false;

    /**
     * false - /mergedTNT is disabled.
     * true  - /mergedTNT <x> <y> <z> [n] [m] spawns a primed TNT that explodes n times, the
     *         same way mergeTNT does (each extra pass is a separate 4.0F explosion at the
     *         TNT's position). It works without mergeTNT, mergeTNTPro or mergeTNTMax.
     */
    @Rule(categories = {"dan"})
    public static boolean mergeTNTdebug = false;

    public static boolean ignoreOpenWater() {
        return !"false".equalsIgnoreCase(fishingTreasureNoOpenWater);
    }

    public static boolean onlyTreasure() {
        return "imba".equalsIgnoreCase(fishingTreasureNoOpenWater);
    }

    public static boolean hoeBedrockBreaking() {
        return hoebedrockbreaking;
    }

    public static boolean crispyDeepslate() {
        return crispydeepslate;
    }

    public static boolean tntDoubleDrag() {
        return "doublefloat".equalsIgnoreCase(TNTdrag);
    }

    public static boolean botNoClip() {
        return botnoclip;
    }

    public static boolean botToolSwapEnabled() {
        return botToolSwap;
    }

    public static boolean noBatSpawningEnabled() {
        return noBatSpawning;
    }

    public static boolean noAquaticSpawningEnabled() {
        return noAquaticSpawning;
    }

    public static boolean botMemoEnabled() {
        return botmemo;
    }

    public static boolean botChineseNameEnabled() {
        return botChineseName;
    }

    public static boolean deathChestEnabled() {
        return deathchest;
    }

    public static boolean leashableVillagersEnabled() {
        return leashableVillagers;
    }

    public static boolean villagerNeverStopTrading() {
        return villagerneverstoptrading;
    }

    public static boolean mergeTntDebugEnabled() {
        return mergeTNTdebug;
    }
}
