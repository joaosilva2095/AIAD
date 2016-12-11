package aiad.feup.intentions;

import aiad.feup.agents.Player;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.models.Company;

import java.util.Map;

/**
 * The desire of a player to close its investments
 */
public class InvestClosed extends Intention {
    private static InvestClosed instance;

    private static double FLUCTUATION_WEIGHT;
    private static double INVESTMENT_VOLUME_WEIGHT;
    private static double INVESTMENT_WEIGHT = 0.55;
    private static double TIME_ELAPSED_WEIGHT;
    private static double ALREADY_INVESTED_WEIGHT;

    public static InvestClosed getInstance() {
        if(instance == null)
            instance = new InvestClosed();
        return instance;
    }

    private InvestClosed() {
    }

    public void calculateWeight() {
        weight = 0;

        Player player = Player.getInstance();

        double timeElapsedRatio = ((System.currentTimeMillis() - player.getRoundStartTime()) / 1000.0) / player.getRoundDuration();

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
                    FLUCTUATION_WEIGHT = 0.135;
                    INVESTMENT_VOLUME_WEIGHT = 0.425;
                    TIME_ELAPSED_WEIGHT = 0.135;
                    ALREADY_INVESTED_WEIGHT = 0.1375;
                    currWeight = FLUCTUATION_WEIGHT * (1 - currCompanyInfo.getBelievedFluctuation());
                    currWeight += INVESTMENT_VOLUME_WEIGHT * (1 - investmentMoneyRatio);
                    if(currCompanyInfo.getCurrentOffer() != null)
                        currWeight += ALREADY_INVESTED_WEIGHT;
                    currWeight += TIME_ELAPSED_WEIGHT * timeElapsedRatio;
                    currWeight += INVESTMENT_WEIGHT * calculateInvestmentWeight(currCompanyInfo);
                    break;
                case HIGH_RISK:
                    if(finalMoneyRatio < 0.05)
                        continue;
                    FLUCTUATION_WEIGHT = 0.18;
                    INVESTMENT_VOLUME_WEIGHT = 0.135;
                    TIME_ELAPSED_WEIGHT = 0.1125;
                    ALREADY_INVESTED_WEIGHT = 0.0225;
                    currWeight = FLUCTUATION_WEIGHT * currCompanyInfo.getBelievedFluctuation();
                    currWeight += INVESTMENT_VOLUME_WEIGHT * investmentMoneyRatio;
                    if(currCompanyInfo.getCurrentOffer() != null)
                        currWeight += ALREADY_INVESTED_WEIGHT;
                    currWeight += TIME_ELAPSED_WEIGHT * timeElapsedRatio;
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