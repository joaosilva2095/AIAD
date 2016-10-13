package aiad.feup.agent;

import aiad.feup.agent.exceptions.InvestmentException;
import aiad.feup.core.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * The manager player.
 * Hes goal is to get as money as he can for each company share sold.
 */
public class Manager extends Player {

    /**
     * Companies the manager currently owns
     */
    private List<Company> ownedCompanies;

    /**
     * Constructor of Manager
     * @param name name of the manager
     * @param money money of the manager
     */
    public Manager(final String name, final int money) {
        super(name, money);
        this.ownedCompanies = new ArrayList<>();
    }

    /**
     * Adds a company to the owned companies of a Manager
     * @param company the company to be added
     * @throws InvestmentException throws exception if Manager tries to buy a company he already owns
     */
    public void addCompany(Company company) throws InvestmentException {
        if(ownedCompanies.contains(company))
            throw new InvestmentException(this, "is already owner of the company: " + company.getName());

        ownedCompanies.add(company);
    }

    /**
     * Removes a company to from the owned companies of a Manager
     * @param company the company to be removed
     * @throws InvestmentException throws exception if Manager tries to remove a company he doesn't own
     */
    public void removeCompany(Company company) throws InvestmentException {
        if(!ownedCompanies.contains(company))
            throw new InvestmentException(this, "is not owner of the company: " + company.getName());

        ownedCompanies.remove(company);
    }
}