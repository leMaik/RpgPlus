package de.craften.plugins.rpgplus.components.storage;

public class PdsStorageComponent extends StorageComponent{

	public PdsStorageComponent() {
		super(StorageType.PDS);
	}
	
	@Override
	protected void onActivated() {
		storage = new PDSStorage();
	}
	
}
