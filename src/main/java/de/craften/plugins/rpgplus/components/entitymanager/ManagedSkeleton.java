package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

import de.craften.plugins.rpgplus.components.entitymanager.traits.SkeletonTrait;

/**
 * A managed bat.
 */
public class ManagedSkeleton extends RpgPlusEntity<Skeleton> {
    public ManagedSkeleton(Location location) {
        super(location, EntityType.SKELETON);
        getNpc().addTrait(new SkeletonTrait());
    }
}
