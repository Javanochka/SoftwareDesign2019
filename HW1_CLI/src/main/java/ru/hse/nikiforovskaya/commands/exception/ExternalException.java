package ru.hse.nikiforovskaya.commands.exception;

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
