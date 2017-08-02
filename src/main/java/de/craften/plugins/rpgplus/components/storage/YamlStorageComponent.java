package de.craften.plugins.rpgplus.components.storage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class YamlStorageComponent extends StorageComponent{

	private File directory;
	
	public YamlStorageComponent(File directory) {
		super(StorageType.YAML);
		this.directory = directory;
	}
	
	@Override
	protected void onActivated() {
		try {
			storage = new YamlStorage(directory);
		} catch (IOException e) {
            getLogger().log(Level.SEVERE, "Initializing the storage failed", e);
		}
	}
	
}
