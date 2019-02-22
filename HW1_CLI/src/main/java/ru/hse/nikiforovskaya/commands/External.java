package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.ExternalException;
import ru.hse.nikiforovskaya.commands.exception.InterruptionException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * External is for executing any external command.
 * First argument in arguments array is the name of the external command.
 */
public class External extends Command {

    /**
     * Creates a new External instance
     * @param arguments is a String array of arguments to pass to the command
     * @param input is an input stream to pass to the command
     * @param output is an output stream to pass to the command
     */
    public External(String[] arguments, InputStream input, OutputStream output) {
        super(arguments, input, output);
    }

    /**
     * Runs command
     * @throws CommandException if an exception occurred during command running
     */
    @Override
    public void process() throws CommandException {
        try {
            List<String> args = Arrays.stream(arguments).collect(Collectors.toList());
            ProcessBuilder pb = new ProcessBuilder(args);

            Process process = pb.start();
            OutputStream processOutput = process.getOutputStream();
            if (input != null) {
                input.transferTo(processOutput);
                processOutput.close();
            }
            int result = process.waitFor();
            if (result != 0) {
                throw new ExternalException("Got error code: " + result +
                        ". Here its message: " + new String(process.getErrorStream().readAllBytes()));
            }
            InputStream processInput = process.getInputStream();
            processInput.transferTo(output);
            output.flush();
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        } catch (InterruptedException e) {
            throw new InterruptionException(e);
        }
    }
}
