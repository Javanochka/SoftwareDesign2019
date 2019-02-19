package ru.hse.nikiforovskaya.commands;

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
     * @param arguments is a String array of arguments to pass to the command
     * @param input     is an input stream to pass to the command
     * @param output    is an output stream to pass to the command
     */
    public Ls(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    @Override
    public void process() throws CommandException {
        if (arguments != null && arguments.length > 1) {
            throw new InvalidArgumentException("Ls takes one or no arguments");
        }
        Path path;
        if (arguments != null && arguments.length == 1) {
            Path p = Paths.get(arguments[0]);
            if (p.isAbsolute()) {
                path = p;
            } else {
                path = Paths.get(System.getProperty("user.dir"), arguments[0]);
            }
        } else {
            path = Paths.get(System.getProperty("user.dir"));
        }
        try {
            String res = Files.list(path).map(Path::normalize)
                    .map(x -> {
                        String relativeName = x.getName(x.getNameCount() - 1).toString();
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



