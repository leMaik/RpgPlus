package de.craften.plugins.rpgplus.components.storage;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;

import java.io.File;

/**
 * A component that offers a key-value storage.
 */
public class StorageComponent extends PluginComponentBase {
    private final File directory;
    private Storage storage;

    public StorageComponent(File directory) {
        this.directory = directory;
    }

    @Override
    protected void onActivated() {
        storage = new YamlStorage(directory);
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
