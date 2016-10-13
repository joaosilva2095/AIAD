package aiad.feup.Agents;

import aiad.feup.Core.Company;

import java.util.*;

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
     * Money of the player
     */
    private int money;

    /**
     * Companies owned by the player
     */
    private final List<Company> companies;

    /**
     * Constructor of Player
     * @param name name of the player
     * @param money money of the player
     */
    public Player(final String name, final int money) {
        this.name = name;
        this.money = money;
        this.companies = new ArrayList<>();
    }

    /**
     * Get the name of the player
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current money of the player
     * @return current money of the player
     */
    public int getMoney() {
        return money;
    }

    /**
     * Add money to the player
     * @param money quantity to be added
     */
    public void addMoney(final int money) {
        this.money += money;
    }

    /**
     * List all companies owned by the player
     * @return companies owned by the player
     */
    public List<Company> getCompanies() {
        return Collections.unmodifiableList(companies);
    }

    /**
     * Add a company to the player
     * @param company company to be added
     */
    public void addCompany(final Company company) {
        this.companies.add(company);
    }

    /**
     * Remove a company from the player
     * @param company company to be removed
     */
    public void removeCompany(final Company company) {
        this.companies.remove(company);
    }
}
