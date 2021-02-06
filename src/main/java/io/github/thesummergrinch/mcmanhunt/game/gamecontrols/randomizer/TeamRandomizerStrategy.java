package io.github.thesummergrinch.mcmanhunt.game.gamecontrols.randomizer;

import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;

import java.util.List;

public interface TeamRandomizerStrategy {

    void randomizeTeams(final List<PlayerState> playerStateList);

}
