package com.haidan.carpetdan;

import carpet.utils.SpawnReporter;
import net.minecraft.world.entity.MobCategory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Implements the spawn-rate based rules by writing into Carpet's own spawn rate table,
 * which is exactly what {@code /spawn rates <category> 0} does.
 *
 * <p>Each group is written only when its rule transitions from off to on (and again after
 * being switched off and on). A later manual {@code /spawn rates ...} is left alone, so the
 * command keeps working as usual while the rule is on. When a rule is switched off the values
 * captured before the first override are restored.</p>
 */
public final class SpawnRateControl {
    private static final Group AQUATIC = new Group("noAquaticSpawning", List.of(
            MobCategory.WATER_CREATURE,
            MobCategory.WATER_AMBIENT,
            MobCategory.UNDERGROUND_WATER_CREATURE,
            MobCategory.AXOLOTLS));
    private static final Group AMBIENT = new Group("noBatSpawning", List.of(MobCategory.AMBIENT));

    private SpawnRateControl() {
    }

    public static void tick() {
        AQUATIC.sync(CarpetDanSettings.noAquaticSpawningEnabled());
        AMBIENT.sync(CarpetDanSettings.noBatSpawningEnabled());
    }

    private static final class Group {
        private final String ruleName;
        private final List<MobCategory> categories;
        private final Map<MobCategory, Integer> originals = new EnumMap<>(MobCategory.class);
        private boolean active;

        private Group(String ruleName, List<MobCategory> categories) {
            this.ruleName = ruleName;
            this.categories = categories;
        }

        private void sync(boolean enabled) {
            if (enabled && !active) {
                originals.clear();
                for (MobCategory category : categories) {
                    originals.put(category, SpawnReporter.spawn_tries.get(category));
                    SpawnReporter.spawn_tries.put(category, 0);
                }
                active = true;
                CarpetDanAddition.LOGGER.info("{}: spawn rate set to 0 for {}", ruleName, categories);
            } else if (!enabled && active) {
                for (Map.Entry<MobCategory, Integer> entry : originals.entrySet()) {
                    if (entry.getValue() == null) {
                        SpawnReporter.spawn_tries.remove(entry.getKey());
                    } else {
                        SpawnReporter.spawn_tries.put(entry.getKey(), entry.getValue());
                    }
                }
                originals.clear();
                active = false;
                CarpetDanAddition.LOGGER.info("{}: original spawn rates restored", ruleName);
            }
        }
    }
}
