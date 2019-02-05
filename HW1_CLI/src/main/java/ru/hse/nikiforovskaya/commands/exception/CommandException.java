package ru.hse.nikiforovskaya.commands.exception;

/** CommandException is thrown for every exception occurred during command running. */
public class CommandException extends Exception {
    /**
     * Creates a CommandException with message and cause
     * @param what a message to the user
     * @param cause a cause of the exception
     */
    public CommandException(String what, Throwable cause) {
        super(what, cause);
    }
}
