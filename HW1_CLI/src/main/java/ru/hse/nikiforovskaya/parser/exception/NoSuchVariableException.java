package ru.hse.nikiforovskaya.parser.exception;

/** NoSuchVariableException is thrown when there's no variable with given name. */
public class NoSuchVariableException extends ParserException {
    /**
     * Creates a new NoSuchVariableException
     * @param name a name which was not found
     */
    public NoSuchVariableException(String name) {
        super("There is no variable " + name + " found.");
    }
}
