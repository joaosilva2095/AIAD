package aiad.feup.agents;

import aiad.feup.exceptions.DuplicatedItemException;
import aiad.feup.models.Company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Board Agent
 * Responsible for maintaining the board state
 */
public class Board extends GameAgent {

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
            if(company.getValue() <= 0){
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
        return players.length;
    }

    /**
     * Get all the players in the game
     * @return all players in the game
     */
    public Player[] getPlayers() {
        return Arrays.copyOf(players, players.length);
    }

    /**
     * Get all the companies in the game
     * @return all companies in the game
     */
    public List<Company> getCompanies() {
        return companies;
    }
}
