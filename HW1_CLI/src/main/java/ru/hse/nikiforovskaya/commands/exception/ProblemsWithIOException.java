package ru.hse.nikiforovskaya.commands.exception;

/** ProblemsWithIOException is thrown when every problem with io occurred. */
public class ProblemsWithIOException extends CommandException{
    /**
     * Creates a new ProblemsWithIOException with the given cause
     * @param cause a throwable which caused the exception
     */
    public ProblemsWithIOException(Throwable cause) {
        super("Problems with input or output occured during command running", cause);
    }
}
