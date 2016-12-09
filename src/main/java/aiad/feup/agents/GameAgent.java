package aiad.feup.agents;

import aiad.feup.models.GameState;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

/**
 * Game agent
 */
public abstract class GameAgent extends Agent{

    private static final ThreadedBehaviourFactory factory = new ThreadedBehaviourFactory();

    /**
     * Sate of the game
     */
    private GameState state;

    /**
     * Constructor of a game agent
     */
    public GameAgent() {
        this.state = GameState.NONE;
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
     * Get a the threaded factory
     * @return get the factory
     */
    public ThreadedBehaviourFactory getFactory() {
        return factory;
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
     * Extracts the content of a message. Returns null if inexistant
     * @param message the message with the content
     * @return the content of the message
     */
    public Object extractMessageContentObject(ACLMessage message) {
        Object content;
        try {
            content = message.getContentObject();
        } catch (Exception e) {
            System.out.println("Could not retrieve message content object. " + e.getMessage());
            return null;
        }
        return content;
    }

    /**
     * Send a message
     * @param targetAgent target recipient for the message
     * @param message message to be sent
     */
    public void sendMessage(final RemoteAgent targetAgent, ACLMessage message, Serializable content) {
        try {
            message.setContentObject(content);
        } catch (IOException e) {
            System.out.println("Could not set the message object! " + e.getMessage());
            return;
        }
        message.addReceiver(new AID(targetAgent.getName(), AID.ISGUID));
        send(message);
    }
}
