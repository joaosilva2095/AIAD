package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
import aiad.feup.messages.EndGame;
import aiad.feup.messages.SetupPlayer;
import aiad.feup.messages.UpdatePlayer;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * Receive Message Behaviour. Responsible for receiving all messages
 */
public class ReceiveMessage extends SimpleBehaviour {

    /**
     * Timeout for blocking receive
     */
    private Long timeout;

    /**
     * Constructor of receive message
     * @param player player to receive the message
     */
    public ReceiveMessage(final Player player) {
        super(player);
        timeout = 10000L;
    }

    @Override
    public void action() {
        ACLMessage message = getAgent().blockingReceive(timeout);
        Player player = (Player)getAgent();

        // Get the content
        Object content = null;
        try {
            content = message.getContentObject();
        } catch (UnreadableException e) {
        }
        if(content == null)
            return;

        // Confirmation
        if(message.getPerformative() == ACLMessage.CONFIRM && player.getGameState() == GameState.IDLE) {
            System.out.println("Confirmation Received. Successfully joined the game.");
            timeout = 0L;
            return;
        }

        // Setup the player
        if(content instanceof SetupPlayer) {
            final SetupPlayer setupPlayer = (SetupPlayer) content;
            player.setType(setupPlayer.getPlayerType());
            return;
        }

        // Update the player
        if(content instanceof UpdatePlayer) {
            final UpdatePlayer updatePlayer = (UpdatePlayer) content;
            player.setCompanies(updatePlayer.getCompanyList());
            player.setBalance(updatePlayer.getBalance());
            player.setTokens(updatePlayer.getTokens());
            return;
        }

        // End game
        if(content instanceof EndGame){
            final EndGame endGame = (EndGame) content;
            System.out.println("Ending the game. " + endGame.getMessage());
            player.setGameState(GameState.END_GAME);
            return;
        }
    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() == GameState.END_GAME;
    }
}
