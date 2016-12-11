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
            companyBelief.removeOffers(offer.getInvestor().getName());

            if(companyBelief.getCurrentOffer().getInvestor().getName().equalsIgnoreCase(offer.getInvestor().getName())) {
                roundBalance -= offer.getOfferedValue();
                companyBelief.setCurrentOffer(companyBelief.getOffers().peek());
            }
            message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        } else {
            intention.calculateWeight();

            if(intention.getWeight() >= 0.5) {
                if(companyBelief.getCurrentOffer() != null)
                    roundBalance -= companyBelief.getCurrentOffer().getOfferedValue();
                roundBalance += offer.getOfferedValue();

                companyBelief.addOffer(offer);
                companyBelief.setCurrentOffer(offer);
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
