package de.craften.plugins.rpgplus.components.storage;

public class PdsStorageComponent extends StorageComponent{

	public PdsStorageComponent() {
		
	}
	
	@Override
	protected void onActivated() {
		storage = new PDSStorage();
	}
	
}
