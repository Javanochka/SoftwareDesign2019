package ru.hse.nikiforovskaya.commands;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Exit stops interpreter execution.
 * Does not use arguments or any of streams.
 */
public class Exit extends Command {

    private static boolean SHOULD_EXIT = false;

    /**
     * shouldExit returns true if in the last session exit was called.
     * @return {@code true} if exit was called, {@code false} otherwise.
     */
    public static boolean shouldExit() {
        return SHOULD_EXIT;
    }

    /**
     * Creates a new Exit instance
     * @param arguments is a String array of arguments to pass to the command. Never in use
     * @param input is an input stream to pass to the command. Never in use
     * @param output is an output stream to pass to the command. Never in use
     */
    public Exit(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    /** Runs the command. */
    @Override
    public void process() {
        SHOULD_EXIT = true;
    }
}
