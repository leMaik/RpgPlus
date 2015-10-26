package de.craften.plugins.rpgplus.components.pathfinding.pathing;

/**
 * A list of behaviours for pathing.
 */
public class PathingBehaviours {
    public static PathingBehaviours DEFAULT = new PathingBehaviours(false, false);
    private boolean canOpenDoors;
    private boolean canOpenFenceGates;

    private PathingBehaviours() {
    }

    private PathingBehaviours(boolean canOpenDoors, boolean canOpenFenceGates) {
        this.canOpenDoors = canOpenDoors;
        this.canOpenFenceGates = canOpenFenceGates;
    }

    public boolean canOpenDoors() {
        return canOpenDoors;
    }

    public boolean canOpenFenceGates() {
        return canOpenFenceGates;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PathingBehaviours behaviours = new PathingBehaviours();

        private Builder() {
        }

        public Builder openDoors(boolean openDoors) {
            behaviours.canOpenDoors = openDoors;
            return this;
        }

        public Builder openFenceGates(boolean openFenceGates) {
            behaviours.canOpenFenceGates = openFenceGates;
            return this;
        }

        public PathingBehaviours build() {
            return behaviours;
        }
    }
}
