package aiad.feup.behaviours.player.investor;

import aiad.feup.agents.Player;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.desires.Desire;
import aiad.feup.desires.Invest;
import aiad.feup.desires.Withdraw;
import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import aiad.feup.models.OfferType;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Behaviour to make offers to managers
 */
public class MakeOffer extends TickerBehaviour {

    private static MakeOffer instance;
    private static long tickPeriod = 2500;

    // Desire
    private List<Desire> desires;

    /**
     * Current round balance belief
     */
    private double roundBalance;


    private MakeOffer(final Player player) {
        super(player, tickPeriod);
        roundBalance = player.getBalance();
        desires = new ArrayList<>();
        desires.add(Invest.getInstance());
        desires.add(Withdraw.getInstance());
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

        CompanyInformation companyBelief = player.getCompanyBeliefs().get(offer.getCompany().getName());
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

        List<Company> companies = player.getCompanies();

        Invest invest = Invest.getInstance();
        invest.calculateDesire();

        Company targetCompany = companies.get(r.nextInt(companies.size()));
        CompanyInformation companyBelief = player.getCompanyBeliefs().get(targetCompany.getName());

        Offer theOffer = new Offer(targetCompany, companyBelief.getBelievedValue(), r.nextBoolean(), OfferType.BUY, new RemoteAgent(player.getName()));
        return theOffer;
    }


}
