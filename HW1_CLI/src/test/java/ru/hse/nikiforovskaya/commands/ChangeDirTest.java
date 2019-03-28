package ru.hse.nikiforovskaya.commands;

import org.junit.jupiter.api.Test;
import ru.hse.nikiforovskaya.commands.exception.CommandException;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ChangeDirTest {
    @Test
    public void processOneArgument() throws CommandException  {
        ByteArrayOutputStream outputForOldPwd = new ByteArrayOutputStream();
        ByteArrayOutputStream outputForCd = new ByteArrayOutputStream();
        Pwd pwdStart = new Pwd(null, null, outputForOldPwd);
        pwdStart.process();
        ChangeDir cd = new ChangeDir(new String[]{"src"}, null, outputForCd);
        cd.process();
        ByteArrayOutputStream outputForNewPwd = new ByteArrayOutputStream();
        Pwd pwdEnd = new Pwd(null, null, outputForNewPwd);
        pwdEnd.process();

        String oldPath = outputForOldPwd.toString();
        String newPath = outputForNewPwd.toString();

        //assertEquals(newPath.getParent().toString(), oldPath.toString());
        //System.out.println(Paths.get(newPath).relativize(Paths.get(oldPath)).toString());
    }

}