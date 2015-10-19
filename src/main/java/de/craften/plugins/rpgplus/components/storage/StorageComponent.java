package de.craften.plugins.rpgplus.components.storage;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;

/**
 * A component that offers a key-value storage.
 */
public class StorageComponent extends PluginComponentBase {
    private Storage storage;

    @Override
    protected void onActivated() {
        storage = new MemoryStorage();
    }

    /**
     * Get the storage.
     *
     * @return the storage
     */
    public Storage getStorage() {
        return storage;
    }
}
