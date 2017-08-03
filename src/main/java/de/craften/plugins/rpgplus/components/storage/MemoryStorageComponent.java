package de.craften.plugins.rpgplus.components.storage;

public class MemoryStorageComponent extends StorageComponent{

	@Override
	protected void onActivated() {
		storage = new MemoryStorage();
	}
	
}
