package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.Interpreter;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CatTest {
    @Test
    void processGeneralFile() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Cat cat = new Cat(new String[]{Paths.get("src", "test", "resources",
                "abaca.txt").toString()}, null, output, new Interpreter());
        cat.process();
        output.close();
        String result = output.toString();
        assertEquals("a   b\n" +
                "\n" +
                "c", result);
    }

    @Test
    void processNothing() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Cat cat = new Cat(new String[]{}, null, output, new Interpreter());
        cat.process();
        output.close();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    void processSeveralFiles() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Cat cat = new Cat(new String[]{
                Paths.get("src", "test", "resources", "meow.txt").toString(),
                Paths.get("src", "test", "resources", "abaca.txt").toString()
        }, null, output, new Interpreter());
        cat.process();
        output.close();
        String result = output.toString();
        assertEquals("meow\n" +
                "\n" +
                "meow\n" +
                "m   eo   wa   b\n" +
                "\n" +
                "c", result);
    }

}