package aiad.feup.intentions;

import aiad.feup.agents.Player;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.models.Company;

import java.util.Map;

/**
 * The desire of a player to invest
 */
public class InvestOpen extends Intention {
    private static InvestOpen instance;

    private static final double FLUCTUATION_WEIGHT = 0.30;
    private static final double INVESTMENT_VOLUME_WEIGHT = 0.15;
    private static final double INVESTMENT_WEIGHT = 0.55;

    public static InvestOpen getInstance() {
        if(instance == null)
            instance = new InvestOpen();
        return instance;
    }

    private InvestOpen() {
    }

    public void calculateWeight() {
        weight = 0;

        Player player = Player.getInstance();

        Company currCompany;
        double currWeight = 0, finalMoneyRatio, investmentMoneyRatio;
        CompanyInformation currCompanyInfo;
        double roundBalance = MakeOffer.getInstance(player).getRoundBalance();
        for(Map.Entry<String, CompanyInformation> entry : player.getCompanyBeliefs().entrySet()) {
            currCompany = player.getCompany(entry.getKey());
            if(currCompany.isClosed())
                continue;

            currCompanyInfo = entry.getValue();

            finalMoneyRatio = (roundBalance - currCompanyInfo.getBelievedValue()) / player.getBalance();
            if(finalMoneyRatio < 0)
                continue;

            investmentMoneyRatio = currCompanyInfo.getBelievedValue() / player.getBalance();
            // Check ratio
            switch (player.getStyle()){
                case LOW_RISK:
                    if(finalMoneyRatio < 0.15)
                        continue;
                    currWeight = FLUCTUATION_WEIGHT * (1 - currCompanyInfo.getBelievedFluctuation());
                    currWeight += INVESTMENT_VOLUME_WEIGHT * (1 - investmentMoneyRatio);
                    currWeight += INVESTMENT_WEIGHT * calculateInvestmentWeight(currCompanyInfo);
                    break;
                case HIGH_RISK:
                    if(finalMoneyRatio < 0.05)
                        continue;
                    currWeight = FLUCTUATION_WEIGHT * currCompanyInfo.getBelievedFluctuation();
                    currWeight += INVESTMENT_VOLUME_WEIGHT * investmentMoneyRatio;
                    currWeight += INVESTMENT_WEIGHT * calculateInvestmentWeight(currCompanyInfo);
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

    /**
     * Calculate the weight of an investment. As the believed value approaches the maximum believed value for a company after fluctuation
     * the investment weight is reduced quadratically.
     * @param companyInformation company information
     * @return investment weight
     */
    private double calculateInvestmentWeight(CompanyInformation companyInformation){
        double distance = companyInformation.getMaximumBelievedValue() - companyInformation.getMinimumBelievedValue();
        double ratio = (companyInformation.getMaximumBelievedValue() - companyInformation.getBelievedValue()) / distance;
        return Math.pow(ratio, 2);
    }
}