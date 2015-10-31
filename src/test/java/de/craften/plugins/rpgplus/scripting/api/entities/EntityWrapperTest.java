package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.test.util.LuaCodeTest;
import org.bukkit.entity.Creeper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EntityWrapperTest extends LuaCodeTest {
    @Test
    public void testHealthField() {
        //create a mocked, minimal creeper
        Creeper mockEntity = mock(Creeper.class);
        when(mockEntity.getHealth()).thenReturn(42.0);
        ManagedEntity managedEntity = mock(ManagedEntity.class);
        when(managedEntity.getEntity()).thenReturn(mockEntity);

        //test if getting health works
        setLuaVariable("entity", EntityWrapper.wrap(managedEntity));
        assertEquals(42, executeLua("return entity.health").checkdouble(), 0);

        //test if setting health works
        executeLua("entity.health = 21");
        verify(mockEntity).setHealth(21);
    }

    @Test
    public void testMaximumHealthField() {
        //create a mocked, minimal creeper
        Creeper mockEntity = mock(Creeper.class);
        when(mockEntity.getMaxHealth()).thenReturn(42.0);
        ManagedEntity managedEntity = mock(ManagedEntity.class);
        when(managedEntity.getEntity()).thenReturn(mockEntity);

        //test if getting maximum health works
        setLuaVariable("entity", EntityWrapper.wrap(managedEntity));
        assertEquals(42, executeLua("return entity.maximumHealth").checkdouble(), 0);

        //test if setting maximum health works
        executeLua("entity.maximumHealth = 21");
        verify(mockEntity).setMaxHealth(21);
    }
}