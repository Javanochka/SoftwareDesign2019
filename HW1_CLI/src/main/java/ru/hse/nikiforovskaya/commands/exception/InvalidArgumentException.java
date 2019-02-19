package ru.hse.nikiforovskaya.commands.exception;

public class InvalidArgumentException extends CommandException{

    /**
     * Creates a CommandException with message and cause
     *
     * @param what  a message to the user
     */
    public InvalidArgumentException(String what) {
        super(what, null);
    }
}
