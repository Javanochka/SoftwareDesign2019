package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.Interpreter;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeDirTest {
    @Test
    public void processOneArgument() throws CommandException {
        ByteArrayOutputStream outputForOldPwd = new ByteArrayOutputStream();
        ByteArrayOutputStream outputForCd = new ByteArrayOutputStream();
        Interpreter interpreter = new Interpreter();
        Pwd pwdStart = new Pwd(null, null, outputForOldPwd, interpreter);
        pwdStart.process();
        ChangeDir cd = new ChangeDir(new String[]{"src"}, null, outputForCd,
                interpreter);
        cd.process();
        ByteArrayOutputStream outputForNewPwd = new ByteArrayOutputStream();
        Pwd pwdEnd = new Pwd(null, null, outputForNewPwd, interpreter);
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
        Interpreter interpreter = new Interpreter();
        ChangeDir cd = new ChangeDir(new String[]{Paths.get("src", "test",
                "resources").toString()},
                null,
                outputForCd,
                interpreter);
        cd.process();
        ByteArrayOutputStream outputForCat = new ByteArrayOutputStream();
        Cat cat = new Cat(new String[]{"cd_test.txt"}, null, outputForCat, interpreter);
        cat.process();
        assertEquals("hello from cd and cat", outputForCat.toString());
    }
}