package aiad.feup.models;

/**
 * Possible states for a company
 */
public enum CompanyState {

    /**
     * The offer for the company was outbid
     */
    OUTBID,

    /**
     * A player withdraw the company
     */
    WITHDRAW,

    /**
     * Company is available
     */
    AVAILABLE,

    /**
     * Company has made a closed deal
     */
    CLOSED
}
