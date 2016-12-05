package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
import aiad.feup.messages.EndGame;
import aiad.feup.messages.SetupPlayer;
import aiad.feup.messages.UpdatePlayer;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Serializable;

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

        //Extract content
        Object content = null;
        try {
            content = message.getContentObject();
        } catch (UnreadableException e) {
            System.out.println("Could not retrieve message content object even if it is null.");
            e.printStackTrace();
        }

        // Content-less messages
        if(content == null) {
            // Confirmation
            if(message.getPerformative() == ACLMessage.CONFIRM && player.getGameState() == GameState.IDLE) {
                System.out.println("Confirmation Received. Successfully joined the game.");
                timeout = 0L;
                return;
            }

            //Refused Example
            if(message.getPerformative() == ACLMessage.REFUSE){
                //DO SOMETHING
                return;
            }
        }

        // Content rich messages
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


        //Unknown message
        System.out.println("Received unknown message. Continuing.");
        return;
    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() == GameState.END_GAME;
    }
}
