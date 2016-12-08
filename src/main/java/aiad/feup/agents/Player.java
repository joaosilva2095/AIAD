package aiad.feup.agents;

import aiad.feup.behaviours.player.WaitJoinConfirmation;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messages.EndGame;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import aiad.feup.models.PlayerType;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The player super class.
 * Holds information common to all players
 */
public class Player extends GameAgent {

    /**
     * Instance of the player
     */
    private static Player instance;

    /**
     * Balance of the player
     */
    private double balance;

    /**
     * Type of the player
     */
    private PlayerType type = PlayerType.NONE;

    /**
     * List with all companies
     */
    private List<Company> companies;

    /**
     * Number of tokens of the player
     */
    private int numberTokens;


    /**
     * List with the beliefs of the company environment.
     */
    private Map<Company, CompanyInformation> companyBeliefs;

    /**
     * Constructor of Player
     */
    private Player() {
        super();
        this.balance = 0;
        this.companies = new ArrayList<>();
        this.numberTokens = 0;
        this.companyBeliefs = new HashMap<>();
    }

    /**
     * Get the instance of the player
     * @return instance of the player
     */
    public static Player getInstance() {
        if(instance == null)
            instance = new Player();
        return instance;
    }

    /**
     * Setup the player
     */
    @Override
    protected void setup() {
        super.setup();

        // Create the behaviour for receiving messages
        addBehaviour(getFactory().wrap(WaitJoinConfirmation.getInstance(this)));

        // Register in the DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        try {
            DFService.register(this, dfd);
            setGameState(GameState.WAITING_JOIN_CONFIRMATION);
        } catch (FIPAException e) {
            System.out.println("Could not register in the DF! " + e.getMessage());
            System.exit(1);
        }
    }


    /**
     * Take down method, takes down the agent
     */
    public void takeDown(){
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the player agent
     * @param nickname nickname of the player
     * @param mainHost host of the remote container
     * @param mainPort port of the remote container
     * @param localHost local host
     * @param localPort local port
     */
    public void init(final String nickname, final String mainHost, final int mainPort, final String localHost, final int localPort) throws StaleProxyException {
        Runtime rt = Runtime.instance();

        // Create a default profile
        Profile profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, mainHost);
        profile.setParameter(ProfileImpl.MAIN_PORT, "" + mainPort);
        profile.setParameter(ProfileImpl.LOCAL_HOST, localHost);
        profile.setParameter(ProfileImpl.LOCAL_PORT, "" + localPort);
        profile.setParameter(ProfileImpl.MAIN, "false");

        AgentContainer agentContainer = rt.createAgentContainer(profile);
        AgentController playerController = agentContainer.acceptNewAgent(nickname, this);
        playerController.start();
    }

    /**
     * Get the current balance of the player
     * @return current balance of the player
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Get the player type
     * @return player type
     */
    public PlayerType getType() {
        return type;
    }

    /**
     * Set the balance of the player
     * @param balance balance of the player
     */
    public void setBalance(final double balance) { this.balance = balance; }

    /**
     * Set the type of the player
     * @param type type of the player
     */
    public void setType(final PlayerType type) {
        this.type = type;
    }

    /**
     * Set the companies of the player
     * @param companies companies of the player
     */
    public void setCompanies(final List<Company> companies) {
        this.companies = companies;
    }

    /**
     * Add tokens to the player
     * @param numberTokens number of tokens to be added
     */
    public void setTokens(int numberTokens) {
        this.numberTokens = numberTokens;
    }

    /**
     * Remove a token from the player
     */
    public void removeToken() {
        this.removeTokens(1);
    }

    /**
     * Remove tokens to the player
     * @param numberTokens number of tokens to be removed
     */
    public void removeTokens(int numberTokens) {
        if(numberTokens < 0)
            throw new IllegalArgumentException("Number of tokens to be added must be positive");
        this.numberTokens -= numberTokens;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public Map<Company, CompanyInformation> getCompanyBeliefs(){
        return companyBeliefs;
    }

    /**
     * Extracts the content of a message. Returns null if inexistant
     * @param message the message with the content
     * @return the content of the message
     */
    public Object extractMessageContentObject(ACLMessage message) {
        Object content;

        try {
            System.out.println("Extracting content");
            content = message.getContentObject();
            System.out.println("Content extracted");
            if(content == null) {
                System.out.println("Content was null and void and useless");
            }
            System.out.println("Content was useful and not null");
        } catch (Exception e) {
            System.out.println("Could not retrieve message content object. " + e.getMessage());
            return null;
        }

        System.out.println("Returning content");
        return content;
    }

    /**
     * Handles the end game if that was the content
     */
    public void handleEndGame(Object content) {
        // Ending the game
        if(content instanceof EndGame) {
            EndGame endGame = (EndGame) content;
            System.out.println("Ending the game. Reason: " + endGame.getMessage());
            System.exit(0);
            return;
        }
    }

    /**
     * Generates beliefs for the companies
     */
    public void generateCompanyBeliefs(){
        for(Company company : companies){
            if(companyBeliefs.get(company) != null)
                continue;

            companyBeliefs.put(company, new CompanyInformation(company));
        }
    }
}

