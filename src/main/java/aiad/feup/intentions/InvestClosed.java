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
    private static double INVESTMENT_WEIGHT;
    private static double TIME_LEFT_WEIGHT;
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

        double timeElapsedRatio = ((System.currentTimeMillis() - player.getRoundStartTime()) / 1000) / player.getRoundDuration();

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
                    FLUCTUATION_WEIGHT = 0.30;
                    INVESTMENT_WEIGHT = 0.25;
                    TIME_LEFT_WEIGHT = 0.30;
                    ALREADY_INVESTED_WEIGHT = 0.15;
                    currWeight = FLUCTUATION_WEIGHT * (1 - currCompanyInfo.getBelievedFluctuation());
                    currWeight += INVESTMENT_WEIGHT * (1 - investmentMoneyRatio);
                    if(currCompanyInfo.getCurrentOffer() != null)
                        currWeight += ALREADY_INVESTED_WEIGHT;
                    currWeight += TIME_LEFT_WEIGHT * timeElapsedRatio;
                    break;
                case HIGH_RISK:
                    if(finalMoneyRatio < 0.05)
                        continue;
                    FLUCTUATION_WEIGHT = 0.40;
                    INVESTMENT_WEIGHT = 0.30;
                    TIME_LEFT_WEIGHT = 0.25;
                    ALREADY_INVESTED_WEIGHT = 0.05;
                    currWeight = FLUCTUATION_WEIGHT * currCompanyInfo.getBelievedFluctuation();
                    currWeight += INVESTMENT_WEIGHT * investmentMoneyRatio;
                    if(currCompanyInfo.getCurrentOffer() != null)
                        currWeight += ALREADY_INVESTED_WEIGHT;
                    currWeight += TIME_LEFT_WEIGHT * timeElapsedRatio;
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
