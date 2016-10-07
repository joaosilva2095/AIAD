package aiad.feup;

/**
 * The Investor class
 * Represents an investor player
 */
public class Investor extends Player {

    private int numberTokens;

    public Investor (String name, int money) {
        super(name, money);
        this.numberTokens = 3;
    }


}
