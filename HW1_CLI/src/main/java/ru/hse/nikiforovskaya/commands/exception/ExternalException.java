package ru.hse.nikiforovskaya.commands.exception;

/** ExternalException is thrown when something has occurred during running external commands */
public class ExternalException extends CommandException {

    /**
     * Creates an ExternalException with message and cause
     *
     * @param what  a message to the user
     */
    public ExternalException(String what) {
        super(what, null);
    }
}
