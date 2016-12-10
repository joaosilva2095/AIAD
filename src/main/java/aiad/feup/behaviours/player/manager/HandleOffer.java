package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Player;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive offers from investors
 */
public class HandleOffer extends OneShotBehaviour {

    private double roundBalance;
    private Offer offer;

    public HandleOffer(final Player player, Offer offer) {
        super(player);
        this.offer = offer;
        this.roundBalance = player.getRoundBalance();

    }

    @Override
    public void action() {
        Player player = (Player)getAgent();
        Company offeredCompany = offer.getCompany();

        CompanyInformation companyBelief = player.getCompanyBeliefs().get(offeredCompany.getName());
        companyBelief.addOffer(offer);
        ACLMessage message;
        if(offeredCompany.getValue() > companyBelief.getBelievedValue() / 2) {
            companyBelief.setCurrentOffer(offer);
            message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        }
        else {
            message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
        }

        player.sendMessage(offer.getInvestor(), message, offer);
    }
}
