package aiad.feup.intentions;

import aiad.feup.agents.Player;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.models.Company;

import java.util.Map;

/**
 * The desire of a player to withdraw an offer
 */
public class Withdraw extends Intention {
    private static Withdraw instance;

    private static final double FLUCTUATION_WEIGHT = 0.50;
    private static final double INVESTMENT_WEIGHT = 0.30;
    private static final double PRECISION_WEIGHT = 0.20;

    public static Withdraw getInstance() {
        if(instance == null)
            instance = new Withdraw();
        return instance;
    }

    private Withdraw() {

    }

    public void calculateWeight() {
        weight = 0;

        Player player = Player.getInstance();

        Company currCompany;
        double currWeight = 0, finalMoneyRatio, investmentMoneyRatio, currMoneyRatio;
        CompanyInformation currCompanyInfo;
        double roundBalance = MakeOffer.getInstance(player).getRoundBalance();
        for(Map.Entry<String, CompanyInformation> entry : player.getCompanyBeliefs().entrySet()) {
            currCompany = player.getCompany(entry.getKey());
            currCompanyInfo = entry.getValue();
            if(currCompanyInfo.getCurrentOffer() == null)
                continue;

            finalMoneyRatio = (roundBalance + currCompanyInfo.getCurrentOffer().getOfferedValue()) / player.getBalance();
            currMoneyRatio = roundBalance / player.getBalance();
            investmentMoneyRatio = currCompanyInfo.getBelievedValue() / player.getBalance();

            // Check ratio
            switch (player.getStyle()){
                case LOW_RISK:
                    if(currMoneyRatio >= 0.25)
                        continue;
                    currWeight = FLUCTUATION_WEIGHT * currCompanyInfo.getBelievedFluctuation();
                    currWeight += INVESTMENT_WEIGHT * investmentMoneyRatio;
                    currWeight += PRECISION_WEIGHT * (1 - Math.sqrt(finalMoneyRatio - 0.25));
                    break;
                case HIGH_RISK:
                    if(currMoneyRatio >= 0.10)
                        continue;
                    currWeight = FLUCTUATION_WEIGHT * (1 - currCompanyInfo.getBelievedFluctuation());
                    currWeight += INVESTMENT_WEIGHT * (1 - investmentMoneyRatio);
                    currWeight += PRECISION_WEIGHT * (1 - Math.sqrt(finalMoneyRatio - 0.10));
                    break;
                case RANDOM:
                    currWeight = Math.random();
                    break;
            }

            if(currWeight > weight) {
                weight = currWeight;
                company = currCompany;
            }
        }
    }
}