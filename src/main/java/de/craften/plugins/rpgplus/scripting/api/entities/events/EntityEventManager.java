package de.craften.plugins.rpgplus.scripting.api.entities.events;

import de.craften.plugins.rpgplus.scripting.util.SafeInvoker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.vehicle.*;

/**
 * An event manager for entity events.
 */
public class EntityEventManager extends EntityEventManagerImpl implements Listener {
    public EntityEventManager(SafeInvoker invoker) {
        super(invoker);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        callHandlers("player.interact.atEntity", event, event.getRightClicked());
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        callHandlers("player.interact.entity", event, event.getRightClicked());
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        callHandlers("player.shearEntity", event, event.getEntity());
    }

    @EventHandler
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        callHandlers("player.unleashEntity", event);
    }


    @EventHandler
    public void onBlockEntityForm(EntityBlockFormEvent event) {
        callHandlers("block.form.entity", event, event.getEntity());
    }

    @EventHandler
    public void onCreeperPower(CreeperPowerEvent event) {
        callHandlers("entity.creeperpower", event);
    }

    @EventHandler
    public void onEntityBreakDoor(EntityBreakDoorEvent event) {
        callHandlers("entity.breakdoor", event);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        callHandlers("entity.changeblock", event);
    }

    @EventHandler
    public void onEntityCombustByBlock(EntityCombustByBlockEvent event) {
        callHandlers("entity.combust.byblock", event);
    }

    @EventHandler
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        callHandlers("entity.combust.byentity", event);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        callHandlers("entity.spawn", event);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        callHandlers("entity.combust", event);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        callHandlers("entity.attack.entity", event, event.getDamager());
        callHandlers("entity.damage.byEntity", event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        callHandlers("entity.damage", event);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        callHandlers("entity.damage.byBlock", event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        callHandlers("entity.death", event);
    }

    @EventHandler
    public void onFoodLevelChanges(FoodLevelChangeEvent event) {
        callHandlers("entity.foodLevelChange", event);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        callHandlers("item.spawn", event);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        callHandlers("item.despawn", event);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        callHandlers("projectile.launch", event);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        callHandlers("projectile.hit", event);
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        callHandlers("entity.teleport", event);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        callHandlers("entity.target", event);
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        callHandlers("entity.target.livingentity", event);
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event) {
        callHandlers("entity.tame", event);
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        callHandlers("entity.interact", event);
    }

    @EventHandler
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        callHandlers("entity.portal.enter", event);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        callHandlers("entity.portal", event);
    }

    @EventHandler
    public void onEntityExplosionPrime(ExplosionPrimeEvent event) {
        callHandlers("entity.explosion.prime", event);
    }

    @EventHandler
    public void onEntityPortalExit(EntityPortalExitEvent event) {
        callHandlers("entity.portal.exit", event);
    }

    @EventHandler
    public void onEntityPortalCreate(EntityCreatePortalEvent event) {
        callHandlers("entity.portal.create", event);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        callHandlers("entity.health.regain", event);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        callHandlers("entity.explode", event);
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        callHandlers("player.leashEntity", event, event.getEntity());
    }

    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent event) {
        callHandlers("entity.unleash", event);
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        callHandlers("entity.shootbow", event);
    }

    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        callHandlers("entity.expBottle", event);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        callHandlers("entity.potionSplash", event);
    }

    @EventHandler
    public void onHorseJump(HorseJumpEvent event) {
        callHandlers("entity.horseJump", event);
    }

    @EventHandler
    public void onSheepRegrowWool(SheepRegrowWoolEvent event) {
        callHandlers("entity.sheep.regrowwool", event);
    }

    @EventHandler
    public void onSheepDyeWool(SheepDyeWoolEvent event) {
        callHandlers("entity.sheep.dyewool", event);
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        callHandlers("entity.slime.split", event);
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        callHandlers("vehicle.damage", event, event.getAttacker());
    }

    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        callHandlers("vehicle.destroy", event, event.getAttacker());
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        callHandlers("vehicle.enter", event, event.getEntered());
    }

    @EventHandler
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        callHandlers("vehicle.collision.entity", event, event.getEntity());
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        callHandlers("vehicle.exit", event, event.getExited());
    }
}
