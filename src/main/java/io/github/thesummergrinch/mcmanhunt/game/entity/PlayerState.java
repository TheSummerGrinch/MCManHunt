package io.github.thesummergrinch.mcmanhunt.game.entity;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerState {

    private final UUID uuid;
    private final AtomicBoolean isMovementRestricted;

    private final CompassMeta compassMeta;
    private PlayerRole playerRole;

    public PlayerState(final UUID uuid) {
        this.uuid = uuid;
        playerRole = PlayerRole.DEFAULT;
        this.compassMeta = createCompassMeta();
        this.isMovementRestricted = new AtomicBoolean(false);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public PlayerRole getPlayerRole() {
        return this.playerRole;
    }

    public void setPlayerRole(final PlayerRole playerRole) {
        if (isOnline()) ((Player) getPlayerObject()).sendMessage("Your Role has been set to: " + playerRole.toString());
        this.playerRole = playerRole;
    }

    public boolean isOnline() {
        return Bukkit.getOnlinePlayers().stream().anyMatch(player -> player.getUniqueId().equals(uuid));
    }

    public OfflinePlayer getPlayerObject() {
        if (this.isOnline()) return Bukkit.getPlayer(this.uuid);
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    public CompassMeta getCompassMeta() {
        if (!this.playerRole.equals(PlayerRole.RUNNER)) return null;
        return this.compassMeta;
    }

    private CompassMeta createCompassMeta() {
        OfflinePlayer playerObject = this.getPlayerObject();
        CompassMeta compassMeta = (CompassMeta) new ItemStack(Material.COMPASS).getItemMeta();
        compassMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        compassMeta.setDisplayName(playerObject.getName() + " Tracker");
        if (playerObject instanceof Player) {
            Player player = (Player) playerObject;
            compassMeta.setLodestone(player.getLocation());
            compassMeta.setLodestoneTracked(!player.getWorld().getEnvironment().equals(World.Environment.NORMAL));
        } else {
            compassMeta.setLodestoneTracked(true);
        }
        return compassMeta;
    }

    public void updateCompassMeta() {
        if (this.isOnline()) {
            Player player = getPlayerObject().getPlayer();
            if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                this.compassMeta.setLodestone(player.getLocation());
                this.compassMeta.setLodestoneTracked(false);
            }
        }
    }

    public boolean isMovementRestricted() {
        return this.isMovementRestricted.get();
    }

    public void setIsMovementRestricted(final boolean isMovementRestricted) {
        this.isMovementRestricted.set(isMovementRestricted);
    }

}
