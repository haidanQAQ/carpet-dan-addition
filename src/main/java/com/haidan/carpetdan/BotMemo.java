package com.haidan.carpetdan;

import carpet.helpers.EntityPlayerActionPack;
import carpet.helpers.EntityPlayerActionPack.Action;
import carpet.helpers.EntityPlayerActionPack.ActionType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A remembered bot state: where it was, which way it looked, and what it was doing.
 *
 * <p>The actions are stored as plain data (type + interval parameters) instead of a live
 * {@link EntityPlayerActionPack}, so a memo can be written to disk and survives a server restart.</p>
 */
public record BotMemo(ResourceKey<Level> dimension,
                      double x, double y, double z,
                      float yaw, float pitch,
                      GameType gameType,
                      ActionData actions) {

    public record ActionEntry(String type, int limit, int interval, int offset, boolean isContinuous) {
    }

    public record ActionData(boolean sneaking, boolean sprinting, float forward, float strafing,
                             List<ActionEntry> actions) {

        public static ActionData capture(EntityPlayerActionPack pack) {
            List<ActionEntry> entries = new ArrayList<>();
            try {
                Field actionsField = EntityPlayerActionPack.class.getDeclaredField("actions");
                actionsField.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<ActionType, Action> map = (Map<ActionType, Action>) actionsField.get(pack);
                if (map != null) {
                    for (Map.Entry<ActionType, Action> entry : map.entrySet()) {
                        Action action = entry.getValue();
                        entries.add(new ActionEntry(entry.getKey().name(), action.limit, action.interval, action.offset, readActionFlag(action, "isContinuous")));
                    }
                }
            } catch (ReflectiveOperationException e) {
                CarpetDanAddition.LOGGER.warn("botmemo: could not read Carpet action pack, actions are not saved", e);
            }
            return new ActionData(
                    readBoolean(pack, "sneaking"),
                    readBoolean(pack, "sprinting"),
                    readFloat(pack, "forward"),
                    readFloat(pack, "strafing"),
                    List.copyOf(entries));
        }

        public void apply(EntityPlayerActionPack pack) {
            pack.stopAll();
            pack.setSneaking(sneaking);
            pack.setSprinting(sprinting);
            pack.setForward(forward);
            pack.setStrafing(strafing);
            for (ActionEntry entry : actions) {
                ActionType type;
                try {
                    type = ActionType.valueOf(entry.type());
                } catch (IllegalArgumentException e) {
                    continue;
                }
                pack.start(type, recreate(entry));
            }
        }

        private static Action recreate(ActionEntry entry) {
            if (entry.isContinuous()) {
                return Action.continuous();
            }
            if (entry.interval() > 0) {
                return Action.interval(entry.interval(), entry.offset());
            }
            if (entry.limit() == 1) {
                return Action.once();
            }
            return Action.continuous();
        }

        private static boolean readActionFlag(Action action, String name) {
            try {
                Field field = Action.class.getDeclaredField(name);
                field.setAccessible(true);
                return field.getBoolean(action);
            } catch (ReflectiveOperationException e) {
                return false;
            }
        }

        private static boolean readBoolean(EntityPlayerActionPack pack, String name) {
            try {
                return readField(pack, name).getBoolean(pack);
            } catch (ReflectiveOperationException e) {
                return false;
            }
        }

        private static float readFloat(EntityPlayerActionPack pack, String name) {
            try {
                return readField(pack, name).getFloat(pack);
            } catch (ReflectiveOperationException e) {
                return 0.0F;
            }
        }

        private static Field readField(EntityPlayerActionPack pack, String name) throws NoSuchFieldException {
            Field field = EntityPlayerActionPack.class.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        }
    }
}
