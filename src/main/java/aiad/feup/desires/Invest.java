package aiad.feup.desires;

import aiad.feup.agents.Player;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.models.Company;
import sun.plugin.com.event.COMEventHandler;

import java.util.Map;

/**
 * The desire of a player to invest
 */
public class Invest extends Desire{
    private static Invest instance;

    private static final double FLUCTUATION_WEIGHT = 0.6;
    private static final double INVESTMENT_WEIGHT = 0.4;

    public static Invest getInstance() {
        if(instance == null)
            instance = new Invest();
        return instance;
    }

    private Invest() {
        desire = 0;
    }

    public void calculateDesire() {
        desire = 0;

        Player player = Player.getInstance();

        Company currCompany;
        double currDesire = 0, finalMoneyRatio, investmentMoneyRatio;
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
                    if(finalMoneyRatio < 0.25)
                        continue;
                    currDesire = FLUCTUATION_WEIGHT * (1 - currCompanyInfo.getBelievedFluctuation());
                    currDesire += INVESTMENT_WEIGHT * (1 - investmentMoneyRatio);
                    break;
                case HIGH_RISK:
                    currDesire = FLUCTUATION_WEIGHT * currCompanyInfo.getBelievedFluctuation();
                    currDesire += INVESTMENT_WEIGHT * investmentMoneyRatio;
                    break;
                case RANDOM:
                    if(finalMoneyRatio < Math.random())
                        continue;
                    double temp = Math.random();
                    currDesire = temp * currCompanyInfo.getBelievedFluctuation();
                    currDesire += (1 - temp) * investmentMoneyRatio;
                    break;
            }

            if(currDesire > desire) {
                desire = currDesire;
                company = currCompany;
            }
        }
    }
}
