package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.behaviours.player.manager.ReceiveOffer;
import aiad.feup.messages.EndGame;
import aiad.feup.messages.SetupPlayer;
import aiad.feup.messages.UpdatePlayer;
import aiad.feup.models.GameState;
import aiad.feup.models.PlayerType;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * Waiting for join confirmation behaviour
 */
public class WaitStartGame extends SimpleBehaviour {

    private static WaitStartGame instance;

    private WaitStartGame(final Player player) { super(player); }

    public static WaitStartGame getInstance(final Player player) {
        if(instance == null)
            instance = new WaitStartGame(player);
        return instance;
    }

    @Override
    public void action() {
        final ACLMessage message = getAgent().blockingReceive();
        final Player player = (Player)getAgent();

        //Extract content
        Object content;
        try {
            content = message.getContentObject();
        } catch (UnreadableException e) {
            System.out.println("Could not retrieve message content object. " + e.getMessage());
            return;
        }

        // Content-less messages
        if(content == null)
            return;

        // Ending the game
        if(content instanceof EndGame) {
            EndGame endGame = (EndGame) content;
            System.out.println("Ending the game. Reason: " + endGame.getMessage());
            System.exit(0);
            return;
        }

        // Setup the player
        if(content instanceof SetupPlayer) {
            SetupPlayer setupPlayer = (SetupPlayer) content;
            player.setType(setupPlayer.getPlayerType());
            System.out.println("I have been assigned as " + player.getType());
            return;
        }

        // Updating the player
        if(content instanceof UpdatePlayer) {
            UpdatePlayer updatePlayer = (UpdatePlayer) content;
            player.setCompanies(updatePlayer.getCompanyList());
            player.setTokens(updatePlayer.getTokens());
            player.setBalance(updatePlayer.getBalance());
            player.setGameState(updatePlayer.getState());

            if(player.getType() == PlayerType.INVESTOR)
                player.addBehaviour(player.getFactory().wrap(MakeOffer.getInstance(player)));
            else
                player.addBehaviour(player.getFactory().wrap(ReceiveOffer.getInstance(player)));
        }
    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() != GameState.WAITING_GAME_START;
    }
}
