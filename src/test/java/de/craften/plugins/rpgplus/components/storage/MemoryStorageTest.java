package de.craften.plugins.rpgplus.components.storage;

import org.bukkit.OfflinePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryStorageTest {
    private MemoryStorage storage;

    @Before
    public void setup() throws Exception {
        storage = new MemoryStorage();
    }

    @Test
    public void testStorage() throws Exception {
        storage.set("foo", "bar");
        assertEquals("bar", storage.get("foo", ""));
        assertEquals("default", storage.get("other", "default"));
    }

    @Test
    public void testPlayerStorage() throws Exception {
        OfflinePlayer mockPlayer = mock(OfflinePlayer.class);
        UUID uuid = UUID.randomUUID();
        when(mockPlayer.getUniqueId()).thenReturn(uuid);
        OfflinePlayer mockPlayer2 = mock(OfflinePlayer.class);
        UUID uuid2 = UUID.randomUUID();
        when(mockPlayer2.getUniqueId()).thenReturn(uuid2);

        storage.set(mockPlayer, "foo", "bar");
        assertEquals("bar", storage.get(mockPlayer, "foo", ""));
        assertEquals("default", storage.get(mockPlayer, "other", "default"));

        assertEquals("default", storage.get(mockPlayer2, "foo", "default"));
    }

    @Test
    public void testGetAll() throws Exception {
        storage.set("this.is.bar", "value");
        storage.set("this.is.a.test", "value2");
        storage.set("something", "value3");

        Map<String, String> values = storage.getAll("this.is");
        assertEquals(2, values.size());
        assertEquals("value", values.get("bar"));
        assertEquals("value2", values.get("a.test"));
    }
}