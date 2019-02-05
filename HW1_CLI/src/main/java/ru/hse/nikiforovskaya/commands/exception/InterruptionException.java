package ru.hse.nikiforovskaya.commands.exception;

/** InterruptionException is thrown when a command was unexpectedly interrupted. */
public class InterruptionException extends CommandException {
    /**
     * Creates an InterruptionException with the cause.
     * @param cause why an exception occurred
     */
    public InterruptionException(Throwable cause) {
        super("Caught an interrupt during command running.", cause);
    }
}
