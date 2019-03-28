package ru.hse.nikiforovskaya.commands;

import com.sun.istack.internal.NotNull;
import ru.hse.nikiforovskaya.Interpreter;
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
    protected Interpreter interpreter;

    /**
     * Creates a new Command instance
     *
     * @param arguments   is a String array of arguments to pass to the command
     * @param input       is an input stream to pass to the command
     * @param output      is an output stream to pass to the command
     * @param interpreter is an interpreter which executes this command
     */
    public Command(String[] arguments, InputStream input, OutputStream
            output, Interpreter interpreter) {
        this.arguments = arguments;
        this.input = input;
        this.output = output;
        this.interpreter = interpreter;
    }

    /**
     * Runs current command. Doesn't close any of the streams.
     *
     * @throws CommandException if any exception has happened during the command running
     */
    public abstract void process() throws CommandException;

    /**
     * @param filename is name of file we want to process
     * @return if a file name represents an absolute path, it wil be returned,
     * otherwise returns path relative to the current working directory
     */
    protected Path getPath(@NotNull String filename) {
        Path path = Paths.get(filename);
        if (path.isAbsolute()) {
            return path;
        }
        return Paths.get(interpreter.getCurrentDirectory(), filename);
    }
}
