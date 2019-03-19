package ru.hse.nikiforovskaya.commands.exception;

/** NoInputException is thrown when a command expected an input stream, but got null. */
public class NoInputException extends  CommandException {
    /**
     * Creates a new NoInputException
     * @param where gives more details on where the problem happened.
     */
    public NoInputException(String where) {
        super("Command " + where + " needs input", null);
    }
}
