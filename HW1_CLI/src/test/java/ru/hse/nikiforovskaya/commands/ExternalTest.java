package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.commands.exception.CommandException;
import ru.hse.nikiforovskaya.commands.exception.ExternalException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExternalTest {

    // I don't actually know, if there is echo in Windows, sorry :(
    @Test
    void processEchoExternal() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        External ext = new External(new String[]{"echo", "aba    a", "meow"}, null, output);
        ext.process();
        output.close();
        String result = output.toString();
        assertEquals("aba    a meow" + System.lineSeparator(), result);
    }

    @Test
    void processCatExternalException() throws CommandException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        External ext = new External(new String[]{"cat", "this_shouldnt_exist.nono"}, null, output);
        assertThrows(ExternalException.class, ext::process);
        output.close();
    }
}