package com.haidan.carpetdan;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class CarpetDanAdditionExtension implements CarpetExtension {
    private static final Gson GSON = new Gson();
    private static boolean settingsRegistered;

    /**
     * Register our rules into Carpet's own {@link carpet.settings.SettingsManager}, so they show up
     * in {@code /carpet} with their own category like every other Carpet Additions mod does.
     */
    @Override
    public void onGameStarted() {
        if (!settingsRegistered) {
            settingsRegistered = true;
            CarpetServer.settingsManager.parseSettingsClass(CarpetDanSettings.class);
            CarpetDanAddition.LOGGER.info("registered {} carpet rule(s)", CarpetServer.settingsManager.getCarpetRules().size());
        }
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        String path = "assets/" + CarpetDanAddition.MOD_ID + "/lang/" + lang + ".json";
        try (InputStream in = CarpetDanAdditionExtension.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                return Collections.emptyMap();
            }
            Map<String, String> map = GSON.fromJson(
                    new InputStreamReader(in, StandardCharsets.UTF_8),
                    new TypeToken<Map<String, String>>() {
                    }.getType()
            );
            return map == null ? Collections.emptyMap() : map;
        } catch (Exception e) {
            CarpetDanAddition.LOGGER.warn("Failed to load translations for {}", lang, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public String version() {
        return CarpetDanAddition.VERSION;
    }
}
