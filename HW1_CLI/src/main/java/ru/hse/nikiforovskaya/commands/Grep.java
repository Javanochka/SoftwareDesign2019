package ru.hse.nikiforovskaya.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import ru.hse.nikiforovskaya.commands.exception.ArgumentException;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
import com.beust.jcommander.Parameter;
import ru.hse.nikiforovskaya.commands.exception.NoInputException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Grep is a command used for finding some words or patterns in the texts.
 * Works with files if they are listed, looks for input otherwise.
 */
public class Grep extends Command {
    /**
     * Creates a new Grep instance
     *
     * @param arguments is a String array of arguments to pass to the command
     * @param input     is an input stream to pass to the command
     * @param output    is an output stream to pass to the command
     */
    public Grep(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    private class GrepArgs {
        @Parameter(names = {"-i", "--ignore-case"}, description = "Should be the case during the search ignored or not.")
        boolean ignoreCase;
        @Parameter(names = {"-w", "--word-regexp"}, description = "Select only those lines containing matches that form whole words.")
        boolean wordsFully;
        @Parameter(names = {"-A", "--after-context"}, description = "Print NUM lines of trailing context after matching lines.")
        int afterContext;

        @Parameter(required = true)
        List<String> others = new ArrayList<>();
    }

    /**
     * Runs command.
     * @throws CommandException if problem with output occurred.
     */
    @Override
    public void process() throws CommandException {
        GrepArgs args = new GrepArgs();
        try {
            JCommander.newBuilder()
                    .addObject(args)
                    .build().parse(arguments);
        } catch(ParameterException e) {
            throw new ArgumentException("Grep arguments problem.", e);
        }
        if (args.afterContext < 0) {
            throw new ArgumentException("Grep -A: should be non negative.", null);
        }
        String patternString = args.others.get(0);
        int flags = 0;
        if (args.ignoreCase) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        if (args.wordsFully) {
            patternString = "\\b" + patternString + "\\b";
        }
        Pattern pattern = Pattern.compile(patternString, flags);
        List<String> files = args.others.subList(1, args.others.size());
        if (!files.isEmpty()) {
            boolean first = true;
            for (String file : files) {
                List<String> lines = null;
                try {
                    if (!first) {
                        output.write(("--" + System.lineSeparator()).getBytes());
                    }
                    first = false;
                    lines = Files.readAllLines(Paths.get(file));
                } catch (IOException e) {
                    throw new ProblemsWithIOException(e);
                }
                processLinesList(pattern, args, lines, files.size() > 1 ? file : null);
            }
            return;
        }
        if (input == null) {
            throw new NoInputException("grep");
        }
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] current = new byte[1024];
            int read = input.read(current);
            while (read != -1) {
                buffer.write(current, 0, read);
                read = input.read(current);
            }
            String result = buffer.toString();
            String[] lines = result.split(System.lineSeparator());
            processLinesList(pattern, args, Arrays.asList(lines), null);
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        }
    }

    /**
     * Runs grep on content of file.
     * @param pattern a pattern to match
     * @param args arguments for grep
     * @param lines list of lines where to find matches
     * @param file a name of the current file (used for beautiful printing)
     * @throws ProblemsWithIOException if some problem with printing the information found.
     */
    private void processLinesList(Pattern pattern, GrepArgs args, List<String> lines, String file)
            throws ProblemsWithIOException {
        try {
            int toPrint = 0;
            boolean delimiter = false;
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    if (delimiter) {
                        output.write(("--" + System.lineSeparator()).getBytes());
                    }
                    if (file == null) {
                        output.write((line + System.lineSeparator()).getBytes());
                    } else {
                        output.write((file + ":" + line + System.lineSeparator()).getBytes());
                    }
                    output.flush();
                    toPrint = args.afterContext;
                    if (toPrint > 0) {
                        delimiter = true;
                    }
                } else if (toPrint > 0) {
                    if (file == null) {
                        output.write((line + System.lineSeparator()).getBytes());
                    } else {
                        output.write((file + "-" + line + System.lineSeparator()).getBytes());
                    }
                    output.flush();
                    toPrint--;
                }
            }
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        }
    }
}
