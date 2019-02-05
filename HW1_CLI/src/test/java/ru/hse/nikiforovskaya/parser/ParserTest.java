package ru.hse.nikiforovskaya.parser;

import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.parser.exception.ParserException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void splitIntoWords() throws ParserException {
        List<String> result = Parser.splitIntoWords("aba");
        assertArrayEquals(new String[]{"aba"}, result.toArray(new String[1]));
        result = Parser.splitIntoWords("aba     b");
        assertArrayEquals(new String[]{"aba", "b"}, result.toArray(new String[1]));
        result = Parser.splitIntoWords("aba \"   meow\"  c");
        assertArrayEquals(new String[]{"aba", "   meow", "c"},
                result.toArray(new String[1]));
    }

    @Test
    void splitIntoCommands() throws ParserException {
        List<String> result = Parser.splitIntoCommands("aba");
        assertArrayEquals(new String[]{"aba"}, result.toArray(new String[1]));
        result = Parser.splitIntoCommands("aba  |   b");
        assertArrayEquals(new String[]{"aba  ", "   b"}, result.toArray(new String[1]));
        result = Parser.splitIntoCommands("aba \"|   meow\"  c");
        assertArrayEquals(new String[]{"aba \"|   meow\"  c"},
                result.toArray(new String[1]));
        result = Parser.splitIntoCommands("aba | \"   meow\" | c");
        assertArrayEquals(new String[]{"aba ", " \"   meow\" ", " c"},
                result.toArray(new String[1]));
    }
}