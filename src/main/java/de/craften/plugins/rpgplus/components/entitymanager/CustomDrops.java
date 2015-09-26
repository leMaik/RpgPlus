package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An entity that drops custom items on death, based on the player who killed it.
 */
public interface CustomDrops {
    /**
     * Get the experience points to give to the player.
     *
     * @param killer player who killed this entity
     * @return experience points
     */
    int getExp(Player killer);

    /**
     * Get the drops to drop.
     *
     * @param killer player who killed this entity
     * @return drops
     */
    List<ItemStack> getDrops(Player killer);
}
