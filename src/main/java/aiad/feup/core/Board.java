package aiad.feup.core;

import aiad.feup.agent.Investor;
import aiad.feup.agent.Manager;
import aiad.feup.agent.Player;
import aiad.feup.core.exceptions.DuplicatedItemException;
import aiad.feup.core.exceptions.MalformedObjectException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Board singleton class.
 * Represents the board state and holds all information common throughout the elements of the board
 * Only 1 instance of Board may exist at any given time
 */
public class Board {

    /**
     * Players of the game
     */
    private Player[] players;

    /**
     * Companies of the game
     */
    private List<Company> companies;

    /**
     * Constructor of Board
     *
     * @param numberPlayers number of players that will play
     */
    public Board(final int numberPlayers) {

        this.players = new Player[numberPlayers];
        this.companies = new ArrayList<>();
    }


    public static void main(String[] args) {
        Board theBoard = new Board(4);

        try {
            Company google = new Company("Google", 1000, true, 40);
            theBoard.addCompany(google);
            Company apple = new Company("Apple", 1200, false, 5);
            theBoard.addCompany(apple);
            Company reddit = new Company("Reddit", 1200, true, 101);
            theBoard.addCompany(reddit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 20; i > 0; i--) {
            theBoard.applyEndOfRoundFluctuation();
        }
    }

    /**
     * Add a company to the board
     *
     * @param company company to be added
     */
    private void addCompany(Company company) {
        if (companies.contains(company))
            throw new DuplicatedItemException("Company " + company.getName() + " is already registered on the board.");
        companies.add(company);
    }

    /**
     * AdRemoved a company from the board
     *
     * @param company company to be removed
     */
    private void removeCompany(Company company) {
        companies.remove(company);
    }

    /**
     * Applies the end of round fluctuation for all companies, bringing drastic change to the market
     */
    private void applyEndOfRoundFluctuation() {
        for (Company company : companies) {
            if(company.getPrice() <= 0){
                continue;
            }
            
            System.out.println("Company " + company.getName() + " value before fluctuation: " + company.getPrice());
            company.applyFluctuation();
            System.out.println("Company " + company.getName() + " value after fluctuation: " + company.getPrice());
        }
    }

    /**
     * Get the number of players in the game
     *
     * @return number of players in the game
     */
    public int getNumberPlayers() {
        return players.length;
    }

    /**
     * Get all the players in the game
     *
     * @return all players in the game
     */
    public Player[] getPlayers() {
        return Arrays.copyOf(players, players.length);
    }

    /**
     * Get all managers in the game
     *
     * @return all managers in the game
     */
    public Manager[] getManagers() {
        return Arrays.stream(players).filter(player -> player instanceof Manager).toArray(Manager[]::new);
    }

    /**
     * Get all investors in the game
     *
     * @return all investors in the game
     */
    public Investor[] getInvestors() {
        return Arrays.stream(players).filter(player -> player instanceof Investor).toArray(Investor[]::new);
    }

    /**
     * Get all the companies in the game
     *
     * @return all companies in the game
     */
    public List<Company> getCompanies() {
        return companies;
    }

    /**
     * Get the owner of a given company
     *
     * @param company company to get the owner
     * @return owner of the company
     */
    public Player getCompanyOwner(final Company company) {
        for (final Manager manager : getManagers()) {
            if (manager.getOwnedCompanies().contains(company))
                return manager;
        }
        return null;
    }

    /**
     * Returns companies that haven't been purchased
     *
     * @return all companies available for purchase
     */
    public List<Company> getOpenCompanies() {
        ArrayList<Company> openCompanies = new ArrayList<>();
        for (Company company : companies) {
            if (getCompanyOwner(company) == null)
                openCompanies.add(company);
        }

        return openCompanies;
    }
}
