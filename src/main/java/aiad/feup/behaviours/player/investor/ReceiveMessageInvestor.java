package aiad.feup.behaviours.player.investor;
import aiad.feup.agents.Player;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messageObjects.Offer;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import aiad.feup.models.OfferType;
import com.oracle.deploy.update.Updater;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.text.DecimalFormat;

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
        player.handleKick(content);

        switch (player.getGameState()){
            case START_NEGOTIATION:
                // Received UpdatePlayer (end of round)
                if(content instanceof UpdatePlayer) {
                    UpdatePlayer updatePlayer = (UpdatePlayer) content;
                    System.out.println("Received update player " + updatePlayer.getState() + " | " + player.getGameState());
                    player.setGameState(updatePlayer.getState());
                }
                else if(content instanceof Offer) {
                    Offer offer = (Offer) content;
                    if(offer.getType() == OfferType.WITHDRAW)
                        return;

                    CompanyInformation companyBelief = player.getCompanyBeliefs().get(offer.getCompany().getName());

                    DecimalFormat df = new DecimalFormat("#0.00");

                    // Received standard ACL message
                    if(message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        System.out.println("[ACCEPT] " + offer.getCompany().getName() + " for " + df.format(offer.getOfferedValue()) + "€ (closed: " + offer.isClosed() + ")");
                        companyBelief.setCurrentOffer(offer);
                        MakeOffer makeOfferInstance = MakeOffer.getInstance(player, false);
                        makeOfferInstance.setRoundBalance(makeOfferInstance.getRoundBalance() - offer.getOfferedValue());
                        if(offer.isClosed()) {
                            player.getCompany(offer.getCompany().getName()).close();
                        } else {
                            companyBelief.updateBeliefAsInvestor(offer, true);
                        }
                    } else if (message.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                        System.out.println("[REJECT] " + offer.getCompany().getName() + " for " + df.format(offer.getOfferedValue()) + "€ (closed: " + offer.isClosed() + ")");
                        companyBelief.updateBeliefAsInvestor(offer, false);
                    }
                }
                break;
            case END_NEGOTIATION:
                // Received UpdatePlayer
                if(content instanceof UpdatePlayer) {
                    UpdatePlayer updatePlayer = (UpdatePlayer) content;
                    if(updatePlayer.getState() != GameState.START_NEGOTIATION
                            && updatePlayer.getState() != GameState.WAIT_WINNERS)
                        return;

                    player.setCompanies(updatePlayer.getCompanyList());
                    player.setTokens(updatePlayer.getTokens());
                    player.setBalance(updatePlayer.getBalance());
                    player.setGameState(updatePlayer.getState());
                    player.generateCompanyBeliefs();

                    MakeOffer makeOfferInstance = MakeOffer.getInstance(player, true);
                    player.addBehaviour(player.getFactory().wrap(makeOfferInstance));
                    makeOfferInstance.setRoundBalance(updatePlayer.getBalance());

                    player.setRoundStartTime(System.currentTimeMillis());
                    if(updatePlayer.getState() == GameState.START_NEGOTIATION)
                        player.incrementRoundNumber();
                }
                break;
            case KICKED:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean done() {
        GameState state = ((Player)getAgent()).getGameState();
        return state != GameState.START_NEGOTIATION && state != GameState.END_NEGOTIATION && state != GameState.KICKED && state != GameState.WAIT_WINNERS;
    }
}

