package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Player;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messages.Offer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive offers from investors
 */
public class HandleOffer extends OneShotBehaviour {

    private static HandleOffer instance;
    private double roundBalance;
    private Offer offer;


    private HandleOffer(final Player player) {
        super(player);
        roundBalance = player.getBalance();
    }

    public static HandleOffer getInstance(final Player player, Offer offer) {
        if(instance == null)
            instance = new HandleOffer(player);

        instance.setOffer(offer);
        return instance;
    }



    public void setRoundBalance(double roundBalance) {
        this.roundBalance = roundBalance;
    }

    public void setOffer(Offer offer){
        this.offer = offer;
    }

    @Override
    public void action() {
        Player player = (Player)getAgent();
        Company offeredCompany = offer.getCompany();

        CompanyInformation companyBelief = player.getCompanyBeliefs().get(offeredCompany.getName());
        ACLMessage message;
        if(offeredCompany.getValue() > companyBelief.getBelievedValue()) {
            message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            System.out.println("Accepting the offer");
        }
        else {
            message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
            System.out.println("Refusing the offer");
        }


        player.sendMessage(offer.getOfferer(), message, null);
    }
}
