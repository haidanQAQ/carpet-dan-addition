package com.haidan.carpetdan;

import carpet.fakes.ServerPlayerInterface;
import carpet.helpers.EntityPlayerActionPack;
import carpet.patches.EntityPlayerMPFake;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * {@code /botmemo <name> save|run}: remembers a fake player's position, rotation and running
 * actions, and puts it back exactly there later. When the bot is not online, {@code run} spawns
 * it at the remembered spot first.
 */
public final class BotMemoCommand {
    private static final Map<String, BotMemo> MEMOS = new HashMap<>();
    private static final Map<String, PendingRestore> PENDING_RESTORES = new HashMap<>();

    /**
     * A restore that has to wait: Carpet's fake-player creation writes the action pack of a fresh
     * player *after* {@code PlayerList.placeNewPlayer} returns (it copies an empty pack over ours),
     * so joining-tick restore is lost. We wait a couple of server ticks instead.
     */
    private record PendingRestore(BotMemo memo, int ticksLeft) {
    }
    private static final SuggestionProvider<CommandSourceStack> NAME_SUGGESTIONS = (context, builder) -> {
        for (String name : MEMOS.keySet()) {
            builder.suggest(SuggestionText.quoteIfNeeded(name));
        }
        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            if (player instanceof EntityPlayerMPFake) {
                builder.suggest(SuggestionText.quoteIfNeeded(player.getGameProfile().name()));
            }
        }
        return CompletableFuture.completedFuture(builder.build());
    };

    private BotMemoCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("botmemo")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)
                        && CarpetDanSettings.botMemoEnabled())
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(NAME_SUGGESTIONS)
                        .then(Commands.literal("save")
                                .executes(context -> save(context, StringArgumentType.getString(context, "name"))))
                        .then(Commands.literal("run")
                                .executes(context -> run(context, StringArgumentType.getString(context, "name"))))));
    }

    private static int save(CommandContext<CommandSourceStack> context, String name) {
        ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayerByName(name);
        if (player == null) {
            context.getSource().sendFailure(Component.literal("No player named " + name + " is online; save needs the bot online"));
            return 0;
        }
        if (!(player instanceof EntityPlayerMPFake)) {
            context.getSource().sendFailure(Component.literal(name + " is not a Carpet fake player"));
            return 0;
        }

        EntityPlayerActionPack snapshot = new EntityPlayerActionPack(player);
        snapshot.copyFrom(((ServerPlayerInterface) player).getActionPack());

        BotMemo memo = new BotMemo(
                player.level().dimension(),
                player.getX(), player.getY(), player.getZ(),
                player.getYRot(), player.getXRot(),
                player.gameMode.getGameModeForPlayer(),
                snapshot
        );
        MEMOS.put(name, memo);

        context.getSource().sendSuccess(() -> Component.literal(String.format(
                "Saved %s at %.2f, %.2f, %.2f in %s (yaw %.1f, pitch %.1f)",
                name, memo.x(), memo.y(), memo.z(), memo.dimension().identifier(), memo.yaw(), memo.pitch())), false);
        return 1;
    }

    private static int run(CommandContext<CommandSourceStack> context, String name) {
        var server = context.getSource().getServer();
        BotMemo memo = MEMOS.get(name);
        if (memo == null) {
            context.getSource().sendFailure(Component.literal("No memo for " + name + "; run /botmemo " + name + " save first"));
            return 0;
        }
        ServerLevel level = server.getLevel(memo.dimension());
        if (level == null) {
            context.getSource().sendFailure(Component.literal("Saved dimension is not loaded: " + memo.dimension().identifier()));
            return 0;
        }

        ServerPlayer online = server.getPlayerList().getPlayerByName(name);
        if (online instanceof EntityPlayerMPFake fake) {
            apply(fake, memo, false);
            context.getSource().sendSuccess(() -> Component.literal(String.format(
                    "Restored %s to %.2f, %.2f, %.2f in %s",
                    name, memo.x(), memo.y(), memo.z(), memo.dimension().identifier())), false);
            return 1;
        }
        if (online != null) {
            context.getSource().sendFailure(Component.literal(name + " is online but is not a Carpet fake player"));
            return 0;
        }

        // Store the memo first: fake player creation is asynchronous and a fast (cached) profile
        // lookup can put the player in the world before createFake() returns. The restore itself
        // runs on a later tick, after Carpet finished overwriting the action pack.
        PENDING_RESTORES.put(name, new PendingRestore(memo, 2));
        boolean spawned = EntityPlayerMPFake.createFake(
                name, server,
                new Vec3(memo.x(), memo.y(), memo.z()),
                memo.yaw(), memo.pitch(),
                memo.dimension(), memo.gameType(),
                memo.gameType().isCreative());
        if (!spawned) {
            PENDING_RESTORES.remove(name);
            context.getSource().sendFailure(Component.literal("Could not spawn fake player " + name));
            return 0;
        }
        context.getSource().sendSuccess(() -> Component.literal(String.format(
                "Spawning %s at %.2f, %.2f, %.2f in %s and restoring its actions",
                name, memo.x(), memo.y(), memo.z(), memo.dimension().identifier())), false);
        return 1;
    }

    /** Called every server tick: finishes deferred restores for freshly spawned fake players. */
    public static void tickPendingRestores(MinecraftServer server) {
        if (PENDING_RESTORES.isEmpty()) {
            return;
        }
        PENDING_RESTORES.entrySet().removeIf(entry -> {
            String name = entry.getKey();
            PendingRestore pending = entry.getValue();
            if (!(server.getPlayerList().getPlayerByName(name) instanceof EntityPlayerMPFake fake)) {
                return false; // still spawning; keep waiting
            }
            if (pending.ticksLeft() > 0) {
                entry.setValue(new PendingRestore(pending.memo(), pending.ticksLeft() - 1));
                return false;
            }
            apply(fake, pending.memo(), true);
            return true;
        });
    }

    private static void apply(EntityPlayerMPFake player, BotMemo memo, boolean teleport) {
        if (teleport) {
            ServerLevel level = player.level().getServer().getLevel(memo.dimension());
            if (level != null) {
                player.teleportTo(level, memo.x(), memo.y(), memo.z(), Set.<Relative>of(), memo.yaw(), memo.pitch(), false);
            }
            player.setYRot(memo.yaw());
            player.setXRot(memo.pitch());
            player.setYHeadRot(memo.yaw());
            player.setDeltaMovement(Vec3.ZERO);
        }
        EntityPlayerActionPack actions = ((ServerPlayerInterface) player).getActionPack();
        actions.stopAll();
        actions.copyFrom(memo.snapshot());
    }
}
