package aiad.feup.agents;

import aiad.feup.behaviours.board.CheckGameIntegrity;
import aiad.feup.behaviours.board.ReadCommand;
import aiad.feup.behaviours.board.WaitForPlayers;
import aiad.feup.exceptions.DuplicatedItemException;
import aiad.feup.messageObjects.SetupPlayer;
import aiad.feup.messageObjects.UpdatePlayer;
import aiad.feup.models.Company;
import aiad.feup.models.GameState;
import aiad.feup.models.PlayerType;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.*;

/**
 * The Board Agent
 * Responsible for maintaining the board state
 */
public class Board extends GameAgent {

    /**
     * The number of rounds for the game
     */
    public static final int NUMBER_ROUNDS = 3;

    /**
     * Initial balance for players
     */
    private static final double INITIAL_BALANCE = 120000;

    /**
     * Initial tokens for investors
     */
    private static final int INITIAL_TOKENS = 3;

    /**
     * The base value of a company to use for generating a company
     */
    private static final double BASE_VALUE = 30000;

    /**
     * The base duration of a round in seconds
     */
    public static final int ROUND_DURATION = 10;

    /**
     * The factory for wrapping behaviours in new threads
     */
    private ThreadedBehaviourFactory factory;

    /**
     * Instance of the board
     */
    private static Board instance;

    /**
     * Players of the game
     */
    private List<RemoteAgent> players;

    /**
     * Companies of the game
     */
    private List<Company> companies;

    /**
     * Balances of the remote agents
     */
    private Map<RemoteAgent, Integer> balances;

    /**
     * Tokens of the remote agents
     */
    private Map<RemoteAgent, Integer> tokens;

    /**
     * Type of the players
     */
    private Map<RemoteAgent, PlayerType> types;

    /**
     * The number of the current round
     */
    private int currentRoundNumber;


    /**
     * Constructor of Board
     */
    private Board() {
        super();
        this.players = new ArrayList<>();
        this.companies = new ArrayList<>();
        this.factory = new ThreadedBehaviourFactory();

        this.balances = new HashMap<>();
        this.tokens = new HashMap<>();
        this.types = new HashMap<>();
        this.currentRoundNumber = 0;


        setGameState(GameState.WAITING_GAME_START);
    }

    /**
     * Get the instance of the board
     * @return instance of the board
     */
    public static Board getInstance() {
        if(instance == null)
            instance = new Board();
        return instance;
    }

    /**
     * Setup the board
     */
    @Override
    protected void setup() {
        super.setup();

        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(10L);
        DFAgentDescription dfd = new DFAgentDescription();
        addBehaviour(factory.wrap(CheckGameIntegrity.getInstance(this)));
        addBehaviour(factory.wrap(WaitForPlayers.getInstance(this, dfd, sc)));
        addBehaviour(factory.wrap(ReadCommand.getInstance(this)));


    }

    /**
     * Initialize the board
     * @param host hostname of the DFS
     * @param port port of the DFS
     */
    public void init(final String host, final int port) throws StaleProxyException {
        Runtime rt = Runtime.instance();

        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);

        // Create a default profile
        Profile profile = new ProfileImpl(host, port, null);

        AgentContainer mainContainer = rt.createMainContainer(profile);
        AgentController rma = mainContainer.createNewAgent("rma","jade.tools.rma.rma", new Object[0]);
        rma.start();

        // Add board agent
        AgentController boardController = mainContainer.acceptNewAgent("board", instance);
        boardController.start();
    }

    /**
     * Add a company to the board
     *
     * @param company company to be added
     */
    private void addCompany(Company company) {
        if (companies.contains(company))
            throw new DuplicatedItemException("Company " + company.getName() + " is already registered on the board.");
        companies.add(company);
    }

    /**
     * AdRemoved a company from the board
     *
     * @param company company to be removed
     */
    private void removeCompany(Company company) {
        companies.remove(company);
    }

    /**
     * Applies the end of round fluctuation for all companies, bringing drastic change to the market
     */
    private void applyEndOfRoundFluctuation() {
        for (Company company : companies) {
            if (company.getValue() <= 0) {
                continue;
            }

            System.out.println("Company " + company.getName() + " value before fluctuation: " + company.getValue());
            company.applyFluctuation();
            System.out.println("Company " + company.getName() + " value after fluctuation: " + company.getValue());
        }
    }

    /**
     * Get the number of players in the game
     *
     * @return number of players in the game
     */
    public int getNumberPlayers() {
        return players.size();
    }

    /**
     * Get all the players in the game
     *
     * @return all players in the game
     */
    public List<RemoteAgent> getPlayers() {
        return players;
    }

    /**
     * Get a player by its name
     * @param name name of the player to get
     * @return player with that name
     */
    public RemoteAgent getPlayer(final String name) {
        for (RemoteAgent player : players) {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    /**
     * Add a player to the game
     * @param player player to be added
     */
    public void addPlayer(RemoteAgent player) {
        this.players.add(player);
    }

    /**
     * Get all the companies in the game
     *
     * @return all companies in the game
     */
    public List<Company> getCompanies() {
        return companies;
    }

    /**
     * Assign roles and send to the agents
     */
    public void setupPlayers() {
        int index = 0;
        Collections.shuffle(players);
        for(RemoteAgent agent : players) {
            if(index % 2 == 0) { // Investor
                sendMessage(agent, new ACLMessage(ACLMessage.INFORM), new SetupPlayer(PlayerType.INVESTOR, ROUND_DURATION));
                types.put(agent, PlayerType.INVESTOR);
            } else { // Manager
                sendMessage(agent, new ACLMessage(ACLMessage.INFORM), new SetupPlayer(PlayerType.MANAGER, ROUND_DURATION));
                types.put(agent, PlayerType.MANAGER);
            }
            index++;
        }
    }

    /**
     * Generate a number of random companies
     * @param number the number of companies to be generated
     * @return list with generated companies
     */
    public List<Company> generateRandomCompanies(int number){
        List<Company> returnList = new ArrayList<>();
        for(int i = 0; i < number; i++){
            returnList.add(generateRandomCompany());
        }

        return returnList;
    }
    /**
     * Generate a company
     */
    public Company generateRandomCompany() {
        Random random = new Random();

        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String companyName = sb.toString();
        double companyValue = random.nextDouble()*BASE_VALUE + BASE_VALUE/2;

        boolean doubleRevenue = random.nextDouble() > 0.75;
        int minFluctuation = 10;
        int maxFluctuation = 80;
        int fluctuation = random.nextInt(maxFluctuation-minFluctuation) + minFluctuation;


        return new Company(companyName, companyValue, doubleRevenue, fluctuation);
    }

    /**
     * Calculates and creates a Map with the correct UpdatePlayer to send to each RemoteAgent for the initial distribution
     * @return the map with the RemoteAgent - UpdatePlayer relation
     */
    public Map<RemoteAgent, UpdatePlayer> calculatePlayerUpdates(){
        Queue<Company> undistributedCompanies;
        if(currentRoundNumber == 1) {
            companies = generateRandomCompanies(getNumberManagers() * 3); //We multiply by 3 because every manager must have 3 companies at the start. there will be surplus companies
             undistributedCompanies = new LinkedList<>(companies);
        } else {
            undistributedCompanies = new LinkedList<>(generateRandomCompanies(getNumberManagers()));
        }

        ArrayList<Company> assignedCompanies = new ArrayList<>();
        Map<RemoteAgent, UpdatePlayer> playerUpdates = new HashMap<>();
        int numberManagers = getNumberManagers();
        int numberInvestors = getNumberInvestors();


        // Assign companies to managers
        for(RemoteAgent targetPlayer : players){
            if(types.get(targetPlayer) != PlayerType.MANAGER)
                continue;

            List<Company> playerCompanies = new ArrayList<>();
            //Give 3 companies to each manager
            for(int i = 0; i < 3; i++) {
                undistributedCompanies.peek().setOwner(targetPlayer);
                assignedCompanies.add(undistributedCompanies.peek());
                playerCompanies.add(undistributedCompanies.remove());
            }
            playerUpdates.put(targetPlayer, new UpdatePlayer(INITIAL_BALANCE, INITIAL_TOKENS, playerCompanies, numberInvestors, numberManagers, GameState.START_NEGOTIATION));
        }

        // Assign companies to investors
        for(RemoteAgent targetPlayer : players) {
            if (types.get(targetPlayer) != PlayerType.INVESTOR)
                continue;

            playerUpdates.put(targetPlayer, new UpdatePlayer(INITIAL_BALANCE, INITIAL_TOKENS, assignedCompanies, numberInvestors, numberManagers, GameState.START_NEGOTIATION));
        }

        return playerUpdates;
    }

    public int getNumberManagers(){
        return getNumberPlayers() / 2;
    }

    public int getNumberInvestors() {
        return getNumberPlayers() / 2 + getNumberPlayers() % 2;
    }

    public int getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    public void setCurrentRoundNumber(int currentRoundNumber) {
        this.currentRoundNumber = currentRoundNumber;
    }

    public void incrementCurrentRound(){
        this.currentRoundNumber++;
    }



}
