package com.haidan.carpetdan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores {@code /botmemo} memos in {@code config/carpet-dan-addition/botmemo.json} so they survive
 * a server restart. The action pack is saved as plain data (see {@link BotMemo.ActionData}).
 */
public final class BotMemoStore {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("carpet-dan-addition")
            .resolve("botmemo.json");
    private static final Type FILE_TYPE = new TypeToken<Map<String, SavedMemo>>() {
    }.getType();

    private BotMemoStore() {
    }

    public static Map<String, BotMemo> load() {
        if (!Files.isRegularFile(FILE)) {
            return new HashMap<>();
        }
        try (Reader reader = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            Map<String, SavedMemo> saved = GSON.fromJson(reader, FILE_TYPE);
            Map<String, BotMemo> result = new HashMap<>();
            if (saved != null) {
                for (Map.Entry<String, SavedMemo> entry : saved.entrySet()) {
                    BotMemo memo = toMemo(entry.getValue());
                    if (memo != null) {
                        result.put(entry.getKey(), memo);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            CarpetDanAddition.LOGGER.warn("botmemo: failed to read {}", FILE, e);
            return new HashMap<>();
        }
    }

    public static void save(Map<String, BotMemo> memos) {
        try {
            Files.createDirectories(FILE.getParent());
            Map<String, SavedMemo> saved = new HashMap<>();
            for (Map.Entry<String, BotMemo> entry : memos.entrySet()) {
                saved.put(entry.getKey(), toSaved(entry.getValue()));
            }
            try (Writer writer = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
                GSON.toJson(saved, FILE_TYPE, writer);
            }
        } catch (Exception e) {
            CarpetDanAddition.LOGGER.warn("botmemo: failed to write {}", FILE, e);
        }
    }

    private static SavedMemo toSaved(BotMemo memo) {
        SavedMemo saved = new SavedMemo();
        saved.dimension = memo.dimension().identifier().toString();
        saved.x = memo.x();
        saved.y = memo.y();
        saved.z = memo.z();
        saved.yaw = memo.yaw();
        saved.pitch = memo.pitch();
        saved.gameType = memo.gameType().getName();
        saved.sneaking = memo.actions().sneaking();
        saved.sprinting = memo.actions().sprinting();
        saved.forward = memo.actions().forward();
        saved.strafing = memo.actions().strafing();
        saved.actions = new ArrayList<>();
        for (BotMemo.ActionEntry entry : memo.actions().actions()) {
            SavedAction action = new SavedAction();
            action.type = entry.type();
            action.limit = entry.limit();
            action.interval = entry.interval();
            action.offset = entry.offset();
            action.isContinuous = entry.isContinuous();
            saved.actions.add(action);
        }
        return saved;
    }

    private static BotMemo toMemo(SavedMemo saved) {
        if (saved == null || saved.dimension == null) {
            return null;
        }
        try {
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse(saved.dimension));
            GameType gameType;
            try {
                gameType = GameType.byName(saved.gameType, GameType.SURVIVAL);
            } catch (Exception e) {
                gameType = GameType.SURVIVAL;
            }
            List<BotMemo.ActionEntry> actions = new ArrayList<>();
            if (saved.actions != null) {
                for (SavedAction action : saved.actions) {
                    // Older memo files did not store the continuous flag: infer it from the
                    // Carpet factory Defaults (continuous() == limit -1, interval 1, offset 0).
                    Boolean continuous = action.isContinuous;
                    boolean isContinuous = continuous != null
                            ? continuous
                            : action.limit == -1 && action.interval == 1 && action.offset == 0;
                    actions.add(new BotMemo.ActionEntry(action.type, action.limit, action.interval, action.offset, isContinuous));
                }
            }
            BotMemo.ActionData data = new BotMemo.ActionData(
                    saved.sneaking, saved.sprinting, saved.forward, saved.strafing, List.copyOf(actions));
            return new BotMemo(dimension, saved.x, saved.y, saved.z, saved.yaw, saved.pitch, gameType, data);
        } catch (Exception e) {
            CarpetDanAddition.LOGGER.warn("botmemo: skipping invalid memo for dimension {}", saved.dimension, e);
            return null;
        }
    }

    private static final class SavedMemo {
        String dimension;
        double x;
        double y;
        double z;
        float yaw;
        float pitch;
        String gameType;
        boolean sneaking;
        boolean sprinting;
        float forward;
        float strafing;
        List<SavedAction> actions;
    }

    private static final class SavedAction {
        String type;
        int limit;
        int interval;
        int offset;
        Boolean isContinuous;
    }
}
