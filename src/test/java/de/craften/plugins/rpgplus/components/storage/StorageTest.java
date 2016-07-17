package de.craften.plugins.rpgplus.components.storage;

import org.bukkit.OfflinePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class StorageTest {
    protected Storage storage;

    @Before
    public void setup() throws Exception {
        storage = createStorage();
    }

    protected abstract Storage createStorage() throws Exception;

    @Test
    public void testStorage() throws Exception {
        storage.set("foo", "bar");
        assertEquals("bar", storage.get("foo", ""));
        assertEquals("default", storage.get("other", "default"));

        Map<String, String> test = storage.getAll("notexisting");
        assertTrue(test.isEmpty());

        storage.set("deep.nested.test", "works");
        Map<String, String> deep = storage.getAll("deep");
        assertEquals(1, deep.size());
        assertEquals("works", deep.get("nested.test"));

        storage.set("foo", "bar");
        storage.set("foo", null);
        assertFalse(storage.contains("foo"));
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

        Map<String, String> test = storage.getAll(mockPlayer, "notexisting");
        assertTrue(test.isEmpty());

        storage.set(mockPlayer, "deep.nested.test", "works");
        Map<String, String> deep = storage.getAll(mockPlayer, "deep");
        assertEquals(1, deep.size());
        assertEquals("works", deep.get("nested.test"));

        storage.set(mockPlayer, "foo", "bar");
        storage.set(mockPlayer, "foo", null);
        assertFalse(storage.contains(mockPlayer, "foo"));
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