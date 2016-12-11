package aiad.feup.desires;

/**
 * The desire of a player to withdraw an offer
 */
public class Withdraw extends Desire {
    private static Withdraw instance;

    public static Withdraw getInstance() {
        if(instance == null)
            instance = new Withdraw();
        return instance;
    }

    private Withdraw() {

    }
}
