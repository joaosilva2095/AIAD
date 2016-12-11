package aiad.feup.beliefs;

import aiad.feup.agents.Player;
import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;
import aiad.feup.models.PlayerStyle;
import aiad.feup.models.PlayerType;

import java.util.*;

/**
 * Company Information belief.
 * Stores the believed information of a company
 */
public class CompanyInformation extends Belief{

    private double believedFluctuation;
    private double believedValue;
    private double maximumBelievedValue;
    private double minimumBelievedValue;
    private Deque<Offer> offers;
    private Offer currentOffer;


    /**
     * Constructor for CompanyInformation belief
     * @param company the company to form the belief for
     */
    public CompanyInformation(Company company){
        this.offers = new ArrayDeque<>();

        Player player = Player.getInstance();

        initBalances(company);

    }

    public double getBelievedFluctuation() {
        return believedFluctuation;
    }

    public void setBelievedFluctuation(double believedFluctuation) {
        this.believedFluctuation = believedFluctuation;
    }

    public double getBelievedValue() {
        return believedValue;
    }

    public double getMaximumBelievedValue() {
        return maximumBelievedValue;
    }

    public double getMinimumBelievedValue() {
        return minimumBelievedValue;
    }

    public void setBelievedValue(double believedValue) {
        this.believedValue = believedValue;
    }

    public void addOffer(Offer offer){
        offers.push(offer);
    }

    public void setCurrentOffer(Offer currentOffer) {
        this.currentOffer = currentOffer;
    }

    public void removeOffer(Offer offer){
        offers.remove(offer);
    }

    public void removeOffers(String investor) {
        for(Offer offer : new ArrayDeque<>(offers)) {
            if(offer.getInvestor().getName().equalsIgnoreCase(investor))
                removeOffer(offer);
        }
    }

    public Offer getCurrentOffer() { return currentOffer; }

    public Deque<Offer> getOffers() { return offers; }

    public void reset() {
        offers = new ArrayDeque<>();
        currentOffer = null;
    }

    /**
     * Update a belief based on an offer
     * @param offer offer that was received
     */
    public void updateBeliefAsManager(Offer offer, boolean accepted) {
        Player player = Player.getInstance();
        if(accepted) {
            if(player.getStyle() == PlayerStyle.RANDOM) {
                believedValue = minimumBelievedValue + Math.random() * (maximumBelievedValue - minimumBelievedValue);
            } else {
                if(offer.getOfferedValue() > maximumBelievedValue)
                    maximumBelievedValue = offer.getOfferedValue();
                double addValue = (maximumBelievedValue - minimumBelievedValue) / (int) (Math.random() * 15 + 5);
                believedValue += addValue;
            }
        } else {
            if(player.getStyle() == PlayerStyle.RANDOM) {
                believedValue = maximumBelievedValue - Math.random() * (maximumBelievedValue - minimumBelievedValue);
            } else {
                double removeValue = 0;
                double offeredValue = offer.getOfferedValue();
                removeValue = (maximumBelievedValue - minimumBelievedValue) / (int) (Math.random() * 15 + 5);

                if(offer.isClosed())
                    removeValue *= 0.5;

                believedValue -= removeValue;
                if(believedValue < minimumBelievedValue || believedValue < offeredValue)
                    believedValue = Math.max(minimumBelievedValue, offeredValue);
            }
        }
    }

    /**
     * Update the believed value in the company
     * @param offer offer that was made
     * @param accepted accepted or not
     */
    public void updateBeliefAsInvestor(Offer offer, boolean accepted) {
        if(accepted)
            return;

        Player player = Player.getInstance();
        if(player.getStyle() == PlayerStyle.RANDOM) {
            believedValue = minimumBelievedValue + Math.random() * (maximumBelievedValue - minimumBelievedValue);
        } else {
            double offeredValue = offer.getOfferedValue();
            believedValue = offeredValue + ((maximumBelievedValue - minimumBelievedValue) / (int) (Math.random() * 15 + 5));
            double maxOfferValue = (minimumBelievedValue + ((maximumBelievedValue - minimumBelievedValue) / 2) * 1.2);
            if(believedValue >= maxOfferValue)
                believedValue = offeredValue;
        }
    }

    /**
     * Automatically generates some noise for the values to assign for the beliefs so that there's an error.
     * Minimum and max error is configurable
     * @param company the company to initialize balances for
     */
    private void initBalances(Company company){
        Random r = new Random();
        Player player = Player.getInstance();
        int maxError = 40;
        int minError = 15;
        int error = r.nextInt(maxError - minError) + minError;
        error /= 100;
        if(r.nextBoolean())
            this.believedFluctuation = (company.getFluctuation() / 100.0) * (1+error);
        else
            this.believedFluctuation =  (company.getFluctuation() / 100.0)  * (1-error);

        if(player.getType() == PlayerType.INVESTOR) {
            this.believedValue =  company.getValue() * (1 - believedFluctuation);
            this.maximumBelievedValue = company.getValue() * (1 + believedFluctuation);
            this.minimumBelievedValue = this.believedValue;
        } else {
            this.believedValue =  company.getValue() * (1 + believedFluctuation);
            this.minimumBelievedValue = company.getValue() * (1 - believedFluctuation);
            this.maximumBelievedValue = this.believedValue;
        }
    }


}
