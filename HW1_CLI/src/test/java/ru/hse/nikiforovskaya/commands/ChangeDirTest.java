package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeDirTest {
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
    public void processOneArgument() throws CommandException {
        ByteArrayOutputStream outputForOldPwd = new ByteArrayOutputStream();
        ByteArrayOutputStream outputForCd = new ByteArrayOutputStream();
        Pwd pwdStart = new Pwd(null, null, outputForOldPwd);
        pwdStart.process();
        ChangeDir cd = new ChangeDir(new String[]{"src"}, null, outputForCd);
        cd.process();
        ByteArrayOutputStream outputForNewPwd = new ByteArrayOutputStream();
        Pwd pwdEnd = new Pwd(null, null, outputForNewPwd);
        pwdEnd.process();

        String oldPath = outputForOldPwd.toString().split(System
                .lineSeparator())[0];
        String newPath = outputForNewPwd.toString().split(System
                .lineSeparator())[0];
        assertEquals("src", Paths.get(oldPath).relativize(Paths.get(newPath)).toString());
    }

    @Test
    public void otherCommands() throws CommandException {
        ByteArrayOutputStream outputForCd = new ByteArrayOutputStream();
        ChangeDir cd = new ChangeDir(new String[]{Paths.get("src", "test",
                "resources").toString()},
                null,
                outputForCd);
        cd.process();
        ByteArrayOutputStream outputForCat = new ByteArrayOutputStream();
        Cat cat = new Cat(new String[]{"cd_test.txt"}, null, outputForCat);
        cat.process();
        assertEquals("hello from cd and cat", outputForCat.toString());
    }
}