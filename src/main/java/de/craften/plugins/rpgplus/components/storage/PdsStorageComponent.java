package de.craften.plugins.rpgplus.components.storage;

public class PdsStorageComponent extends StorageComponent{

	@Override
	protected void onActivated() {
		storage = new PDSStorage();
	}
	
}
