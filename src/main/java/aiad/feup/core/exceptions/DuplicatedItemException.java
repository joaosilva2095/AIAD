package aiad.feup.core.exceptions;

/**
 * Balance exception
 */
public class DuplicatedItemException extends Exception {

    /**
     * Constructor of {@link DuplicatedItemException}
     * Is thrown when an item is added to a list where it already exists
     * @param message error message
     */
    public DuplicatedItemException(final String message) {
        super(message);
    }
}