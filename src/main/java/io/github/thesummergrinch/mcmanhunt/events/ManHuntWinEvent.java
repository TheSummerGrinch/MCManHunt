package io.github.thesummergrinch.mcmanhunt.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

public class ManHuntWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @NotNull
    final HashSet<UUID> winnerUUIDs;
    @NotNull
    final String gameName;

    public ManHuntWinEvent(@NotNull final String gameName, @NotNull final HashSet<UUID> winnerUUIDs) {
        this.winnerUUIDs = winnerUUIDs;
        this.gameName = gameName;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HashSet<UUID> getWinners() {
        return this.winnerUUIDs;
    }

    @NotNull
    public String getGameName() {
        return this.gameName;
    }

}
