package com.haidan.carpetdan;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;

/**
 * {@code /mergeTNT <pos> [n] [m]} spawns a primed TNT with a remembered explosion count.
 *
 * <p>The position accepts the vanilla coordinate syntax, including absolute values, {@code ~ ~ ~}
 * relative coordinates and {@code ^ ^ ^} local coordinates, with the vanilla tab completion.</p>
 *
 * <p>This is the standalone counterpart of Carpet's / REMS' mergeTNT rules: it never reads or writes
 * their counters. The explosion count is kept in {@link MergedTntTracker} and
 * {@code PrimedTntMixin} repeats the 4.0F explosion {@code n - 1} times when the TNT goes off.</p>
 */
public final class MergedTntCommand {
    private static final int DEFAULT_FUSE = 80;

    private MergedTntCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Always registered, so the command is visible in the client's command tree even while the
        // rule is off. The rule is checked when the command runs, not when the tree is built.
        dispatcher.register(root("mergeTNT"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> root(String name) {
        return Commands.literal(name)
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(Commands.argument("pos", Vec3Argument.vec3(false))
                        .executes(ctx -> spawn(ctx, 1, DEFAULT_FUSE))
                        .then(Commands.argument("n", IntegerArgumentType.integer(1))
                                .executes(ctx -> spawn(
                                        ctx,
                                        IntegerArgumentType.getInteger(ctx, "n"),
                                        DEFAULT_FUSE))
                                .then(Commands.argument("m", IntegerArgumentType.integer(0, 32767))
                                        .executes(ctx -> spawn(
                                                ctx,
                                                IntegerArgumentType.getInteger(ctx, "n"),
                                                IntegerArgumentType.getInteger(ctx, "m"))))));
    }

    private static int spawn(CommandContext<CommandSourceStack> ctx, int count, int fuse) {
        CommandSourceStack source = ctx.getSource();
        if (!CarpetDanSettings.mergeTntDebugEnabled()) {
            source.sendFailure(Component.literal("mergeTNTdebug is disabled. Run /carpet mergeTNTdebug true first."));
            return 0;
        }
        ServerLevel level = source.getLevel();
        // Vec3Argument resolves absolute, ~-relative and ^-local coordinates against the source.
        Vec3 pos = Vec3Argument.getVec3(ctx, "pos");
        PrimedTnt tnt = new PrimedTnt(level, pos.x, pos.y, pos.z, null);
        // PrimedTnt's constructor gives the entity a small random launch motion; a debug TNT must
        // start exactly at the requested coordinates, so drop that initial velocity.
        tnt.setDeltaMovement(Vec3.ZERO);
        tnt.setFuse(fuse);
        level.addFreshEntity(tnt);
        MergedTntTracker.set(tnt, count);
        source.sendSuccess(() -> Component.literal(
                "Summoned TNT with " + count + " TNT-equivalent(s), fuse " + fuse
                        + " at " + pos.x + " " + pos.y + " " + pos.z), true);
        return 1;
    }
}
