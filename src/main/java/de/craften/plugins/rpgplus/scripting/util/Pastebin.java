package de.craften.plugins.rpgplus.scripting.util;

import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Methods for Pastebin.
 */
public class Pastebin {
    /**
     * Posts a stacktrace to Pastebin. The paste will expire in one hour.
     *
     * @param devKey developer key
     * @param title  title of the paste
     * @param t      throwable to paste the stacktrace of
     * @return URL of the paste
     * @throws IOException if creating the paste fails
     */
    public static String createStacktracePaste(String devKey, String title, Throwable t) throws IOException {
        String stacktrace;
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            stacktrace = sw.toString();

            final PastebinFactory factory = new PastebinFactory();
            final Paste paste = factory.createPaste()
                    .setTitle(title)
                    .setRaw(stacktrace)
                    .setMachineFriendlyLanguage("text")
                    .setVisiblity(PasteVisiblity.Unlisted)
                    .setExpire(PasteExpire.OneHour)
                    .build();

            return factory.createPastebin(devKey).post(paste).get();
        }
    }
}