package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.managedentities.ManagedEntityBase;
import de.craften.plugins.managedentities.behavior.SecondNameBehavior;
import de.craften.plugins.managedentities.behavior.VisibleNameBehavior;
import de.craften.plugins.managedentities.util.nms.NmsEntityUtil;
import org.bukkit.Location;
import org.bukkit.entity.*;

/**
 * A basic managed entity without any special logic.
 */
public abstract class RpgPlusEntity<T extends Entity> extends ManagedEntityBase<T> {
    private boolean isTakingDamage = true;
    private boolean nameVisible = true;

    public RpgPlusEntity(Location location) {
        super(location);

        addBehavior(new VisibleNameBehavior());
        addBehavior(new SecondNameBehavior());
    }

    @Override
    public void spawn() {
        super.spawn();

        if (getEntity() instanceof LivingEntity && !isTakingDamage) {
            NmsEntityUtil.setInvulnerable((LivingEntity) getEntity(), true);
        }
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

        if (getEntity() instanceof LivingEntity) {
            NmsEntityUtil.setInvulnerable((LivingEntity) getEntity(), !isTakingDamage);
        }
    }

    public boolean isNameVisible() {
        return nameVisible;
    }

    public void setNameVisible(boolean nameVisible) {
        if (nameVisible != this.nameVisible) {
            this.nameVisible = nameVisible;
            if (nameVisible) {
                removeBehavior(getBehaviors(VisibleNameBehavior.class).iterator().next());
            } else {
                addBehavior(new VisibleNameBehavior());
            }
        }
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

    @Override
    public void teleport(Location location) {
        Location old = getLocation();
        if (old != null) {
            super.teleport(location.clone().setDirection(location.toVector().subtract(old.toVector())));
        } else {
            super.teleport(location);
        }
    }
}
