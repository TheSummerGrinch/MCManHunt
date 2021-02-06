package io.github.thesummergrinch.mcmanhunt.game.players.compasses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CompassMetaBuilder {

    private static volatile CompassMetaBuilder instance;

    private CompassMeta compassMeta;

    private CompassMetaBuilder() {
        this.compassMeta = (CompassMeta) new ItemStack(Material.COMPASS).getItemMeta();
    }

    @NotNull
    public static CompassMetaBuilder getInstance() {
        CompassMetaBuilder compassMetaBuilder = instance;
        if (compassMetaBuilder != null) return compassMetaBuilder;
        synchronized (CompassMetaBuilder.class) {
            if (instance == null) instance = new CompassMetaBuilder();
            return instance;
        }
    }

    @NotNull
    public CompassMetaBuilder setName(@NotNull final String name) {
        this.compassMeta.setDisplayName(name);
        return this;
    }

    @NotNull
    public CompassMetaBuilder setLodestone(@NotNull final Location location) {
        this.compassMeta.setLodestone(location);
        return this;
    }

    @NotNull
    public CompassMetaBuilder setLodestoneTracked(final boolean lodestoneTracked) {
        this.compassMeta.setLodestoneTracked(lodestoneTracked);
        return this;
    }

    @NotNull
    public CompassMetaBuilder addEnchant(@NotNull final Enchantment enchantment, final int level,
                                         final boolean ignoreLevelRestriction) {
        this.compassMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    @NotNull
    public CompassMeta create() {
        CompassMeta meta = this.compassMeta;
        reset();
        return meta;
    }

    @NotNull
    public CompassMetaBuilder addLore(final List<String> lore) {
        this.compassMeta.setLore(lore);
        return this;
    }

    public void reset() {
        this.compassMeta = (CompassMeta) new ItemStack(Material.COMPASS).getItemMeta();
    }

}
