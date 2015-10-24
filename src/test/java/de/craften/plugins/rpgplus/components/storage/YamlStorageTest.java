package de.craften.plugins.rpgplus.components.storage;

import org.bukkit.OfflinePlayer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YamlStorageTest extends StorageTest {
    @Rule
    public TemporaryFolder configFolder = new TemporaryFolder();

    @Override
    protected Storage createStorage() throws Exception {
        return new YamlStorage(configFolder.getRoot());
    }

    @Test
    public void testActuallyPersisted() throws Exception {
        OfflinePlayer mockPlayer = mock(OfflinePlayer.class);
        UUID uuid = UUID.randomUUID();
        when(mockPlayer.getUniqueId()).thenReturn(uuid);
        storage.set("foo", "bar");
        storage.set(mockPlayer, "key", "value");

        Storage otherStorage = createStorage();
        assertEquals("bar", otherStorage.get("foo", ""));
        assertEquals("value", otherStorage.get(mockPlayer, "key", ""));
    }
}
