package aiad.feup.behaviours.player.investor;

import aiad.feup.agents.Player;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;

/**
 * Behaviour to make offers to managers
 */
public class MakeOffer extends SimpleBehaviour {

    private static MakeOffer instance;

    private MakeOffer(final Player player) { super(player); }

    public static MakeOffer getInstance(final Player player) {
        if(instance == null)
            instance = new MakeOffer(player);
        return instance;
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() != GameState.START_NEGOTIATION;
    }
}
