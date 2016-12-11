package aiad.feup.beliefs;

import aiad.feup.agents.Player;
import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;
import aiad.feup.models.PlayerStyle;
import aiad.feup.models.PlayerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Company Information belief.
 * Stores the believed information of a company
 */
public class CompanyInformation extends Belief{

    private double believedFluctuation;
    private double believedValue;
    private double maximumBelievedValue;
    private double minimumBelievedValue;
    private List<Offer> offers;
    private Offer currentOffer;


    /**
     * Constructor for CompanyInformation belief
     * @param company the company to form the belief for
     */
    public CompanyInformation(Company company){
        this.offers = new ArrayList<>();

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
        offers.add(offer);
    }

    public void setCurrentOffer(Offer currentOffer) {
        this.currentOffer = currentOffer;
    }

    public void removeOffer(Offer offer){
        offers.remove(offer);
    }

    public Offer getCurrentOffer() { return currentOffer; }

    public List<Offer> getOffers() { return offers; }

    public void reset() {
        offers = new ArrayList<>();
        currentOffer = null;
    }

    /**
     * Update a belief based on an offer
     * @param offer offer that was received
     */
    public void updateBeliefAsManager(Offer offer, boolean accepted) {
        if(accepted)
            return;

        Player player = Player.getInstance();
        if(player.getStyle() == PlayerStyle.RANDOM) {
            believedValue = maximumBelievedValue - Math.random() * (maximumBelievedValue - minimumBelievedValue);
        } else {
            double offeredValue = offer.getOfferedValue();
            believedValue = offeredValue + ((maximumBelievedValue - minimumBelievedValue) / (int) (Math.random() * 15 + 5));
            double maxOfferValue = (minimumBelievedValue + ((maximumBelievedValue - minimumBelievedValue) / 2) * 1.2);
            if(believedValue >= maxOfferValue)
                believedValue = offeredValue;
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
            this.believedFluctuation = company.getFluctuation() * (1+error);
        else
            this.believedFluctuation =  company.getFluctuation()  * (1-error);

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
