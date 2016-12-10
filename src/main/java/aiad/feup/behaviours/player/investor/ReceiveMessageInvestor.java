package aiad.feup.behaviours.player.investor;
import aiad.feup.agents.Player;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messageObjects.Offer;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import com.oracle.deploy.update.Updater;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive Message Behaviour. Continuously listens for messageObjects from other agents
 */
public class ReceiveMessageInvestor extends SimpleBehaviour {

    private static ReceiveMessageInvestor instance;

    private ReceiveMessageInvestor(Player player) { super(player); }

    public static ReceiveMessageInvestor getInstance(Player player) {
        if(instance == null)
            instance = new ReceiveMessageInvestor(player);
        return instance;
    }

    @Override
    public void action() {
        ACLMessage message = getAgent().blockingReceive();
        Player player = (Player)getAgent();

        Object content = player.extractMessageContentObject(message);
        player.handleEndGame(content);

        if(content instanceof Offer) {
            Offer offer = (Offer) content;
            // Received standard ACL message
            if(message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                System.out.println("Hooray, offer accepted");
                CompanyInformation companyBelief = player.getCompanyBeliefs().get(offer.getCompany().getName());
                companyBelief.setCurrentOffer(offer);
            } else if (message.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                System.out.println("Damnit.");
            }
        }

        // Received UpdatePlayer (end of round)
        if(content instanceof UpdatePlayer) {
            UpdatePlayer updatePlayer = (UpdatePlayer) content;
            System.out.println("Received update. " + updatePlayer.getState());
            player.setGameState(updatePlayer.getState());
        }
    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() != GameState.START_NEGOTIATION;
    }
}

