package aiad.feup.agents;

import aiad.feup.exceptions.DuplicatedItemException;
import aiad.feup.models.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Board Agent
 * Responsible for maintaining the board state
 */
public class Board extends GameAgent {

    /**
     * Players of the game
     */
    private List<RemoteAgent> players;

    /**
     * Balances of the remote agents
     */
    private Map<RemoteAgent, Integer> balances;

    /**
     * Tokens of the remote agents
     */
    private Map<RemoteAgent, Integer> tokens;

    /**
     * Companies of the game
     */
    private List<Company> companies;

    /**
     * Constructor of Board
     */
    public Board() {
        this.players = new ArrayList<>();
        this.companies = new ArrayList<>();
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
            if (company.getValue() <= 0) {
                continue;
            }

            System.out.println("Company " + company.getName() + " value before fluctuation: " + company.getValue());
            company.applyFluctuation();
            System.out.println("Company " + company.getName() + " value after fluctuation: " + company.getValue());
        }
    }

    /**
     * Get the number of players in the game
     *
     * @return number of players in the game
     */
    public int getNumberPlayers() {
        return players.size();
    }

    /**
     * Get all the players in the game
     *
     * @return all players in the game
     */
    public List<RemoteAgent> getPlayers() {
        return players;
    }

    /**
     * Get a player by its name
     * @param name name of the player to get
     * @return player with that name
     */
    public RemoteAgent getPlayer(final String name) {
        for (RemoteAgent player : players) {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    /**
     * Get all the companies in the game
     *
     * @return all companies in the game
     */
    public List<Company> getCompanies() {
        return companies;
    }
}
