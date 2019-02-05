package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EchoTest {

    @Test
    void processNullArguments() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Echo echo = new Echo(null, null, output);
        echo.process();
        output.close();
        String result = output.toString();
        assertEquals(System.lineSeparator(), result);
    }

    @Test
    void processEmptyArrayArguments() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Echo echo = new Echo(new String[]{}, null, output);
        echo.process();
        output.close();
        String result = output.toString();
        assertEquals(System.lineSeparator(), result);
    }

    @Test
    void processSeveralElementsArguments() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Echo echo = new Echo(new String[]{"a     b", "c"}, null, output);
        echo.process();
        output.close();
        String result = output.toString();
        assertEquals("a     b c" + System.lineSeparator(), result);
    }

    @Test
    void processOneElementArguments() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Echo echo = new Echo(new String[]{"a     b"}, null, output);
        echo.process();
        output.close();
        String result = output.toString();
        assertEquals("a     b" + System.lineSeparator(), result);
    }

    @Test
    void processWithNewlineArguments() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Echo echo = new Echo(new String[]{"meow", "a\n b"}, null, output);
        echo.process();
        output.close();
        String result = output.toString();
        assertEquals("meow a\n b" + System.lineSeparator(), result);
    }
}