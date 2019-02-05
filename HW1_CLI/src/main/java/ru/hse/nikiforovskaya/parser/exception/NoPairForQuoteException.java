package ru.hse.nikiforovskaya.parser.exception;

/** NoPairForQuoteException is thrown every time we did not find a pair quotation mark. */
public class NoPairForQuoteException extends ParserException {
    /**
     * Creates a new NoPairForQuoteException
     * @param c a quotation mark which needs pair
     */
    public NoPairForQuoteException(char c) {
        super("Character \"" + c + "\" does not have pair.");
    }
}
