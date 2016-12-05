package aiad.feup.agents;

import java.io.Serializable;

/**
 * Remote agent
 */
public class RemoteAgent implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 456599173322220545L;

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
