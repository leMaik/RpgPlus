package de.craften.plugins.rpgplus.components.storage;

/**
 * An exception that is thrown when accessing the storage fails.
 */
public class StorageException extends Exception {
    public StorageException(Exception inner) {
        super(inner);
    }

    public StorageException(String msg, Exception inner) {
        super(msg, inner);
    }

    public StorageException(String msg) {
        super(msg);
    }
}
