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
    private static double CLOSED_WEIGHT;

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

        Offer currentOffer = player.getCompanyInformation(company.getName()).getCurrentOffer();
        if(currentOffer != null && offer.getOfferedValue() < currentOffer.getOfferedValue()) {
            return;
        }

        double timeElapsedRatio = ((System.currentTimeMillis() - player.getRoundStartTime()) / 1000) / player.getRoundDuration();
        switch(player.getStyle()) {
            case HIGH_RISK:
                INVESTMENT_WEIGHT = 0.7;
                TIME_ELAPSED_WEIGHT = 0.2;
                CLOSED_WEIGHT = 0.1;

                weight += TIME_ELAPSED_WEIGHT * timeElapsedRatio;
                if(!offer.isClosed())
                    weight += CLOSED_WEIGHT;
                break;
            case LOW_RISK:
                INVESTMENT_WEIGHT = 0.65;
                TIME_ELAPSED_WEIGHT = 0.3;
                CLOSED_WEIGHT = 0.05;
                weight += TIME_ELAPSED_WEIGHT * timeElapsedRatio;
                if(!offer.isClosed())
                    weight += CLOSED_WEIGHT;
                break;
            case RANDOM:
                weight = Math.random();
                break;
        }
    }

    /**
     * Calculate the weight of an investment. As the believed value approaches the maximum believed value for a company after fluctuation
     * the investment weight is reduced quadratically.
     * @param companyInformation company information
     * @param offeredValue value that was offered
     * @return investment weight
     */
    private double calculateInvestmentWeight(CompanyInformation companyInformation, double offeredValue){
        double ratio = offeredValue / companyInformation.getBelievedValue();
        return Math.pow(ratio, 2);
    }
}