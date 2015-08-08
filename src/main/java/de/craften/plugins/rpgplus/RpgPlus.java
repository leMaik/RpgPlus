package de.craften.plugins.rpgplus;

import de.craften.plugins.rpgplus.components.entitymanager.EntityManager;
import de.craften.plugins.rpgplus.components.villagerdialogs.VillagerDialogs;
import de.craften.plugins.rpgplus.components.welcome.WelcomeComponent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class of RpgPlus.
 */
public class RpgPlus extends JavaPlugin {
    private EntityManager entityManager;

    @Override
    public void onEnable() {
        new WelcomeComponent().activateFor(this);
        new VillagerDialogs().activateFor(this);

        entityManager = new EntityManager();
        entityManager.activateFor(this);
    }

    /**
     * Get the entity manager.
     *
     * @return the entity manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
