package aiad.feup.agent;

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
     * Constructor of Player
     *
     * @param name    name of the player
     * @param balance balance of the player
     */
    public Player(final String name, final int balance) {
        this.name = name;
        this.balance = balance;
    }

    /**
     * Get the name of the player
     *
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current balance of the player
     *
     * @return current balance of the player
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Add money to the player
     *
     * @param money quantity to be added
     */
    public void addMoney(final int money) {
        this.balance += money;
    }

    /**
     * Remove money from the player
     *
     * @param money quantity to be removed
     */
    public void removeMoney(final int money) {
        this.balance -= money;
    }



}

