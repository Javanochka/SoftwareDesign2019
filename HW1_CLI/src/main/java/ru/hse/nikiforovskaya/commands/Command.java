package ru.hse.nikiforovskaya.commands;

import com.sun.istack.internal.NotNull;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     *
     * @param arguments is a String array of arguments to pass to the command
     * @param input     is an input stream to pass to the command
     * @param output    is an output stream to pass to the command
     */
    public Command(String[] arguments, InputStream input, OutputStream output) {
        this.arguments = arguments;
        this.input = input;
        this.output = output;
    }

    /**
     * Runs current command. Doesn't close any of the streams.
     *
     * @throws CommandException if any exception has happened during the command running
     */
    public abstract void process() throws CommandException;

    static Path getPath(@NotNull String filename) {
        Path path = Paths.get(filename);
        if (path.isAbsolute()) {
            return path;
        }
        return Paths.get(System.getProperty("user.dir"), filename);
    }
}
