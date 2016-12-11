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

    private static final double FLUCTUATION_WEIGHT = 0.6;
    private static final double INVESTMENT_WEIGHT = 0.4;

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
                    currWeight += INVESTMENT_WEIGHT * (1 - investmentMoneyRatio);
                    break;
                case HIGH_RISK:
                    if(finalMoneyRatio < 0.05)
                        continue;
                    currWeight = FLUCTUATION_WEIGHT * currCompanyInfo.getBelievedFluctuation();
                    currWeight += INVESTMENT_WEIGHT * investmentMoneyRatio;
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
