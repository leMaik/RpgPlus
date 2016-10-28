package de.craften.plugins.rpgplus.components.entitymanager.traits;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.entity.Bat;

public class BatTrait extends Trait {
    private boolean isAwake;

    public BatTrait() {
        super("RpgPlus Bat Trait");
    }

    @Override
    public void onSpawn() {
        Bat bat = (Bat) getNPC().getEntity();
        bat.setAwake(isAwake);
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        if (key.keyExists("awake")) isAwake = key.getBoolean("awake");
    }

    @Override
    public void save(DataKey key) {
        key.setBoolean("awake", isAwake);
    }

    public boolean isAwake() {
        return isAwake;
    }

    public void setAwake(boolean awake) {
        isAwake = awake;
        if (getNPC().isSpawned()) ((Bat) getNPC().getEntity()).setAwake(awake);
    }
}
