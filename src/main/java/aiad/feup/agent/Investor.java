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
     * @throws InvestmentException when the player has already invested in the company
     * or when he tries to invest on a closed company
     */
    public void addInvestment(final Company company) throws InvestmentException {
        if(this.investedCompanies.contains(company))
            throw new InvestmentException(this, "already invested in the company.");

        if(company.isClosed())
            throw new InvestmentException(this, "tried to invest on a closed company.");

        this.investedCompanies.add(company);
    }

    /**
     * Remove a player investment
     * @param company company to be removed
     * @throws InvestmentException when the player has not invested in the company or
     * is trying to remove the investment of a closed company
     */
    public void removeInvestment(final Company company) throws InvestmentException {
        if(!this.investedCompanies.contains(company))
            throw new InvestmentException(this, "not invested in the company");

        if(company.isClosed())
            throw new InvestmentException(this, "tried to remove a investment on a closed company.");

        this.investedCompanies.remove(company);
    }
}
