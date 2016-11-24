package aiad.feup.messages;

import aiad.feup.models.Company;

/**
 * Auction sale
 */
public class AuctionSale extends Message {

    /**
     * Company that will the auctioned
     */
    private Company company;

    /**
     * Number of remaining companies for auction
     */
    private int remainingCompanies;

    /**
     * Constructor of AuctionSale
     * @param company company that will be auctioned
     * @param remainingCompanies number of remaining companies to be auctioned
     */
    public AuctionSale(Company company, int remainingCompanies) {
        this.company = company;
        this.remainingCompanies = remainingCompanies;
    }

    /**
     * Get the company that will be auctioned
     * @return company that will be auctioned
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Get the number of remaining companies for auction
     * @return number of remaining companies for auction
     */
    public int getRemainingCompanies() {
        return remainingCompanies;
    }
}
