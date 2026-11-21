package com.haidan.carpetdan;

import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarpetDanAddition implements ModInitializer {
    public static final String MOD_ID = "carpet-dan-addition";
    public static final String MOD_NAME = "Carpet Dan Addition";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(new CarpetDanAdditionExtension());
        LOGGER.info("[{}] {} loaded, extensions={}", MOD_ID, VERSION, CarpetServer.extensions.size());
    }
}
