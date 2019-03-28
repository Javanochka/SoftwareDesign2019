package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.Interpreter;
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
     * Creates a new Command instance
     *
     * @param arguments   is a String array of arguments to pass to the command
     * @param input       is an input stream to pass to the command
     * @param output      is an output stream to pass to the command
     * @param interpreter is an interpreter which executes this command
     */
    public Pwd(String[] arguments, InputStream input, OutputStream output, Interpreter interpreter) {
        super(arguments, input, output, interpreter);
    }

    @Override
    public void process() throws CommandException {
        try {
            output.write((interpreter.getCurrentDirectory() + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        }
    }
}
