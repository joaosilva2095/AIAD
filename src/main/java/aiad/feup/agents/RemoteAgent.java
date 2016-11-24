package aiad.feup.agents;

/**
 * Remote agent
 */
public class RemoteAgent {

    /**
     * Name of the remote agent
     */
    private String name;

    /**
     * Constructor of RemoteAgent
     * @param name name of the agent
     */
    public RemoteAgent(final String name) {
        this.name = name;
    }

    /**
     * Get the name of the remote agent
     * @return name of the remote agent
     */
    public String getName() {
        return name;
    }
}
