package io.github.thesummergrinch.mcmanhunt.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

public class ManHuntWinEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @NotNull
    final HashSet<UUID> winnerUUIDs;
    @NotNull
    final String gameName;
    private boolean cancel;

    public ManHuntWinEvent(@NotNull final String gameName, @NotNull final HashSet<UUID> winnerUUIDs) {
        this.cancel = false;
        this.winnerUUIDs = winnerUUIDs;
        this.gameName = gameName;

    }

    public static @NotNull HandlerList getHandlerList() {

        return handlers;

    }

    @Override
    public @NotNull HandlerList getHandlers() {

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

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
