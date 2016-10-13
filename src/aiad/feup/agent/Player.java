package aiad.feup.agent;

import aiad.feup.core.Company;

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
     * Balance of the player
     */
    private int balance;

    /**
     * Companies owned by the player
     */
    private final List<Company> companies;

    /**
     * Constructor of Player
     * @param name name of the player
     * @param balance balance of the player
     */
    public Player(final String name, final int balance) {
        this.name = name;
        this.balance = balance;
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
     * Get the current balance of the player
     * @return current balance of the player
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Add money to the player
     * @param money quantity to be added
     */
    public void addMoney(final int money) {
        this.balance += money;
    }

