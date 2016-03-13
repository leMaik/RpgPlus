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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

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

        ScriptingManager scriptingManager = new ScriptingManager(testFolder.getRoot(), ScriptingManager.StrictModeOption.DISABLED);
        Varargs returnValue;
        returnValue = scriptingManager.executeScript(executedScript, "test");
        assertEquals("script should work properly", 1, returnValue.checkint(1));
        returnValue = scriptingManager.executeScript(executedScript, "test");
        assertEquals("script should work properly", 2, returnValue.checkint(1));

        scriptingManager.reset();
        returnValue = scriptingManager.executeScript(executedScript, "test");
        assertEquals("modules should reload when the script manager is reset", 1, returnValue.checkint(1));
    }

    @Test
    public void testGlobalVariableWarning() throws Exception {
        ScriptingManager scriptingManager = new ScriptingManager(testFolder.getRoot(), ScriptingManager.StrictModeOption.ERROR);
        try {
            scriptingManager.executeScript(StringUtils.join(new String[]{
                    "globalVar = 42"
            }, "\n"), "test");
            fail("Defining global variables should fail if strict mode is set to throw errors");
        } catch (ScriptErrorException ignore) {
        }

        final AtomicBoolean warned = new AtomicBoolean(false);
        scriptingManager = new ScriptingManager(testFolder.getRoot(), ScriptingManager.StrictModeOption.WARN) {
            @Override
            protected void reportScriptWarning(String warning) {
                warned.set(true);
            }
        };
        scriptingManager.executeScript(StringUtils.join(new String[]{
                "globalVar = 42"
        }, "\n"), "test");
        assertTrue("Defining global variables should show a warning if strict mode is set to show warnings", warned.get());

        warned.set(false);
        scriptingManager = new ScriptingManager(testFolder.getRoot(), ScriptingManager.StrictModeOption.DISABLED) {
            @Override
            protected void reportScriptWarning(String warning) {
                warned.set(true);
            }
        };
        try {
            scriptingManager.executeScript(StringUtils.join(new String[]{
                    "globalVar = 42"
            }, "\n"), "test");
        } catch (ScriptErrorException ignore) {
            fail("Defining global variables should not fail if strict mode is disabled");
        }
        assertFalse("Defining global variables should not show a warning if strict mode is disabled", warned.get());
    }
}