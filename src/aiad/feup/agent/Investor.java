package aiad.feup.agent;

import aiad.feup.agent.exceptions.BalanceException;
import aiad.feup.agent.exceptions.InvestmentException;
import aiad.feup.core.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * The investor player.
 * Hes goal is to buy each share for as little as possible.
 */

public class Investor extends Player {

    /**
     * Companies where the player has invested
     */
    private final List<Company> investedCompanies;

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
        this.investedCompanies = new ArrayList<>();
        this.numberTokens = 3;
    }

    /**
     * Add a player investment
     * @param company company where the player has invested
     */
    public void addInvestment(final Company company) throws InvestmentException, BalanceException {
        if(this.investedCompanies.contains(company))
            throw new InvestmentException(this, "already invested in the company.");

        this.investedCompanies.add(company);
    }

    /**
     * Remove a player investment
     * @param company company to be removed
     */
    public void removeInvestment(final Company company) throws InvestmentException {
        if(!this.investedCompanies.contains(company))
            throw new InvestmentException(this, "not invested in the company");

        this.investedCompanies.remove(company);
    }
}
