package com.haidan.carpetdan.ticket;

import com.haidan.carpetdan.CarpetDanAddition;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.TicketType;

/**
 * A chunk ticket modelled on {@link TicketType#ENDER_PEARL}, but with an 80 gt timeout instead of 40 gt.
 *
 * <p>Flags are copied from {@code ENDER_PEARL} so the loading/simulation level and the
 * keep-dimension-active behaviour are identical to a thrown ender pearl. The ticket is placed with
 * the same radius (2) that {@code ServerPlayer.placeEnderPearlTicket} uses.</p>
 */
public final class TntChunkLoadingTicket {

    /** 80 gt loader ticket, same flag bits as {@link TicketType#ENDER_PEARL}. */
    public static final TicketType TNT_CHUNK_LOADING = Registry.register(
            BuiltInRegistries.TICKET_TYPE,
            Identifier.fromNamespaceAndPath(CarpetDanAddition.MOD_ID, "tnt_chunk_loading"),
            new TicketType(80L, TicketType.ENDER_PEARL.flags()));

    private TntChunkLoadingTicket() {
    }
}
