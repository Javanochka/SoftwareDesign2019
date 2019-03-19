package ru.hse.nikiforovskaya.parser.exception;

/** ParserException is thrown for every problem occurred while reading strings */
public class ParserException extends Exception {
    /**
     * Creates a new ParserException with the given message
     * @param message a message explaining the reason exception happened
     */
    public ParserException(String message) {
        super(message);
    }
}
