package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Villager;

/**
 * A managed villager.
 */
public class ManagedVillager extends BasicManagedEntity<Villager> {
    private Villager.Profession profession;

    ManagedVillager(Location location, EntityManager manager) {
        super(Villager.class, location, manager);
    }

    public Villager.Profession getProfession() {
        return profession;
    }

    public void setProfession(Villager.Profession profession) {
        this.profession = profession;
        if (this.getEntity() != null) {
            this.getEntity().setProfession(profession);
        }
    }

    @Override
    public void spawn() {
        super.spawn();
        if (profession != null) {
            this.getEntity().setProfession(profession);
        }
    }
}
