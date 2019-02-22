package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Cat is a command which print the given in arguments files to output one after another.
 * If there are no arguments, just prints input to the output.
 */
public class Cat extends Command {

    /**
     * Creates a new Cat instance
     * @param arguments is a String array of arguments to pass to the command
     * @param input is an input stream to pass to the command
     * @param output is an output stream to pass to the command
     */
    public Cat(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    /**
     * Runs cat command.
     * @throws CommandException if some problem during running command occurred.
     */
    @Override
    public void process() throws CommandException {
        for (String argument : arguments) {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(argument));
                output.write(bytes);
                output.flush();
            } catch (IOException e) {
                throw new ProblemsWithIOException(e);
            }
        }
        if (arguments.length == 0 && input != null) {
            try {
                byte[] buf = new byte[1024];
                while (input.read(buf) != -1) {
                    output.write(buf);
                }
                output.flush();
            } catch (IOException e) {
                throw new ProblemsWithIOException(e);
            }
        }
    }
}
