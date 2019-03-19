package ru.hse.nikiforovskaya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.parser.Parser;
import ru.hse.nikiforovskaya.parser.exception.ParserException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    private ByteArrayOutputStream output;
    private Parser parser = new Parser();

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @Test
    void testExample() throws CommandException, ParserException {
        Interpreter intr = new Interpreter(parser);
        intr.processString("echo \"Hello, world!\"");
        assertEquals("Hello, world!" + System.lineSeparator(), output.toString());
        intr.processString("FILE=" + Paths.get("src", "test", "resources", "abaca.txt").toString());
        intr.processString("cat $FILE");
        assertEquals("Hello, world!" + System.lineSeparator() +
                "a   b\n" +
                "\n" +
                "c", output.toString());
        intr.processString("cat $FILE | wc");
        assertEquals("Hello, world!" + System.lineSeparator() +
                "a   b\n" +
                "\n" +
                "c" +
                "3 3 8" + System.lineSeparator(), output.toString());
    }

    @Test
    void testQuotes() throws CommandException, ParserException {
        Interpreter intr = new Interpreter(parser);
        intr.processString("m=meow");
        intr.processString("echo \'$m\'");
        assertEquals("$m" + System.lineSeparator(), output.toString());
        output.reset();
        intr.processString("echo \"$m\"");
        assertEquals("meow" + System.lineSeparator(), output.toString());
        output.reset();
        intr.processString("echo \"\'$m\'\"");
        assertEquals("\'meow\'" + System.lineSeparator(), output.toString());
    }
}