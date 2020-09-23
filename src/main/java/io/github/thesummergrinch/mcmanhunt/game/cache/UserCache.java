package io.github.thesummergrinch.mcmanhunt.game.cache;

import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class UserCache {

    private static volatile UserCache instance;
    private final HashMap<UUID, PlayerState> playerCache;

    private UserCache() {
        this.playerCache = new HashMap<>();
    }

    public static synchronized UserCache getInstance() {
        UserCache userCache = instance;
        if (userCache != null) return userCache;
        synchronized (UserCache.class) {
            if (instance == null) {
                instance = new UserCache();
            }
            return instance;
        }
    }

    public void addPlayer(final PlayerState playerState) {
        this.playerCache.put(playerState.getUUID(), playerState);
    }

    public PlayerState getPlayerState(final UUID uuid) {
        return this.playerCache.get(uuid);
    }

    public ArrayList<PlayerState> getHunters() {
        return (ArrayList<PlayerState>) this.playerCache.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.HUNTER)).collect(Collectors.toList());
    }

    public ArrayList<PlayerState> getRunners() {
        return (ArrayList<PlayerState>) this.playerCache.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.RUNNER)).collect(Collectors.toList());
    }

    public UUID getUniqueIDByName(final String playerName) {
        Optional<Player> found = Optional.empty();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(playerName)) {
                found = Optional.of(player);
                break;
            }
        }
        Optional<Player> optionalPlayer = found;
        return optionalPlayer.map(Entity::getUniqueId).orElse(null);
    }

    public long getNumberOfRunners() {
        return this.playerCache.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.RUNNER)).count();
    }

    public long getNumberOfHunters() {
        return this.playerCache.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.HUNTER)).count();
    }

    public boolean userHasEntry(final UUID playerUUID) {
        return this.playerCache.containsKey(playerUUID);
    }

    public Collection<PlayerState> getAllPlayers() {
        return playerCache.values();
    }

    public List<PlayerState> getSpectators() {
        return this.playerCache.values().stream().filter(playerState -> playerState.getPlayerRole()
                .equals(PlayerRole.SPECTATOR)).collect(Collectors.toList());
    }

    public List<PlayerState> getPlayersToRandomlyAllocate() {
        return this.playerCache.values().stream().filter(playerState -> playerState.getPlayerRole()
                .equals(PlayerRole.RANDOM)).collect(Collectors.toList());
    }

}
