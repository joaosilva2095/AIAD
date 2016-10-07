package ldso.feup;

/**
 * The Board singleton class.
 * Represents the board state and holds all information common throughout the elements of the board
 * Only 1 instance of Board may exist at any given time
 */
public class Board {
    private static Board instance;

    private int numberPlayers;

    private Player[] players;
    private Manager[] managers;
    private Investor[] investors;

    private Company[] companies;

    public static Board getInstance(){
        if (instance == null)
            instance = new Board();

        return instance;
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }
    public void setNumberPlayers(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }
    public Player[] getPlayers() {
        return players;
    }
    public void setPlayers(Player[] players) {
        this.players = players;
    }
    public Manager[] getManagers() {
        return managers;
    }
    public void setManagers(Manager[] managers) {
        this.managers = managers;
    }
    public Investor[] getInvestors() {
        return investors;
    }
    public void setInvestors(Investor[] investors) {
        this.investors = investors;
    }
    public Company[] getCompanies() {
        return companies;
    }
    public void setCompanies(Company[] companies) {
        this.companies = companies;
    }
}
