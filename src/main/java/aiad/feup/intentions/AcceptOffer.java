package aiad.feup.intentions;

import aiad.feup.agents.Player;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messageObjects.Offer;

/**
 * Accept an offer intention
 */
public class AcceptOffer extends Intention {
    private static AcceptOffer instance;

    private static double INVESTMENT_WEIGHT;
    private static double TIME_ELAPSED_WEIGHT;
    private static double NON_CLOSED_WEIGHT;

    /**
     * Offer for being accepted or not
     */
    private Offer offer;

    public static AcceptOffer getInstance() {
        if(instance == null)
            instance = new AcceptOffer();
        return instance;
    }

    private AcceptOffer() {
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Override
    public void calculateWeight() {
        weight = 0;
        company = offer.getCompany();
        Player player = Player.getInstance();

        CompanyInformation companyInformation = player.getCompanyInformation(company.getName());
        Offer currentOffer = companyInformation.getCurrentOffer();
        if(currentOffer != null && offer.getOfferedValue() < currentOffer.getOfferedValue()) {
            return;
        }

        double timeElapsedRatio = ((System.currentTimeMillis() - player.getRoundStartTime()) / 1000.0) / player.getRoundDuration();
        switch(player.getStyle()) {
            case LOW_RISK:
                INVESTMENT_WEIGHT = 0.8;
                TIME_ELAPSED_WEIGHT = 0.15;
                NON_CLOSED_WEIGHT = 0.05;
                weight = INVESTMENT_WEIGHT * calculateInvestmentWeight(companyInformation.getBelievedValue(), offer.getOfferedValue());
                weight += TIME_ELAPSED_WEIGHT * timeElapsedRatio;
                if(!offer.isClosed())
                    weight += NON_CLOSED_WEIGHT;
                break;
            case HIGH_RISK:
                INVESTMENT_WEIGHT = 0.50;
                TIME_ELAPSED_WEIGHT = 0.30;
                NON_CLOSED_WEIGHT = 0.20;
                weight = INVESTMENT_WEIGHT * calculateInvestmentWeight(companyInformation.getBelievedValue(), offer.getOfferedValue());
                weight += TIME_ELAPSED_WEIGHT * timeElapsedRatio;
                if(!offer.isClosed())
                    weight += NON_CLOSED_WEIGHT;
                break;
            case RANDOM:
                weight = Math.random();
                break;
        }
    }

    /**
     * Calculate the weight of an investment. As the believed value approaches the maximum believed value for a company after fluctuation
     * the investment weight is reduced quadratically.
     * @param believedValue believed value
     * @param offeredValue value that was offered
     * @return investment weight
     */
    private double calculateInvestmentWeight(double believedValue, double offeredValue){
        return offeredValue / believedValue;
    }
}