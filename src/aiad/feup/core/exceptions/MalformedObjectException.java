package aiad.feup.core.exceptions;

/**
 * Balance exception
 */
public class MalformedObjectException extends Exception {

    /**
     * Constructor of {@link MalformedObjectException}
     * @param message error message
     */
    public MalformedObjectException(final String message) {
        super(message);
    }
}
