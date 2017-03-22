package de.craften.plugins.rpgplus.components.entitymanager.traits;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.entity.Rabbit;

public class RabbitTrait extends Trait {
    private Rabbit.Type rabbitType;

    public RabbitTrait() {
        super("RpgPlus Rabbit Trait");
    }

    @Override
    public void onSpawn() {
        Rabbit rabbit = (Rabbit) getNPC().getEntity();
        if (rabbitType != null) rabbit.setRabbitType(rabbitType);
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        if (key.keyExists("rabbitType")) rabbitType = Rabbit.Type.valueOf(key.getString("rabbitType"));
    }

    @Override
    public void save(DataKey key) {
        if (rabbitType != null) key.setString("rabbitType", rabbitType.toString());
    }

    public Rabbit.Type getRabbitType() {
        return rabbitType;
    }

    public void setRabbitType(Rabbit.Type rabbitType) {
        this.rabbitType = rabbitType;
        if (getNPC().isSpawned()) ((Rabbit) getNPC().getEntity()).setRabbitType(rabbitType);
    }
}
