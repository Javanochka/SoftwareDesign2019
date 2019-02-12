package ru.hse.nikiforovskaya.commands;

import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GrepTest {

    @Test
    void processNoneArguments() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{}, null, output);
        assertThrows(ParameterException.class, grep::process);
        output.close();
    }

    @Test
    void processSingleWordPattern() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"meow", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("meow\nmeow\n", result);
    }

    @Test
    void processSingleWordPatternPrintAfter() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"-A", "1", "meow", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("meow\n" +
                "\n--\n" +
                "meow\n" +
                "m   eo   w\n", result);
    }

    @Test
    void processSingleWordPatternNoMatch() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"abaca", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    void processSingleWordPatternCaseSensitive() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"Meow", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    void processSingleWordPatternCaseInsensitive() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"-i", "Meow", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("meow\nmeow\n", result);
    }

    @Test
    void processSingleWordPatternNonWord() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"me", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("meow\nmeow\n", result);
    }

    @Test
    void processSingleWordPatternWordNoMatch() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"-w", "me", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    void processSingleWordPatternWordMatch() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Grep grep = new Grep(new String[]{"-w", "meow", Paths.get("src", "test", "resources", "meow.txt").toString()}, null, output);
        grep.process();
        output.close();
        String result = output.toString();
        assertEquals("meow\nmeow\n", result);
    }
}