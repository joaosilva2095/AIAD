package aiad.feup.behaviours.player.investor;
import aiad.feup.agents.Player;
import aiad.feup.models.GameState;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Receive Message Behaviour. Continuously listens for messages from other agents
 */
public class ReceiveMessage extends SimpleBehaviour {

    private static ReceiveMessage instance;

    private ReceiveMessage(Player player) { super(player); }

    public static ReceiveMessage getInstance(Player player) {
        if(instance == null)
            instance = new ReceiveMessage(player);
        return instance;
    }

    @Override
    public void action() {
        System.out.println("Receiving messages");
        ACLMessage message = getAgent().blockingReceive();
        Player player = (Player)getAgent();

        System.out.println("Received a message");
        Object content = player.extractMessageContentObject(message);
        player.handleEndGame(content);

        // Received standard ACL message
        if(message.getPerformative() == ACLMessage.ACCEPT_PROPOSAL)
            System.out.println("Hooray, offer accepted");
        else if (message.getPerformative() == ACLMessage.REJECT_PROPOSAL)
            System.out.println("Damnit.");
    }

    @Override
    public boolean done() {
        return ((Player)getAgent()).getGameState() != GameState.START_AUCTION;
    }
}

