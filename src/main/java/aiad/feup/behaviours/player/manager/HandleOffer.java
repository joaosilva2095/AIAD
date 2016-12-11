package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Player;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.intentions.AcceptOffer;
import aiad.feup.intentions.Intention;
import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;
import aiad.feup.models.OfferType;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Receive offers from investors
 */
public class HandleOffer extends OneShotBehaviour {

    private double roundBalance;

    // Intention
    private AcceptOffer intention;

    private Offer offer;

    public HandleOffer(final Player player, Offer offer) {
        super(player);
        this.offer = offer;
        this.roundBalance = player.getRoundBalance();
        this.intention = AcceptOffer.getInstance();
    }

    @Override
    public void action() {
        Player player = (Player)getAgent();
        Company offeredCompany = offer.getCompany();

        CompanyInformation companyBelief = player.getCompanyBeliefs().get(offeredCompany.getName());

        ACLMessage message;
        if(offer.getType() == OfferType.WITHDRAW) {
            if(companyBelief.getCurrentOffer().getInvestor().getName().equalsIgnoreCase(offer.getInvestor().getName())) {
                roundBalance -= offer.getOfferedValue();
            }

            message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        } else {
            companyBelief.addOffer(offer);
            intention.calculateWeight();
            double weight = intention.getWeight();

            if(weight >= 0.5) {
                companyBelief.setCurrentOffer(offer);
                roundBalance += offer.getOfferedValue();
                if(offer.isClosed()) {
                    player.getCompany(offer.getCompany().getName()).close();
                } else {
                    companyBelief.updateBeliefAsManager(offer, true);
                }
                message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            } else {
                message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                companyBelief.updateBeliefAsManager(offer, false);
            }
        }

        player.sendMessage(offer.getInvestor(), message, offer);
    }
}
