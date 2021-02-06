package io.github.thesummergrinch.mcmanhunt.game.gamecontrols.randomizer;

import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SimpleTeamRandomizer implements TeamRandomizerStrategy {

    @Override
    public void randomizeTeams(@NotNull final List<PlayerState> playerStateList) {
        Collections.shuffle(playerStateList);
        for (int index = 0; index < playerStateList.size(); index ++) {
            playerStateList.get(index).setPlayerRole(
                (index % 2 == 0)
                ? PlayerRole.RUNNER
                : PlayerRole.HUNTER
            );
        }
    }

}
