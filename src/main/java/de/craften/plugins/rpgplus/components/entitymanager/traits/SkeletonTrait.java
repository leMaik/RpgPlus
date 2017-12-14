package de.craften.plugins.rpgplus.components.entitymanager.traits;

import net.citizensnpcs.api.trait.Trait;

import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

public class SkeletonTrait extends Trait {
    private boolean isWitherSkeleton;

    public SkeletonTrait() {
        super("RpgPlus Skeleton Trait");
    }

    @Override
    public void onSpawn() {
        Skeleton skeleton = (Skeleton) getNPC().getEntity();
        skeleton.setSkeletonType(isWitherSkeleton ? SkeletonType.WITHER : SkeletonType.NORMAL);
    }

    public boolean isWitherSkeleton() {
        return isWitherSkeleton;
    }

    public void setWitherSkeleton(boolean isWitherSkeleton) {
    	this.isWitherSkeleton = isWitherSkeleton;
        if (getNPC().isSpawned()) ((Skeleton) getNPC().getEntity()).setSkeletonType(isWitherSkeleton ? SkeletonType.WITHER : SkeletonType.NORMAL);
    }
}
