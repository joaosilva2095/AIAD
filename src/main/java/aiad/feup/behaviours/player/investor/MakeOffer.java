package aiad.feup.behaviours.player.investor;

import aiad.feup.agents.Player;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.intentions.Intention;
import aiad.feup.intentions.InvestClosed;
import aiad.feup.intentions.InvestOpen;
import aiad.feup.intentions.Withdraw;
import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import aiad.feup.models.OfferType;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Behaviour to make offers to managers
 */
public class MakeOffer extends TickerBehaviour {

    private static MakeOffer instance;
    private static long tickPeriod = 2500;

    // Intention
    private List<Intention> intentions;

    /**
     * Current round balance belief
     */
    private double roundBalance;


    private MakeOffer(final Player player) {
        super(player, tickPeriod);
        roundBalance = player.getBalance();
        intentions = new ArrayList<>();
        intentions.add(InvestOpen.getInstance());
        intentions.add(InvestClosed.getInstance());
        intentions.add(Withdraw.getInstance());
    }

    public static MakeOffer getInstance(final Player player) {
        if(instance == null)
            instance = new MakeOffer(player);
        return instance;
    }

    public double getRoundBalance() {
        return roundBalance;
    }

    public void setRoundBalance(double roundBalance) {
        this.roundBalance = roundBalance;
    }

    @Override
    protected void onTick() {
        Player player = (Player) getAgent();
        if(player.getGameState() != GameState.START_NEGOTIATION) {
            stop();
            return;
        }

        Offer offer = planOffer(player);
        if(offer == null)
            return;

        CompanyInformation companyBelief = player.getCompanyInformation(offer.getCompany().getName());
        companyBelief.addOffer(offer);

        RemoteAgent targetManager = offer.getCompany().getOwner();
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        player.sendMessage(targetManager, message, offer);
    }

    /**
     * Plan the offer to be made
     * @param player the player that will make the offer
     * @return the prepared offer
     */
    private Offer planOffer(Player player){
        Random r = new Random();

        double maxWeight = Integer.MIN_VALUE;
        Intention bestIntention = null;
        for(Intention intention : intentions) {
            intention.calculateWeight();
            if(intention.getWeight() > maxWeight) {
                maxWeight = intention.getWeight();
                bestIntention = intention;
            }
        }

        if(bestIntention == null || maxWeight == 0)
            return null;

        // InvestOpen or withdraw
        Company company = bestIntention.getCompany();
        CompanyInformation companyInformation = player.getCompanyInformation(company.getName());
        OfferType type;
        if(bestIntention instanceof InvestOpen || bestIntention instanceof InvestClosed) {
            type = OfferType.INVEST;
        } else {
            type = OfferType.WITHDRAW;
        }

        // Closed deal or not
        boolean closed = false;
        if(bestIntention instanceof InvestClosed)
            closed = true;

        return new Offer(company, companyInformation.getBelievedValue(), closed, type, new RemoteAgent(player.getName()));
    }


}
