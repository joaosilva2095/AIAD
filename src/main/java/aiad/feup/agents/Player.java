package aiad.feup.agents;

import aiad.feup.models.Company;
import aiad.feup.models.PlayerType;

import java.util.ArrayList;
import java.util.List;

/**
 * The player super class.
 * Holds information common to all players
 */
public abstract class Player {

    /**
     * Name of the player
     */
    private final String name;

    /**
     * Balance of the player
     */
    private int balance;

    /**
     * Type of the player
     */
    private PlayerType type = PlayerType.NONE;

    /**
     * List with all companies
     */
    private List<Company> companies;

    /**
     * Number of tokens of the player
     */
    private int numberTokens;

    /**
     * Constructor of Player
     * @param name    name of the player
     * @param balance balance of the player
     */
    public Player(final String name, final int balance) {
        this.name = name;
        this.balance = balance;
        this.companies = new ArrayList<>();
        this.numberTokens = 0;
    }

    /**
     * Get the name of the player
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current balance of the player
     * @return current balance of the player
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Get the player type
     * @return player type
     */
    public PlayerType getType() {
        return type;
    }

    /**
     * Set the balance of the player
     * @param balance balance of the player
     */
    public void setBalance(final int balance) { this.balance = balance; }

    /**
     * Set the type of the player
     * @param type type of the player
     */
    public void setType(PlayerType type) {
        this.type = type;
    }

    /**
     * Set the companies of the player
     * @param companies companies of the player
     */
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    /**
     * Add tokens to the player
     * @param numberTokens number of tokens to be added
     */
    public void addTokens(int numberTokens) {
        if(numberTokens < 0)
            throw new IllegalArgumentException("Number of tokens to be added must be positive");
        this.numberTokens += numberTokens;
    }

    /**
     * Remove a token from the player
     */
    public void removeToken() {
        this.removeTokens(1);
    }

    /**
     * Remove tokens to the player
     * @param numberTokens number of tokens to be removed
     */
    public void removeTokens(int numberTokens) {
        if(numberTokens < 0)
            throw new IllegalArgumentException("Number of tokens to be added must be positive");
        this.numberTokens -= numberTokens;
    }

    /**
     * Add money to the player
     * @param money quantity to be added
     */
    public void addMoney(final int money) {
        if(money < 0)
            throw new IllegalArgumentException("Money to be added must be positive");
        this.balance += money;
    }

    /**
     * Remove money from the player
     * @param money quantity to be removed
     */
    public void removeMoney(final int money) {
        if(money < 0)
            throw new IllegalArgumentException("Money to be added must be positive");
        this.balance -= money;
    }
}

