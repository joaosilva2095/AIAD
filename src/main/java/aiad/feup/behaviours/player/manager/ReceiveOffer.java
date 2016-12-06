package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Player;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;

/**
 * Receive offers from investors
 */
public class ReceiveOffer extends SimpleBehaviour {

    private static ReceiveOffer instance;

    private ReceiveOffer(final Player player) { super(player); }

    public static ReceiveOffer getInstance(final Player player) {
        if(instance == null)
            instance = new ReceiveOffer(player);
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
