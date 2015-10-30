package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;

/**
 * A lua wrapper for a {@link de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity}.
 */
public class EntityWrapper {
    private final ManagedEntity entity;

    public EntityWrapper(ManagedEntity entity) {
        this.entity = entity;
    }
}
