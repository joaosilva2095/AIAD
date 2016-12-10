package aiad.feup.beliefs;

import aiad.feup.messageObjects.Offer;
import aiad.feup.models.Company;

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
     * Automatically generates some noise for the values to assign for the beliefs so that there's an error.
     * Minimum and max error is configurable
     * @param company the company to form the belief for
     */
    public CompanyInformation(Company company){
        this.offers = new ArrayList<>();

        Random r = new Random();

        int maxError = 30;
        int minError = 5;
        int error = r.nextInt(maxError - minError) + minError;
        error /= 100;
        if(r.nextBoolean())
            this.believedValue = company.getValue()* (1+error);
        else
            this.believedValue =  company.getValue()* (1-error);

        error = r.nextInt(maxError-minError) + minError;
        if(r.nextBoolean())
            this.believedFluctuation = company.getFluctuation() * (1+error);
        else
            this.believedFluctuation =  company.getFluctuation()  * (1-error);
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

}
