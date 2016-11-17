package aiad.feup.messages.exceptions;

/**
 * Balance exception
 */
public class DuplicatedItemException extends RuntimeException {

    /**
     * Constructor of {@link DuplicatedItemException}
     * Is thrown when an item is added to a list where it already exists
     * @param message error message
     */
    public DuplicatedItemException(final String message) {
        super(message);
    }
}