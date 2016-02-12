package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.test.util.LuaCodeTest;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

public class EntityWrapperTest extends LuaCodeTest {
    private RpgPlusEntity managedEntity;
    private Entity entity;

    private void mockDamagableEntity() {
        //create a mocked, minimal creeper
        entity = mock(Creeper.class);
        assumeTrue(entity instanceof Damageable);
        managedEntity = mock(RpgPlusEntity.class);
        when(managedEntity.getEntity()).thenReturn(entity);
    }

    @Test
    public void testHealthField() {
        mockDamagableEntity();
        setLuaVariable("entity", EntityWrapper.wrap(managedEntity));

        //test if getting health works
        when(((Damageable) entity).getHealth()).thenReturn(42.0);
        assertEquals(42, executeLua("return entity.health").checkdouble(), 0);

        //test if setting health works
        executeLua("entity.health = 21");
        verify((Damageable) entity).setHealth(21);
    }

    @Test
    public void testMaximumHealthField() {
        mockDamagableEntity();
        setLuaVariable("entity", EntityWrapper.wrap(managedEntity));

        //test if getting maximum health works
        when(((Damageable) entity).getMaxHealth()).thenReturn(42.0);
        assertEquals(42, executeLua("return entity.maxHealth").checkdouble(), 0);

        //test if setting maximum health works
        executeLua("entity.maxHealth = 21");
        verify((Damageable) entity).setMaxHealth(21);
    }

    @Test
    public void setSimpleFields() {
        mockDamagableEntity();
        setLuaVariable("entity", EntityWrapper.wrap(managedEntity));

        // test getters
        when(managedEntity.getName()).thenReturn("name");
        assertEquals("name", executeLua("return entity.name").checkjstring());

        when(managedEntity.getSecondName()).thenReturn("name2");
        assertEquals("name2", executeLua("return entity.secondName").checkjstring());

        when(managedEntity.isNameVisible()).thenReturn(false);
        assertFalse(executeLua("return entity.nameVisible").checkboolean());

        when(managedEntity.isTakingDamage()).thenReturn(false);
        assertTrue(executeLua("return entity.invulnerable").checkboolean());

        // test setters
        executeLua("entity.name = \"newname\"");
        verify(managedEntity).setName("newname");

        executeLua("entity.secondName = \"newSecondName\"");
        verify(managedEntity).setSecondName("newSecondName");

        executeLua("entity.nameVisible = true");
        verify(managedEntity).setNameVisible(true);

        executeLua("entity.invulnerable = false");
        verify(managedEntity).setTakingDamage(true);
    }
}