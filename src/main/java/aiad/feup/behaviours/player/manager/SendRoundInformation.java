package aiad.feup.behaviours.player.manager;

import aiad.feup.agents.Board;
import aiad.feup.agents.Player;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messageObjects.Offer;
import aiad.feup.messageObjects.RoundInformation;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Send Round Information behaviour.
 * Sends the information of the round to the board
 */
public class SendRoundInformation extends OneShotBehaviour{


    private static SendRoundInformation instance;

    private SendRoundInformation(){
    }

    public static SendRoundInformation getInstance(){
        if(instance == null)
            instance = new SendRoundInformation();
        return instance;
    }

    @Override
    public void action() {
        Player player = (Player)getAgent();
        List<Offer> finalOffers = player.getCompanyBeliefs().values().stream().map(CompanyInformation::getCurrentOffer).collect(Collectors.toList());

        RoundInformation info = new RoundInformation(finalOffers);
        RemoteAgent board = player.getBoard();
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);

        player.sendMessage(board, message, info);
    }
}
