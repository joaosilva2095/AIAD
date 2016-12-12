package aiad.feup.behaviours.player;

import aiad.feup.agents.Player;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.behaviours.player.investor.ReceiveMessageInvestor;
import aiad.feup.behaviours.player.manager.ReceiveMessageManager;
import aiad.feup.messageObjects.SetupPlayer;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.GameState;
import aiad.feup.models.PlayerType;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Waiting for join confirmation behaviour
 */
public class WaitStartGame extends SimpleBehaviour {

    private static WaitStartGame instance;

    private WaitStartGame(Player player) { super(player); }

    public static WaitStartGame getInstance(Player player) {
        if(instance == null)
            instance = new WaitStartGame(player);
        return instance;
    }

    @Override
    public void action() {
        ACLMessage message = getAgent().blockingReceive();
        Player player = (Player)getAgent();

        //Extract content
        Object content = player.extractMessageContentObject(message);

        // Content-less messageObjects
        if(content == null)
            return;

        // Check if end game
        player.handleEndGame(content);

        // Setup the player
        if(content instanceof SetupPlayer) {
            SetupPlayer setupPlayer = (SetupPlayer) content;
            player.setType(setupPlayer.getPlayerType());
            player.setRoundDuration(setupPlayer.getRoundDuration());
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
            player.incrementRoundNumber();
            player.generateCompanyBeliefs();

            if(player.getType() == PlayerType.INVESTOR) {
                player.addBehaviour(player.getFactory().wrap(ReceiveMessageInvestor.getInstance(player)));
                MakeOffer makeOfferInstance = MakeOffer.getInstance(player, true);
                player.addBehaviour(player.getFactory().wrap(makeOfferInstance));
                makeOfferInstance.setRoundBalance(updatePlayer.getBalance());
            }
            else {
                player.addBehaviour(player.getFactory().wrap(ReceiveMessageManager.getInstance(player)));
            }

            player.setRoundStartTime(System.currentTimeMillis());
        }
    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() != GameState.WAITING_GAME_START;
    }
}
