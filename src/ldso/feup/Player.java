package ldso.feup;

import java.util.ArrayList;

/**
 * The player super class.
 * Holds information common to all players
 */
public class Player {

    private String name;

    private int money;

    private ArrayList<Company> companies;



    public Player(String name, int money) {
        this.name = name;
        this.money = money;
        this.companies = new ArrayList<Company>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public void addCompany(Company company) {
        this.companies.add(company);
    }

    public void removeCompany(Company company) {
        this.companies.remove(company);
    }


}
