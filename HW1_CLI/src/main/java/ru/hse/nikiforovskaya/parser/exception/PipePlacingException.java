package ru.hse.nikiforovskaya.parser.exception;

public class PipePlacingException extends ParserException {
    /**
     * Creates a new PipPlacingException with the given message
     *
     * @param string a string where a problem was found
     */
    public PipePlacingException(String string) {
        super("Pipes are placed wrong in the string \"" + string + "\".");
    }
}
