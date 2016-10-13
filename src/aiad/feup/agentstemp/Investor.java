<<<<<<< HEAD:src/aiad/feup/Agents/Investor.java
package aiad.feup.agents;
=======
package aiad.feup.agentstemp;
>>>>>>> bbd2ae9ac49b0534f40319592a116478bb9402b1:src/aiad/feup/agentstemp/Investor.java

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
