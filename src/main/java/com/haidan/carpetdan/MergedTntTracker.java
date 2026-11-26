package com.haidan.carpetdan;

import net.minecraft.world.entity.item.PrimedTnt;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Remembers how many TNT-equivalents a {@code /mergedTNT} primed TNT carries.
 *
 * <p>Kept outside of the entity (a weak map instead of a mixin field) so the addon does not need
 * to touch Carpet's or REMS' own merge counters. {@code PrimedTntMixin} reads this map in
 * {@code explode()} and fires the remaining explosions.</p>
 */
public final class MergedTntTracker {
    private static final Map<PrimedTnt, Integer> COUNTS = new WeakHashMap<>();

    private MergedTntTracker() {
    }

    public static void set(PrimedTnt tnt, int count) {
        COUNTS.put(tnt, count);
    }

    public static int get(PrimedTnt tnt) {
        Integer count = COUNTS.get(tnt);
        return count == null ? 1 : count;
    }

    public static void remove(PrimedTnt tnt) {
        COUNTS.remove(tnt);
    }
}
