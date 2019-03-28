package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.Interpreter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Exit stops interpreter execution.
 * Does not use arguments or any of streams.
 */
public class Exit extends Command {

    /**
     * Creates a new Command instance
     *
     * @param arguments   is a String array of arguments to pass to the command
     * @param input       is an input stream to pass to the command
     * @param output      is an output stream to pass to the command
     * @param interpreter is an interpreter which executes this command
     */
    public Exit(String[] arguments, InputStream input, OutputStream output, Interpreter interpreter) {
        super(arguments, input, output, interpreter);
    }

    /**
     * Runs the command.
     */
    @Override
    public void process() {
        System.exit(0);
    }
}
