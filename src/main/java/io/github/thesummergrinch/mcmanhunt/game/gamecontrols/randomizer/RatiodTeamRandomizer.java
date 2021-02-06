package io.github.thesummergrinch.mcmanhunt.game.gamecontrols.randomizer;

import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RatiodTeamRandomizer implements TeamRandomizerStrategy {

    private final int huntersPerRunner;

    /**
     * Initializes the RatiodTeamRandomizer with a default ratio of 2 hunters
     * per runner.
     */
    public RatiodTeamRandomizer() {
        this.huntersPerRunner = 2;
    }

    public RatiodTeamRandomizer(final int huntersPerRunner) {
        this.huntersPerRunner = huntersPerRunner;
    }

    @Override
    public void randomizeTeams(@NotNull final List<PlayerState> playerStateList) {
        Collections.shuffle(playerStateList);
        for(int index = 0; index < playerStateList.size(); index++) {
            playerStateList.get(index).setPlayerRole(
                    (index % (this.huntersPerRunner + 1) == 0)
                            ? PlayerRole.RUNNER
                            : PlayerRole.HUNTER);
        }
    }
}
