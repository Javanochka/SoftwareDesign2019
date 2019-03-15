package ru.hse.nikiforovskaya.commands.exception;

/** ArgumentException is thrown if something wrong with the arguments passed to internal commands.*/
public class ArgumentException extends CommandException {
    /**
     * Creates a ArgumentException with message and cause
     *
     * @param what  a message to the user
     * @param cause a cause of the exception
     */
    public ArgumentException(String what, Throwable cause) {
        super(what, cause);
    }
}
