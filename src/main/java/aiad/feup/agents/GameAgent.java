package aiad.feup.agents;

import aiad.feup.messages.Message;
import aiad.feup.models.GameState;
import jade.core.Agent;

/**
 * Game agent
 */
public abstract class GameAgent extends Agent{

    /**
     * Sate of the game
     */
    private GameState state;

    /**
     * Constructor of a game agent
     */
    public GameAgent() {
        this.state = GameState.IDDLE;
    }

    /**
     * Get the state of the game
     * @return state of the game
     */
    public GameState getGameState() {
        return state;
    }

    /**
     * Set the state of the game
     * @param state new state of the game
     */
    public void setGameState(final GameState state) {
        this.state = state;
    }

    /**
     * Send a message
     * @param message message to be sent
     */
    public void sendMessage(final Message message, final RemoteAgent targetAgent) {

    }
}
