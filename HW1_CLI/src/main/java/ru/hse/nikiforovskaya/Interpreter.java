package ru.hse.nikiforovskaya;

import ru.hse.nikiforovskaya.commands.*;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.ProblemsWithIOException;
import ru.hse.nikiforovskaya.parser.exception.NoPairForQuoteException;
import ru.hse.nikiforovskaya.parser.exception.NoSuchVariableException;
import ru.hse.nikiforovskaya.parser.Parser;
import ru.hse.nikiforovskaya.parser.exception.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * Interpreter is a class with a possibility to run shell commands.
 * It has functions to do it programmatically or main to do it command-line.
 */
public class Interpreter {

    private HashMap<String, String> dictionary;

    /** ArgumentForCreator is a class to store all needed arguments for Commands.*/
    private class ArgumentForCreator {
        private String[] arguments;
        private InputStream input;
        private OutputStream output;

        ArgumentForCreator(String[] arguments, InputStream input, OutputStream output) {
            this.arguments = arguments;
            this.input = input;
            this.output = output;
        }
    }


    /** A map of existing commands */
    private final static HashMap<String, Function<ArgumentForCreator, ? extends Command>> existingCommands = new HashMap<String, Function<ArgumentForCreator, ? extends Command>>(){{
        put("cat", (Function<ArgumentForCreator, Cat>) args ->
                new Cat(args.arguments, args.input, args.output));
        put("echo", (Function<ArgumentForCreator, Echo>) args ->
                new Echo(args.arguments, args.input, args.output));
        put("wc", (Function<ArgumentForCreator, WordCount>) args ->
                new WordCount(args.arguments, args.input, args.output));
        put("pwd", (Function<ArgumentForCreator, Pwd>) args ->
                new Pwd(args.arguments, args.input, args.output));
        put("exit", (Function<ArgumentForCreator, Exit>) args ->
                new Exit(args.arguments, args.input, args.output));
    }};

    /** Creates an Interpreter with personal scope. */
    public Interpreter() {
        dictionary = new HashMap<>();
    }

    /**
     * Substitutes all the variables into the string and check pairs for quotations marks.
     * @param s a string to preprocess
     * @return a preprocessed string
     * @throws ParserException if quotation marks are not paired
     */
    private String preprocessWithSubstitute(String s) throws ParserException {
        StringBuilder result = new StringBuilder("");
        LinkedList<Character> stackOfQuotes = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (current == '$' && (stackOfQuotes.isEmpty() || stackOfQuotes.getFirst() == '\"')) {
                int j = i + 1;
                while (j < s.length() &&
                        (Character.isLetter(s.charAt(j)) || s.charAt(j) == '_')) {
                    j++;
                }
                String name = s.substring(i + 1, j);
                if (dictionary.containsKey(name)) {
                    result.append(dictionary.get(name));
                } else {
                    throw new NoSuchVariableException(name);
                }
                i = j - 1;
            } else {
                if (Parser.isQuote(current)) {
                    if (!stackOfQuotes.isEmpty() && stackOfQuotes.getLast() == current) {
                        stackOfQuotes.pollLast();
                    } else {
                        stackOfQuotes.add(current);
                    }
                }
                result.append(current);
            }
        }
        if (!stackOfQuotes.isEmpty()) {
            throw new NoPairForQuoteException(stackOfQuotes.getLast());
        }
        return result.toString();
    }

    /**
     * Utils function which returns the tail of the list
     * @param list a list which tail to return
     * @return an array of all the elements except for the first one
     */
    private String[] getTail(ArrayList<String> list) {
        if (list.size() == 1) {
            return null;
        }
        return list.subList(1, list.size()).toArray(new String[1]);
    }

    /**
     * Runs one command. Doesn't know anything about pipes.
     * @param s a command to run
     * @param input an input stream from which to read
     * @param output an output stream to write to
     * @throws ParserException if some parser exception occurred
     * @throws CommandException if an exception during running command occurred
     */
    private void runCommand(String s, InputStream input, OutputStream output) throws ParserException, CommandException {
        ArrayList<String> splitted = Parser.splitIntoWords(s);
        String command = splitted.get(0);
        if (splitted.size() == 1 && command.indexOf('=') != -1) {
            int index = command.indexOf('=');
            String key = command.substring(0, index);
            String value = command.substring(index + 1);
            dictionary.put(key, value);
            return;
        }
        if (!existingCommands.containsKey(command)) {
            Command toRun = new External(splitted.toArray(new String[1]), input, output);
            toRun.process();
            return;
        }
        Function<ArgumentForCreator, ? extends Command> creator = existingCommands.get(command);
        Command toRun = creator.apply(new ArgumentForCreator(getTail(splitted), input, output));
        toRun.process();
    }

    /**
     * Runs string as a set of commands, knows about pipes.
     * @param s a command to run
     * @throws ParserException if some parser exception occurred
     * @throws CommandException if an exception during running command occurred
     */
    public void processString(String s) throws ParserException, CommandException {
        String preprocessed = preprocessWithSubstitute(s);
        ArrayList<String> commands = Parser.splitIntoCommands(preprocessed);
        InputStream prev = null;
        ByteArrayOutputStream output = null;
        for (String command : commands) {
            output = new ByteArrayOutputStream();
            runCommand(command, prev, output);
            try {
                if (prev != null) {
                    prev.close();
                }
                output.close();
            } catch (IOException e) {
                throw new ProblemsWithIOException(e);
            }
            prev = new ByteArrayInputStream(output.toByteArray());
        }
        if (output == null) {
            return;
        }
        System.out.print(output.toString());
    }

    /**
     * Runs a command line interpreter
     * @param args not used
     * @throws IOException if some problems with standard input-output occurred
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Interpreter interpreter = new Interpreter();
        while (true) {
            String line = br.readLine();
            try {
                interpreter.processString(line);
            } catch (ParserException e) {
                System.out.println("Parser Exception " + e.getMessage());
            } catch (CommandException e) {
                System.out.println("CommandException " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("The cause is " + e.getCause().getMessage());
                }
            }
        }
    }
}
