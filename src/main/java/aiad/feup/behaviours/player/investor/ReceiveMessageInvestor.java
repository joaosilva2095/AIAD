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

        switch (player.getGameState()){
            case START_NEGOTIATION:
                // Received UpdatePlayer (end of round)
                if(content instanceof UpdatePlayer) {
                    UpdatePlayer updatePlayer = (UpdatePlayer) content;
                    player.setGameState(updatePlayer.getState());
                }
                else if(content instanceof Offer) {
                    Offer offer = (Offer) content;
                    // Received standard ACL message
                    if(message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        CompanyInformation companyBelief = player.getCompanyBeliefs().get(offer.getCompany().getName());
                        companyBelief.setCurrentOffer(offer);
                    } else if (message.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                    }
                }
                break;
            case END_NEGOTIATION:
                // Received UpdatePlayer
                if(content instanceof UpdatePlayer) {
                    UpdatePlayer updatePlayer = (UpdatePlayer) content;
                    if(updatePlayer.getState() != GameState.START_NEGOTIATION)
                        return;

                    player.setCompanies(updatePlayer.getCompanyList());
                    player.setTokens(updatePlayer.getTokens());
                    player.setBalance(updatePlayer.getBalance());
                    player.setGameState(updatePlayer.getState());
                    player.generateCompanyBeliefs();

                    player.addBehaviour(player.getFactory().wrap(MakeOffer.getInstance(player)));
                    MakeOffer.getInstance(player).setRoundBalance(updatePlayer.getBalance());

                    player.setRoundStartTime(System.currentTimeMillis());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean done() {
        GameState state = ((Player)getAgent()).getGameState();
        return state != GameState.START_NEGOTIATION && state != GameState.END_NEGOTIATION;
    }
}

