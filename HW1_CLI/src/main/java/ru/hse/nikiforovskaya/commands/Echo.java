package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;

import java.io.*;

/**
 * Echo is a command which prints its arguments to the output stream.
 * Always adds a newline.
 * Never uses input stream.
 */
public class Echo extends Command {

    /**
     * Creates a new Echo instance
     * @param arguments is a String array of arguments to pass to the command
     * @param input is an input stream to pass to the command. Will never be used.
     * @param output is an output stream to pass to the command
     */
    public Echo(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    /**
     * Runs command.
     * @throws CommandException if problem with output occurred.
     */
    @Override
    public void process() throws CommandException {
        try {
            String result = String.join(" ", arguments);
            result = result + System.lineSeparator();
            output.write(result.getBytes());
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        }
    }
}
