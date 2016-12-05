package aiad.feup.agents;

import aiad.feup.models.GameState;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

import java.io.IOException;

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
     * Setup the game agent
     */
    @Override
    protected void setup() {
        System.out.println("Agent " + getName() + " started!");
    }

    /**
     * On agent take down
     */
    @Override
    protected void takeDown() {
        System.out.println("Agent " + getName() + " terminated!");
    }

    /**
     * Initialize the board
     * @param host hostname of the DFS
     * @param port port of the DFS
     */
    public abstract void init(final String host, final int port) throws StaleProxyException;

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
     * @param targetAgent target recipient for the message
     * @param message message to be sent
     */
    public void sendMessage(final RemoteAgent targetAgent, ACLMessage message) {
        try {
            message.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.addReceiver(new AID(targetAgent.getName(), AID.ISLOCALNAME));
        send(message);
    }
}
