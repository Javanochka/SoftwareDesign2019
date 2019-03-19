package ru.hse.nikiforovskaya.parser;

import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.parser.exception.ParserException;
import ru.hse.nikiforovskaya.parser.exception.PipePlacingException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {


    private Parser parser = new Parser();

    @Test
    void splitIntoWords() throws ParserException {
        List<String> result = parser.splitIntoWords("aba");
        assertArrayEquals(new String[]{"aba"}, result.toArray(new String[1]));
        result = parser.splitIntoWords("aba     b");
        assertArrayEquals(new String[]{"aba", "b"}, result.toArray(new String[1]));
        result = parser.splitIntoWords("aba \"   meow\"  c");
        assertArrayEquals(new String[]{"aba", "   meow", "c"},
                result.toArray(new String[1]));
    }

    @Test
    void splitIntoCommands() throws ParserException {
        List<String> result = parser.splitIntoCommands("aba");
        assertArrayEquals(new String[]{"aba"}, result.toArray(new String[1]));
        result = parser.splitIntoCommands("aba  |   b");
        assertArrayEquals(new String[]{"aba  ", "   b"}, result.toArray(new String[1]));
        result = parser.splitIntoCommands("aba \"|   meow\"  c");
        assertArrayEquals(new String[]{"aba \"|   meow\"  c"},
                result.toArray(new String[1]));
        result = parser.splitIntoCommands("aba | \"   meow\" | c");
        assertArrayEquals(new String[]{"aba ", " \"   meow\" ", " c"},
                result.toArray(new String[1]));
    }

    @Test
    void splitIntoCommandsTooManyPipes() {
        assertThrows(PipePlacingException.class, () -> parser.splitIntoCommands("| cat meow ||"));
        assertThrows(PipePlacingException.class, () -> parser.splitIntoCommands("cat meow | | echo"));
    }
}