package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.Interpreter;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class LsTest {
    private static String userDir;

    @BeforeAll
    public static void init() {
        userDir = System.getProperty("user.dir");
    }

    @BeforeEach
    public void reset() {
        System.setProperty("user.dir", userDir);
    }

    @Test
    public void processThisDir() throws CommandException {
        ByteArrayOutputStream outputForCd = new ByteArrayOutputStream();
        Interpreter interpreter = new Interpreter();
        ChangeDir changeDir = new ChangeDir(new String[]{Paths.get("src",
                "test",
                "resources", "test_dir").toString()}, null, outputForCd, interpreter);
        changeDir.process();
        ByteArrayOutputStream outputForLs = new ByteArrayOutputStream();
        Ls ls = new Ls(null, null, outputForLs, interpreter);
        ls.process();
        String[] files = outputForLs.toString().split(System.lineSeparator());
        String[] expected = new String[]{"a", "another_dir" + File.separator};
        Arrays.sort(files);
        assertArrayEquals(expected, files);
    }

    @Test
    public void processAnotherDir() throws CommandException {
        ByteArrayOutputStream outputForLs = new ByteArrayOutputStream();
        Ls ls = new Ls(new String[]{Paths.get("src",
            "test",
            "resources", "test_dir").toString()}, null, outputForLs, new Interpreter());
        ls.process();
        String[] files = outputForLs.toString().split(System.lineSeparator());
        String[] expected = new String[]{"a", "another_dir" + File.separator};
        Arrays.sort(files);
        assertArrayEquals(expected, files);
    }
}