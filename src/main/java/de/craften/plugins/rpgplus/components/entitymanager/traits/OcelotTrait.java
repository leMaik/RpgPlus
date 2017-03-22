package de.craften.plugins.rpgplus.components.entitymanager.traits;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.entity.Ocelot;

public class OcelotTrait extends Trait {
    private Ocelot.Type catType;

    public OcelotTrait() {
        super("RpgPlus Ocelot Trait");
    }

    @Override
    public void onSpawn() {
        Ocelot ocelot = (Ocelot) getNPC().getEntity();
        if (catType != null) ocelot.setCatType(catType);
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        if (key.keyExists("catType")) catType = Ocelot.Type.valueOf(key.getString("catType"));
    }

    @Override
    public void save(DataKey key) {
        if (catType != null) key.setString("catType", catType.toString());
    }

    public Ocelot.Type getCatType() {
        return catType;
    }

    public void setCatType(Ocelot.Type catType) {
        this.catType = catType;
        if (getNPC().isSpawned()) ((Ocelot) getNPC().getEntity()).setCatType(catType);
    }
}
