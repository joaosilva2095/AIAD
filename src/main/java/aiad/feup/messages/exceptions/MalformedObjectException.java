package aiad.feup.messages.exceptions;

/**
 * Balance exception
 */
public class MalformedObjectException extends RuntimeException {

    /**
     * Constructor of {@link MalformedObjectException}
     * @param message error message
     */
    public MalformedObjectException(final String message) {
        super(message);
    }
}
