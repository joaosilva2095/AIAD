package aiad.feup.messages;

import aiad.feup.exceptions.MalformedObjectException;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;

import java.io.Serializable;
import java.util.List;

/**
 * UpdatePlayer object. Requests player agents to update their behaviour
 */
public class UpdatePlayer implements Serializable {

    /**
     * Serial Version UUID
     */
    private static final long serialVersionUID = -7590826565839178942L;

    /**
     * The new balance of the player
     */
    private double balance;

    /**
     * Number of tokens of the player
     */
    private int tokens;

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
     * New state of the game
     */
    private GameState state;

    /**
     * The UpdatePlayer constructor
     * @param balance the balance for the player
     * @param tokens tokens for the player
     * @param companyList the list of companies for the player
     * @param numberInvestors the number of investors still in game
     * @param numberManagers the number of Managers still in game
     * @param state state of the game
     */
    public UpdatePlayer(double balance, int tokens, List<Company> companyList, int numberInvestors, int numberManagers, GameState state) {
        if(numberInvestors < 0 || numberManagers < 0)
            throw new MalformedObjectException("There can be no negative investors or managers.");

        this.balance = balance;
        this.tokens = tokens;
        this.companyList = companyList;
        this.numberInvestors = numberInvestors;
        this.numberManagers = numberInvestors;
        this.state = state;
    }

    public double getBalance() {
        return balance;
    }

    public int getTokens() {
        return tokens;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public int getNumberInvestors() {
        return numberInvestors;
    }

    public int getNumberManagers() {
        return numberManagers;
    }

    public GameState getState() {
        return state;
    }
}
