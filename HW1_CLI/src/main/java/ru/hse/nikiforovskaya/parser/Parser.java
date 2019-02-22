package ru.hse.nikiforovskaya.parser;

import ru.hse.nikiforovskaya.parser.exception.NoPairForQuoteException;
import ru.hse.nikiforovskaya.parser.exception.NoSuchVariableException;
import ru.hse.nikiforovskaya.parser.exception.ParserException;
import ru.hse.nikiforovskaya.parser.exception.PipePlacingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * Parser has util to work with strings, to split them onto commands and words.
 * Parser does not know anything about commands.
 * Knows only two types of quotes: " and '
 */
public class Parser {

    /**
     * ParserResult is a class to store internal results in parser.
     * It has the result string and a position, where the string ends.
     */
    private static class ParserResult {
        String result;
        int last;

        ParserResult(String result, int last) {
            this.result = result;
            this.last = last;
        }
    }

    /**
     * Finds the pair position for the current symbol, if it is quote.
     * @param s a string in which to search
     * @param i current position in the string
     * @return a pair position
     * @throws ParserException if did not find a pair quote
     */
    private static int findPairPosition(String s, int i) throws ParserException {
        if (!isQuote(s.charAt(i))) {
            return -1;
        }
        char quote = s.charAt(i);
        for (int j = i + 1; j < s.length(); j++) {
            char current = s.charAt(j);
            if (quote == current) {
                return j;
            }
        }
        throw new NoPairForQuoteException(s.charAt(i));
    }

    /**
     * Finds first word in the string starting from start point.
     * Words are something that is separated with chars for which predicate is true.
     * @param s a string to search in
     * @param startPoint a position from where to start the search
     * @param predicate shows which chars should divide the words
     * @param deleteQuotes delete external quotes if {@code true}
     * @return ParserResult with the found word and the position, where stopped.
     * @throws ParserException if did not find a pair quote
     */
    private static ParserResult getFirstWord(String s, int startPoint, Predicate<Character> predicate, boolean deleteQuotes) throws ParserException {
        StringBuilder answer = new StringBuilder("");
        int i = startPoint;
        while (i < s.length() && predicate.test(s.charAt(i))) {
            i++;
        }
        if (i == s.length()) {
            return null;
        }
        int j = i;
        while (true) {
            while (j < s.length() && !predicate.test(s.charAt(j))
                    && !isQuote(s.charAt(j))) {
                answer.append(s.charAt(j++));
            }
            if (j == s.length() || predicate.test(s.charAt(j))) {
                return new ParserResult(answer.toString(), j);
            }
            int k = findPairPosition(s, j);
            if (deleteQuotes) {
                answer.append(s, j + 1, k);
            } else {
                answer.append(s, j, k + 1);
            }
            j = k + 1;
        }
    }

    /**
     * Splits the string on words by predicate
     * @param s a string to split
     * @param predicate shows which chars should divide the string onto words
     * @param deleteQuotes delete external quotes if {@code true}
     * @return ArrayList of the words got
     * @throws ParserException if did not find a pair quote
     */
    private static ArrayList<String> splitByPredicate(String s, Predicate<Character> predicate, boolean deleteQuotes) throws ParserException {
        ParserResult current = getFirstWord(s, 0, predicate, deleteQuotes);
        ArrayList<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current.result);
            current = getFirstWord(s, current.last, predicate, deleteQuotes);
        }
        return result;
    }

    /**
     * Splits a string onto words by spaces
     * @param s a string to split
     * @return ArrayList of words
     * @throws ParserException if did not find a pair quote
     */
    public static ArrayList<String> splitIntoWords(String s) throws ParserException {
        return splitByPredicate(s, Character::isSpaceChar, true);
    }

    /**
     * Splits a string onto words by pipe
     * @param s a string to split
     * @return ArrayList of commands
     * @throws ParserException if did not find a pair quote ot pipes are placed wrong
     */
    public static ArrayList<String> splitIntoCommands(String s) throws ParserException {
        if (s.length() > 0 && (s.charAt(0) == '|' || s.charAt(s.length() - 1) == '|')) {
            throw new PipePlacingException(s);
        }
        ArrayList<String> result = splitByPredicate(s, c -> c == '|', false);
        if (result.size() <= 1) {
            return result;
        }
        for (String toCheck : result) {
            if (toCheck.isBlank()) {
                throw new PipePlacingException(s);
            }
        }
        return result;
    }

    /**
     * Substitutes all the variables into the string and check pairs for quotations marks.
     * @param s a string to preprocess
     * @return a preprocessed string
     * @throws ParserException if quotation marks are not paired
     */
    public static String preprocessWithSubstitute(String s, HashMap<String, String> dictionary) throws ParserException {
        StringBuilder result = new StringBuilder("");
        char lastQuote = 0;
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (current == '$' && lastQuote != '\'') {
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
                    if (lastQuote == current) {
                        lastQuote = 0;
                    } else if (lastQuote == 0){
                        lastQuote = current;
                    }
                }
                result.append(current);
            }
        }
        if (lastQuote != 0) {
            throw new NoPairForQuoteException(lastQuote);
        }
        return result.toString();
    }

    /**
     * Checks if the given character is a quotation mark.
     * @param c a char to check
     * @return {@code true} if c is a quotation mark, {@code false} otherwise
     */
    public static boolean isQuote(char c) {
        return c == '\'' || c == '\"';
    }
}
