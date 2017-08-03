package de.craften.plugins.rpgplus.components.storage;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * A component that offers a key-value storage.
 */
public abstract class StorageComponent extends PluginComponentBase {
    protected Storage storage;

    public StorageComponent() {
    	
    }

    @Override
    protected abstract void onActivated();

    /**
     * Get the storage.
     *
     * @return the storage
     */
    public Storage getStorage() {
        return storage;
    }
    
}
