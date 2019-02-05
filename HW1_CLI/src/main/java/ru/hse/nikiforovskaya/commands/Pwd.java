package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Pwd prints the current directory to the output.
 * Never uses input or arguments.
 */
public class Pwd extends Command {

    /**
     * Creates a new Pwd instance
     * @param arguments is a String array of arguments to pass to the command. Never in use
     * @param input is an input stream to pass to the command. Never in use
     * @param output is an output stream to pass to the command
     */
    public Pwd(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    @Override
    public void process() throws CommandException {
        try {
            output.write((System.getProperty("user.dir") + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        }
    }
}
