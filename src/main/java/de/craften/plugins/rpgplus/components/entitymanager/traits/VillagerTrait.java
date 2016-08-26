package de.craften.plugins.rpgplus.components.entitymanager.traits;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.entity.Villager;

public class VillagerTrait extends Trait {
    private Villager.Profession profession;

    public VillagerTrait() {
        super("RpgPlus Horse Trait");
    }

    @Override
    public void onSpawn() {
        Villager horse = (Villager) getNPC().getEntity();
        if (profession != null) horse.setProfession(profession);
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        if (key.keyExists("profession")) profession = Villager.Profession.valueOf(key.getString("profession"));
    }

    @Override
    public void save(DataKey key) {
        if (profession != null) key.setString("profession", profession.toString());
    }

    public Villager.Profession getProfession() {
        return profession;
    }

    public void setProfession(Villager.Profession profession) {
        this.profession = profession;
        if (getNPC().isSpawned()) ((Villager) getNPC().getEntity()).setProfession(profession);
    }
}
