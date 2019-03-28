package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.Interpreter;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.InvalidArgumentException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Ls extends Command {
    /**
     * Creates a new Command instance
     *
     * @param arguments   is a String array of arguments to pass to the command
     * @param input       is an input stream to pass to the command
     * @param output      is an output stream to pass to the command
     * @param interpreter is an interpreter which executes this command
     */
    public Ls(String[] arguments, InputStream input, OutputStream output, Interpreter interpreter) {
        super(arguments, input, output, interpreter);
    }

    @Override
    public void process() throws CommandException {
        if (arguments != null && arguments.length > 1) {
            throw new InvalidArgumentException("Ls takes one or no arguments");
        }
        Path path;
        if (arguments != null && arguments.length == 1) {
            path = getPath(arguments[0]);
        } else {
            path = getPath(".");
        }
        try {
            String res = Files.list(path).map(Path::normalize)
                    .map(x -> {
                        String relativeName = x.getFileName().toString();
                        if (Files.isDirectory(x)) {
                            return relativeName + File.separator;
                        } else {
                            return relativeName;
                        }
                    }).collect(
                            Collectors.joining(System.lineSeparator(), "", System.lineSeparator())
                    );
            output.write(res.getBytes());
            output.flush();
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        }
    }
}



