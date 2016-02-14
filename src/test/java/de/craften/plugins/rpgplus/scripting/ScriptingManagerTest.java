package de.craften.plugins.rpgplus.scripting;

import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.luaj.vm2.Varargs;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ScriptingManagerTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testRequireAfterReset() throws Exception {
        File requiredFile = testFolder.newFile("test.lua");
        Files.write(requiredFile.toPath(), Arrays.asList(
                "local x = 0",
                "return function()",
                "  x = x + 1",
                "  return x",
                "end"
        ), Charset.forName("UTF-8"));
        String executedScript = StringUtils.join(new String[]{
                "local inc = require \"./test\"",
                "return inc()"
        }, "\n");

        ScriptingManager scriptingManager = new ScriptingManager(testFolder.getRoot());
        Varargs returnValue;
        returnValue = scriptingManager.executeScript(executedScript, "test");
        assertEquals("script should work properly", 1, returnValue.checkint(1));
        returnValue = scriptingManager.executeScript(executedScript, "test");
        assertEquals("script should work properly", 2, returnValue.checkint(1));

        scriptingManager.reset();
        returnValue = scriptingManager.executeScript(executedScript, "test");
        assertEquals("modules should reload when the script manager is reset", 1, returnValue.checkint(1));
    }
}