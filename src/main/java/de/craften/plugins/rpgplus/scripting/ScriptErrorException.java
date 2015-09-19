package de.craften.plugins.rpgplus.scripting;

/**
 * Exception that is thrown when executing a script fails.
 */
public class ScriptErrorException extends Exception {
    public ScriptErrorException(String msg, Throwable inner) {
        super(msg, inner);
    }
}
