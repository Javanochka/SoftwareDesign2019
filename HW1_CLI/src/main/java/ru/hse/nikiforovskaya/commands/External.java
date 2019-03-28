package ru.hse.nikiforovskaya.commands;

import ru.hse.nikiforovskaya.Interpreter;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
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
     * Creates a new Command instance
     *
     * @param arguments   is a String array of arguments to pass to the command
     * @param input       is an input stream to pass to the command
     * @param output      is an output stream to pass to the command
     * @param interpreter is an interpreter which executes this command
     */
    public External(String[] arguments, InputStream input, OutputStream
            output, Interpreter interpreter) {
        super(arguments, input, output, interpreter);
    }

    /**
     * Runs command
     *
     * @throws CommandException if an exception occurred during command running
     */
    @Override
    public void process() throws CommandException {
        try {
            List<String> args = Arrays.stream(arguments).collect(Collectors.toList());
            ProcessBuilder pb = new ProcessBuilder(args).directory(
                    getPath(".").toFile());

            Process process = pb.start();
            OutputStream processOutput = process.getOutputStream();
            if (input != null) {
                byte[] buf = new byte[1024];
                int read;
                while ((read = input.read(buf)) != -1) {
                    processOutput.write(buf, 0, read);
                }
                processOutput.close();
            }
            process.waitFor();
            InputStream processInput = process.getInputStream();
            byte[] buf = new byte[1024];
            int read;
            while ((read = processInput.read(buf)) != -1) {
                output.write(buf, 0, read);
            }
            output.flush();
        } catch (IOException e) {
            throw new ProblemsWithIOException(e);
        } catch (InterruptedException e) {
            throw new InterruptionException(e);
        }
    }
}
