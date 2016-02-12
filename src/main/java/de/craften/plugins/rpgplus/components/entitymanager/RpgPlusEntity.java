package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.managedentities.ManagedEntityBase;
import de.craften.plugins.managedentities.behavior.SecondNameBehavior;
import de.craften.plugins.managedentities.behavior.VisibleNameBehavior;
import org.bukkit.Location;
import org.bukkit.entity.*;

import java.util.UUID;

/**
 * A basic managed entity without any special logic.
 */
public abstract class RpgPlusEntity<T extends Entity> extends ManagedEntityBase<T> {
    private boolean isTakingDamage;
    private boolean nameVisible;

    public RpgPlusEntity(Location location) {
        super(location);

        addBehavior(new VisibleNameBehavior());
        addBehavior(new SecondNameBehavior());
    }

    public String getName() {
        return getProperty(VisibleNameBehavior.NAME_PROPERTY_KEY);
    }

    public void setName(String name) {
        setProperty(VisibleNameBehavior.NAME_PROPERTY_KEY, name);
    }

    public String getSecondName() {
        return getProperty(SecondNameBehavior.NAME_PROPERTY_KEY);
    }

    public void setSecondName(String secondName) {
        setProperty(SecondNameBehavior.NAME_PROPERTY_KEY, secondName);
    }

    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setTakingDamage(boolean isTakingDamage) {
        this.isTakingDamage = isTakingDamage;
        //TODO this has no effect right now
    }

    public boolean isNameVisible() {
        return nameVisible;
    }

    public void setNameVisible(boolean nameVisible) {
        this.nameVisible = nameVisible;
        //TODO this has no effect right now
    }

    public Object getTarget() {
        Entity entity = getEntity();
        if (entity instanceof Creature) {
            return ((Creature) entity).getTarget();
        }
        return null;
    }

    public void setTarget(Player player) {
        Entity entity = getEntity();
        if (entity instanceof Creature) {
            ((Monster) entity).setTarget(player);
        }
        //TODO remember the target if the entity is not spawned yet
    }
}
