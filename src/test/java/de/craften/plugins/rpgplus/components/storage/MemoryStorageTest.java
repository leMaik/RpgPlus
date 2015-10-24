package de.craften.plugins.rpgplus.components.storage;

public class MemoryStorageTest extends StorageTest {
    @Override
    protected Storage createStorage() throws Exception {
        return new MemoryStorage();
    }
}
