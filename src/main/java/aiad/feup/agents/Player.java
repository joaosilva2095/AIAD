package aiad.feup.agents;

import aiad.feup.behaviours.player.WaitJoinConfirmation;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.messageObjects.EndGame;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import aiad.feup.models.PlayerType;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.text.DecimalFormat;
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
     * The board remote agent to communicate with
     */
    private RemoteAgent board;

    /**
     * Balance of the player
     */
    private double balance;

    /**
     * Balance for the current round
     */
    private double roundBalance;

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
     * The total duration of the current round
     */
    private int roundDuration;

    /**
     * The moment in time where the round started
     */
    private long roundStartTime;


    /**
     * List with the beliefs of the company environment.
     */
    private Map<String, CompanyInformation> companyBeliefs;

    /**
     * Constructor of Player
     */
    private Player() {
        super();
        this.balance = 0;
        this.roundBalance = 0;
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

        // Create the behaviour for receiving messageObjects
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
     * Handles the end game if that was the content
     */
    public void handleEndGame(Object content) {
        // Ending the game
        if(content instanceof EndGame) {
            EndGame endGame = (EndGame) content;
            killAgent("Ending the game. Reason: " + endGame.getMessage(), 0);
            return;
        }
    }

    /**
     * Generates beliefs for the companies
     */
    public void generateCompanyBeliefs(){
        for(Company company : companies){
            if(companyBeliefs.containsKey(company.getName()))
                continue;

            companyBeliefs.put(company.getName(), new CompanyInformation(company));
        }
    }

    public double getBalance() {
        return balance;
    }

    public synchronized double getRoundBalance() {
        return roundBalance;
    }

    public PlayerType getType() {
        return type;
    }

    public void setBalance(final double balance) {
        this.balance = balance;
        this.roundBalance = balance;
        DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println("Wallet: " + df.format(balance) + "€");
    }

    public void setType(final PlayerType type) {
        this.type = type;
    }

    public void setCompanies(final List<Company> companies) {
        this.companies = companies;
        System.out.println("Number companies: " + companies.size());
    }

    public int getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(int roundDuration) {
        this.roundDuration = roundDuration;
    }

    public long getRoundStartTime() {
        return roundStartTime;
    }

    public void setRoundStartTime(long roundStartTime) {
        this.roundStartTime = roundStartTime;
    }

    public void setTokens(int numberTokens) {
        this.numberTokens = numberTokens;
    }

    public synchronized void setRoundBalance(double roundBalance) {
        this.roundBalance = roundBalance;
    }

    public void removeToken() {
        this.removeTokens(1);
    }

    public void removeTokens(int numberTokens) {
        if(numberTokens < 0)
            throw new IllegalArgumentException("Number of tokens to be added must be positive");
        this.numberTokens -= numberTokens;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public Map<String, CompanyInformation> getCompanyBeliefs(){
        return companyBeliefs;
    }


    public RemoteAgent getBoard() { return board; }

    public void setBoard(RemoteAgent board) { this.board = board; }
}

