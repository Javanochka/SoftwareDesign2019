package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.*;

/**
 * Command is an interface for all bash-like commands.
 * It stores input and output streams and number of arguments.
 * Some commands implementing this interface can use or not use every of these things.
 */
public abstract class Command {
    protected String[] arguments;
    protected InputStream input;
    protected OutputStream output;

    /**
     * Creates a new Command instance
     * @param arguments is a String array of arguments to pass to the command
     * @param input is an input stream to pass to the command
     * @param output is an output stream to pass to the command
     */
    public Command(String[] arguments, InputStream input, OutputStream output) {
        this.arguments = arguments;
        this.input = input;
        this.output = output;
    }

    /**
     * Runs current command. Doesn't close any of the streams.
     * @throws CommandException if any exception has happened during the command running
     */
    public abstract void process() throws CommandException;
}
