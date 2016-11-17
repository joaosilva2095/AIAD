package aiad.feup.messages;

import aiad.feup.messages.exceptions.MalformedObjectException;
import aiad.feup.models.Company;
import com.oracle.deploy.update.Updater;

import java.util.List;

/**
 * UpdatePlayer object. Requests player agents to update their behaviour
 */
public class UpdatePlayer extends Message {

    /**
     * The new balance of the player
     */
    private double balance;

    /**
     * the list of companies for the player to work with
     */
    private List<Company> companyList;

    /**
     * The number of investors still in game
     */
    private int numberInvestors;

    /**
     * The number of Managers still in game
     */
    private int numberManagers;

    /**
     * The UpdatePlayer constructor
     * @param balance the balance for the player
     * @param companyList the list of companies for the player
     * @param numberInvestors the number of investors still in game
     * @param numberManagers the number of Managers still in game
     */
    public UpdatePlayer(double balance, List<Company> companyList, int numberInvestors, int numberManagers) {

        if(numberInvestors < 0 || numberManagers < 0)
            throw new MalformedObjectException("There can be no negative investors or managers.");

        this.balance = balance;
        this.companyList = companyList;
        this.numberInvestors = numberInvestors;
        this.numberManagers = numberInvestors;
    }




}
