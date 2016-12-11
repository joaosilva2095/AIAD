package aiad.feup.beliefs;

import aiad.feup.agents.Player;
import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;
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

    public void updateBelief(Offer offer, boolean accepted) {

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
        } else {
            this.believedValue = company.getValue() * (1 + believedFluctuation);
        }
    }


}
