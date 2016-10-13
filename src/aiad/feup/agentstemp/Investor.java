package aiad.feup.agentstemp;

/**
 * The investor player.
 * Hes goal is to buy each share for as little as possible.
 */
public class Investor extends Player {

    /**
     * Current number of tokens
     */
    private int numberTokens;

    /**
     * Constructor of Investor
     * @param name name of the investor
     * @param money money of the investor
     */
    public Investor(final String name, final int money) {
        super(name, money);
        this.numberTokens = 3;
    }
}
