package com.haidan.carpetdan.mixin;

import com.haidan.carpetdan.BotMemoCommand;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Deferred {@code /botmemo run} restore: Carpet's fake-player creation overwrites the action pack
 * after {@code placeNewPlayer}, so the restore has to happen on a later server tick.
 */
@Mixin(PlayerList.class)
public abstract class PlayerListTickMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void carpetdan$finishBotMemoRestores(CallbackInfo ci) {
        BotMemoCommand.tickPendingRestores(((PlayerList) (Object) this).getServer());
    }
}
