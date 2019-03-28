package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.NoInputException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * WordCount or shortly known as wc command.
 * If given arguments, never uses input stream.
 * <p>
 * Format:
 * <p>
 * lines_1 words_1 bytes_1 file_1
 * ...
 * lines_n words_n bytes_n file_n
 * lines words bytes "total"
 * <p>
 * If arguments are not given, counts statistics from the input stream.
 * <p>
 * Format:
 * <p>
 * lines words bytes
 */
public class WordCount extends Command {

    /**
     * Creates a new WordCount instance
     *
     * @param arguments is a String array of arguments to pass to the command
     * @param input     is an input stream to pass to the command
     * @param output    is an output stream to pass to the command
     */
    public WordCount(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    /**
     * Runs command
     *
     * @throws CommandException if problem during input-output occurred. Also throws an exception if no input found while expected (no arguments case)
     */
    @Override
    public void process() throws CommandException {
        try {
            if (arguments != null) {
                int sumWords = 0;
                int sumLines = 0;
                int sumBytes = 0;
                for (String argument : arguments) {
                    List<String> lines = Files.readAllLines(getPath(argument));
                    int words = 0;
                    for (String line : lines) {
                        String[] result = line.trim().split("\\s+");
                        if (result.length == 1 && result[0].equals("")) {
                            continue;
                        }
                        words += result.length;
                    }
                    int bytes = Files.readAllBytes(getPath(argument)).length;
                    sumWords += words;
                    sumLines += lines.size();
                    sumBytes += bytes;
                    output.write((lines.size() + " " + words + " " + bytes + " " + argument + System.lineSeparator()).getBytes());
                    output.flush();
                }
                output.write((sumLines + " " + sumWords + " " + sumBytes + " total" + System.lineSeparator()).getBytes());
                output.flush();
            } else {
                if (input == null) {
                    throw new NoInputException("WordCount");
                }
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] current = new byte[1024];
                int read = input.read(current);
                int bytes = 0;
                while (read != -1) {
                    buffer.write(current, 0, read);
                    bytes += read;
                    read = input.read(current);
                }
                String result = buffer.toString();
                String[] lines = result.split(System.lineSeparator());
                int words = 0;
                for (String s : lines) {
                    String[] spliited = s.trim().split("\\s+");
                    if (spliited.length == 1 && spliited[0].equals("")) {
                        continue;
                    }
                    words += spliited.length;
                }
                output.write((lines.length + " " + words + " " + bytes + System.lineSeparator()).getBytes());
                output.flush();
            }
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        }
    }
}
