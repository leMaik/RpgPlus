package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

/**
 * A managed villager.
 */
public class ManagedVillager extends RpgPlusEntity<Villager> {
    private Villager.Profession profession;

    public ManagedVillager(Location location) {
        super(location, EntityType.VILLAGER);
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

    /*
    @Override
    protected Villager spawnEntity(Location location) {
        Villager villager = location.getWorld().spawn(location, Villager.class);
        if (profession != null) {
            villager.setProfession(profession);
        } else {
            profession = villager.getProfession();
        }
        return villager;
    }
    */

    // TODO add villager trait
}
