package de.craften.plugins.rpgplus.components.storage;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * A component that offers a key-value storage.
 */
public class StorageComponent extends PluginComponentBase {
    private final StorageType type;
	private final File directory;
    private Storage storage;

    public StorageComponent(StorageType type, File directory) {
    	this.type = type;
        this.directory = directory;
    }

    @Override
    protected void onActivated() {
        try {
        	switch (type) {
        	case MEMORY:
				storage = new MemoryStorage();
				break;
        	case YAML:
	            storage = new YamlStorage(directory);
        		break;
			case PDS:
	            storage = new PDSStorage();
				break;
			}
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Initializing the storage failed", e);
        }
    }

    /**
     * Get the storage.
     *
     * @return the storage
     */
    public Storage getStorage() {
        return storage;
    }
    
    public StorageType getStorageType() {
    	return type;
    }
}
