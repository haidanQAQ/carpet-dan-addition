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

    public static boolean ignoreOpenWater() {
        return !"false".equalsIgnoreCase(fishingTreasureNoOpenWater);
    }

    public static boolean onlyTreasure() {
        return "imba".equalsIgnoreCase(fishingTreasureNoOpenWater);
    }
}
