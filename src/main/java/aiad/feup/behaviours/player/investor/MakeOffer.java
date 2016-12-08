package aiad.feup.behaviours.player.investor;

import aiad.feup.agents.Board;
import aiad.feup.agents.Player;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messages.Offer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;
import java.util.Random;

/**
 * Behaviour to make offers to managers
 */
public class MakeOffer extends TickerBehaviour {

    private static MakeOffer instance;
    private static long tickPeriod = 5000;

    private double roundBalance;


    private MakeOffer(final Player player) {
        super(player, tickPeriod);
        roundBalance = player.getBalance();
    }

    public static MakeOffer getInstance(final Player player) {
        if(instance == null)
            instance = new MakeOffer(player);
        return instance;
    }


    public void setRoundBalance(double roundBalance) {
        this.roundBalance = roundBalance;
    }

    @Override
    protected void onTick() {
        System.out.println("Making offer");
        Player player = (Player) getAgent();
        Offer offer = planOffer(player);
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

        List<Company> companies = player.getCompanies();

        Company targetCompany = companies.get(r.nextInt(companies.size()));
        CompanyInformation companyBelief = player.getCompanyBeliefs().get(targetCompany);

        Offer theOffer = new Offer(targetCompany, companyBelief.getBelievedValue(), r.nextBoolean(), new RemoteAgent(player.getName()));
        return theOffer;
    }
}
