package de.craften.plugins.rpgplus.components.storage;

public class MemoryStorageComponent extends StorageComponent{

	public MemoryStorageComponent() {
		super(StorageType.MEMORY);
	}
	
	@Override
	protected void onActivated() {
		storage = new MemoryStorage();
	}
	
}
