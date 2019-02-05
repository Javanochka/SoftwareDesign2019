package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.NoInputException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WordCountTest {
    @Test
    void processGeneralFile() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        WordCount wc = new WordCount(new String[]{Paths.get("src", "test", "resources", "abaca.txt").toString()}, null, output);
        wc.process();
        output.close();
        String result = output.toString();
        assertEquals("3 3 8 " +
                Paths.get("src", "test", "resources", "abaca.txt").toString() +
                System.lineSeparator() +
                "3 3 8 total" + System.lineSeparator(), result);
    }

    @Test
    void processTwoFiles() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        WordCount wc = new WordCount(new String[]{
                Paths.get("src", "test", "resources", "abaca.txt").toString(),
                Paths.get("src", "test", "resources", "meow.txt").toString()
        }, null, output);
        wc.process();
        output.close();
        String result = output.toString();
        assertEquals("3 3 8 " +
                Paths.get("src", "test", "resources", "abaca.txt").toString() +
                System.lineSeparator() +
                "4 5 21 " +
                Paths.get("src", "test", "resources", "meow.txt").toString() +
                System.lineSeparator()+
                "7 8 29 total" + System.lineSeparator(), result);
    }

    @Test
    void processThrowsException() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        WordCount wc = new WordCount(null, null, output);
        assertThrows(NoInputException.class, wc::process);
        output.close();
    }

    @Test
    void processFromInput() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream("meow a".getBytes());
        WordCount wc = new WordCount(null, input, output);
        wc.process();
        output.close();
        String result = output.toString();
        assertEquals("1 2 6" + System.lineSeparator(), result);
    }
}