package aiad.feup.beliefs;

import aiad.feup.models.Company;

import java.util.Random;

/**
 * Company Information belief.
 * Stores the believed information of a company
 */
public class CompanyInformation extends Belief{

    private double believedFluctuation;
    private double believedValue;


    /**
     * Constructor for CompanyInformation belief
     * Automatically generates some noise for the values to assign for the beliefs so that there's an error.
     * Minimum and max error is configurable
     * @param company the company to form the belief for
     */
    public CompanyInformation(Company company){
        Random r = new Random();

        int maxError = 30;
        int minError = 5;
        int error = r.nextInt(maxError - minError) + minError;
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
}
