package aiad.feup.agents;

import aiad.feup.behaviours.board.CheckGameIntegrity;
import aiad.feup.behaviours.board.ReadCommand;
import aiad.feup.behaviours.board.WaitForPlayers;
import aiad.feup.beliefs.CompanyInformation;
import aiad.feup.exceptions.DuplicatedItemException;
import aiad.feup.messageObjects.Offer;
import aiad.feup.messageObjects.RoundInformation;
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

import java.rmi.Remote;
import java.text.DecimalFormat;
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
    private static final double INITIAL_BALANCE = 120;

    /**
     * Initial tokens for investors
     */
    private static final int INITIAL_TOKENS = 3;

    /**
     * The base value of a company to use for generating a company
     */
    private static final double BASE_VALUE = 30;

    /**
     * Fee that a manager has to pay for a company at the end of the round
     */
    private static final double COMPANY_FEE = 10;

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
     * List with all the player companies
     */
    private Map<String, List<Company>> playerCompanies;

    /**
     * Balances of the remote agents
     */
    private Map<String, Double> balances;

    /**
     * Tokens of the remote agents
     */
    private Map<String, Integer> tokens;

    /**
     * Type of the players
     */
    private Map<String, PlayerType> types;

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

        this.playerCompanies = new HashMap<>();
        this.balances = new HashMap<>();
        this.tokens = new HashMap<>();
        this.types = new HashMap<>();
        this.currentRoundNumber = 1;


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
    public void applyEndOfRoundFluctuation() {
        for (Company company : companies) {
            if (company.getValue() <= 0) {
                continue;
            }

            company.applyFluctuation();
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
        this.playerCompanies.put(player.getName(), new ArrayList<>());
        this.balances.put(player.getName(), INITIAL_BALANCE);
        this.tokens.put(player.getName(), INITIAL_TOKENS);
    }

    /**
     * Get all the companies in the game
     *
     * @return all companies in the game
     */
    public List<Company> getCompanies() {
        return companies;
    }

    public Company getCompany(String name) {
        for(Company company : companies)
            if(company.getName().equalsIgnoreCase(name))
                return company;
        return null;
    }

    public double getBalance(String name) {
        return balances.get(name);
    }

    public PlayerType getType(String name) {
        return types.get(name);
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
                types.put(agent.getName(), PlayerType.INVESTOR);
            } else { // Manager
                sendMessage(agent, new ACLMessage(ACLMessage.INFORM), new SetupPlayer(PlayerType.MANAGER, ROUND_DURATION));
                types.put(agent.getName(), PlayerType.MANAGER);
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
     * Assign companies to players
     */
    public void assignCompanies() {
        List<Company> newCompanies;
        if(currentRoundNumber == 1) {
            newCompanies = generateRandomCompanies(getNumberManagers() * 3); //We multiply by 3 because every manager must have 3 companies at the start. there will be surplus companies
        } else {
            newCompanies = generateRandomCompanies(getNumberManagers());
        }
        companies.addAll(newCompanies);

        Queue<Company> undistributedCompanies = new LinkedList<>(newCompanies);
        Company company;

        // Assign companies to managers
        int companiesPerPlayer = undistributedCompanies.size() / getNumberManagers();
        for(RemoteAgent targetPlayer : players){
            if(types.get(targetPlayer.getName()) != PlayerType.MANAGER)
                continue;

            List<Company> ownedCompanies = playerCompanies.get(targetPlayer.getName());
            //Give 3 companies to each manager
            for(int i = 0; i < companiesPerPlayer; i++) {
                undistributedCompanies.peek().setOwner(targetPlayer);

                company = undistributedCompanies.remove();
                ownedCompanies.add(company);
            }

            playerCompanies.put(targetPlayer.getName(), ownedCompanies);
        }

        for(RemoteAgent targetPlayer : players){
            if(types.get(targetPlayer.getName()) != PlayerType.INVESTOR)
                continue;

            playerCompanies.put(targetPlayer.getName(), companies);
        }
    }

    /**
     * Calculates and creates a Map with the correct UpdatePlayer to send to each RemoteAgent for the initial distribution
     * @return the map with the RemoteAgent - UpdatePlayer relation
     */
    public Map<RemoteAgent, UpdatePlayer> calculatePlayerUpdates(){
        Map<RemoteAgent, UpdatePlayer> playerUpdates = new HashMap<>();

        // Assign companies to investors
        for(RemoteAgent targetPlayer : players)
            playerUpdates.put(targetPlayer, new UpdatePlayer(balances.get(targetPlayer.getName()), tokens.get(targetPlayer.getName()), playerCompanies.get(targetPlayer.getName()), getNumberInvestors(), getNumberManagers(), GameState.START_NEGOTIATION));

        return playerUpdates;
    }

    /**
     * Calculate the balances given a map with offers
     * @param offers map with managers and their received offers
     */
    public void calculateBalances(final Map<RemoteAgent, List<Offer>> offers) {
        RemoteAgent investor;
        Company company;
        double managerBalance, investorBalance;

        // Apply revenues
        for(RemoteAgent manager : offers.keySet()) {
            managerBalance = balances.get(manager.getName());
            for(Offer offer : offers.get(manager)) {
                company = getCompany(offer.getCompany().getName());
                if(company == null)
                    throw new IllegalStateException("Unknown company " + offer.getCompany().getName());

                investor = offer.getInvestor();
                investorBalance = balances.get(investor.getName());

                investorBalance += company.isDoubleRevenue() ? 2 * company.getValue() : company.getValue();
                investorBalance -= offer.getOfferedValue();
                managerBalance += offer.getOfferedValue();

                balances.put(investor.getName(), investorBalance);
            }
            balances.put(manager.getName(), managerBalance);
        }

        // Apply fees
        for(RemoteAgent targetAgent : players) {
            if(types.get(targetAgent.getName()) != PlayerType.MANAGER)
                continue;
            managerBalance = balances.get(targetAgent.getName());
            managerBalance -= playerCompanies.get(targetAgent.getName()).size() * COMPANY_FEE;
            balances.put(targetAgent.getName(), managerBalance);
        }
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
        System.out.println("Round #" + currentRoundNumber);
    }

    public void incrementCurrentRound(){
        setCurrentRoundNumber(++currentRoundNumber);
    }



}
