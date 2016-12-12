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

    private static double FLUCTUATION_WEIGHT;
    private static double INVESTMENT_VOLUME_WEIGHT;
    private static double INVESTMENT_WEIGHT;
    private static double TIME_ELAPSED_WEIGHT;

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
        double roundBalance = MakeOffer.getInstance(player, false).getRoundBalance();
        for(Map.Entry<String, CompanyInformation> entry : player.getCompanyBeliefs().entrySet()) {
            currCompany = player.getCompany(entry.getKey());
            currCompanyInfo = entry.getValue();
            if(currCompany.isClosed())
                continue;
            if(currCompanyInfo.getCurrentOffer() != null)
                continue;

            finalMoneyRatio = (roundBalance - currCompanyInfo.getBelievedValue()) / player.getBalance();
            if(finalMoneyRatio < 0)
                continue;

            investmentMoneyRatio = currCompanyInfo.getBelievedValue() / player.getBalance();
            // Check ratio
            switch (player.getStyle()){
                case LOW_RISK:
                    FLUCTUATION_WEIGHT = 0.24;
                    INVESTMENT_VOLUME_WEIGHT = 0.12;
                    INVESTMENT_WEIGHT = 0.44;
                    TIME_ELAPSED_WEIGHT = 0.2;
                    double timeElapsedRatio = ((System.currentTimeMillis() - player.getRoundStartTime()) / 1000.0) / player.getRoundDuration();
                    if(finalMoneyRatio < 0.15)
                        continue;
                    currWeight = FLUCTUATION_WEIGHT * (1 - currCompanyInfo.getBelievedFluctuation());
                    currWeight += INVESTMENT_VOLUME_WEIGHT * (1 - investmentMoneyRatio);
                    currWeight += INVESTMENT_WEIGHT * calculateInvestmentWeight(currCompanyInfo);
                    currWeight += TIME_ELAPSED_WEIGHT * (1 - timeElapsedRatio);
                    break;
                case HIGH_RISK:
                    FLUCTUATION_WEIGHT = 0.30;
                    INVESTMENT_VOLUME_WEIGHT = 0.15;
                    INVESTMENT_WEIGHT = 0.55;
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