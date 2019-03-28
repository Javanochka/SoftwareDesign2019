package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.Interpreter;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.InvalidArgumentException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeDir extends Command {
    /**
     * Creates a new Command instance
     *
     * @param arguments   is a String array of arguments to pass to the command
     * @param input       is an input stream to pass to the command
     * @param output      is an output stream to pass to the command
     * @param interpreter is an interpreter which executes this command
     */
    public ChangeDir(String[] arguments, InputStream input, OutputStream
            output, Interpreter interpreter) {
        super(arguments, input, output, interpreter);
    }

    @Override
    public void process() throws CommandException {
        if (arguments != null && arguments.length > 1) {
            throw new InvalidArgumentException("Ls takes one or no arguments");
        }
        Path path = Paths.get(System.getProperty("user.home"));
        if (arguments != null && arguments.length == 1) {
            path = getPath(arguments[0]);
        }
        if (!Files.isDirectory(path)) {
            throw new InvalidArgumentException("The specified path doesn't " +
                    "exist");
        }
        interpreter.setCurrentDirectory(path.normalize().toString());
    }
}
