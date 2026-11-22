package com.haidan.carpetdan;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

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
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        try {
            dispatcher.register(Commands.literal("bot")
                    .requires(src -> src.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                    .then(Commands.argument("name", EntityArgument.player())
                            .then(Commands.literal("dropall")
                                    .executes(CarpetDanAdditionExtension::dropAll))));
        } catch (Exception e) {
            CarpetDanAddition.LOGGER.warn("Failed to register /bot <name> dropall", e);
        }
    }

    private static int dropAll(CommandContext<CommandSourceStack> ctx) {
        if (!CarpetDanSettings.botDropAllEnabled()) {
            ctx.getSource().sendFailure(Component.literal("botdropall is disabled"));
            return 0;
        }
        ServerPlayer bot;
        try {
            bot = EntityArgument.getPlayer(ctx, "name");
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendFailure(Component.literal("No online player/bot with that name"));
            return 0;
        }
        int stacks = 0;
        for (int i = 0; i < bot.getInventory().getContainerSize(); i++) {
            ItemStack stack = bot.getInventory().getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack copy = stack.copy();
            bot.getInventory().setItem(i, ItemStack.EMPTY);
            ServerLevel level = (ServerLevel) bot.level();
            double dx = (bot.getRandom().nextDouble() - 0.5D) * 1.4D;
            double dz = (bot.getRandom().nextDouble() - 0.5D) * 1.4D;
            ItemEntity item = new ItemEntity(level, bot.getX(), bot.getY() + 0.5D, bot.getZ(), copy);
            item.setDeltaMovement(dx, 0.25D, dz);
            item.setPickUpDelay(40);
            item.setThrower(bot);
            level.addFreshEntity(item);
            stacks++;
        }
        final int dropped = stacks;
        ctx.getSource().sendSuccess(
                () -> Component.literal("Dropped " + dropped + " stack(s) from " + bot.getName().getString()),
                false);
        return dropped;
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
