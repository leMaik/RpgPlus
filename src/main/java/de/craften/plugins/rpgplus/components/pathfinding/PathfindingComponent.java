package de.craften.plugins.rpgplus.components.pathfinding;

import com.google.common.collect.Lists;
import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.MovementType;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.AStar;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.PathingBehaviours;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.PathingResult;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.Tile;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.material.Openable;

import java.util.*;

/**
 * A component that provides pathfinding for entities.
 */
public class PathfindingComponent extends PluginComponentBase {
    private Map<UUID, Navigator> navigators;

    @Override
    protected void onActivated() {
        navigators = new HashMap<>();

        runTaskTimer(new Runnable() {
            @Override
            public void run() {
                for (Navigator navigator : Lists.newArrayList(navigators.values())) {
                    navigator.update();
                }
            }
        }, 0, 1);
    }

    /**
     * Stats navigating the given entity to the given location. All running navigations are terminated.
     *
     * @param entity      entity to navigate
     * @param destination destination
     * @param speed       speed in ticks per block (lower is faster)
     * @param behaviours  pathing behaviours
     * @param callback    callback that is invoked when the destination is reached
     * @return pathing result
     * @throws AStar.InvalidPathException if the start or end location is in the air
     */
    public PathingResult navigate(Entity entity, Location destination, int speed, PathingBehaviours behaviours, Runnable callback) throws AStar.InvalidPathException {
        AStar astar = new AStar(entity.getLocation().subtract(0, 1, 0), destination.subtract(0, 1, 0), 200, behaviours);
        ArrayList<Tile> path = astar.iterate();
        if (path != null) {
            navigators.put(entity.getUniqueId(), new Navigator(entity, entity.getLocation(), path, speed, behaviours, callback));
        }
        return astar.getPathingResult();
    }

    private class Navigator {
        private final ManagedEntity entity;
        private final MovementType originalMovementType;
        private final Location startingLocation;
        private final ArrayList<Tile> path;
        private final int speed;
        private final PathingBehaviours behaviours;
        private Runnable callback;
        private int i = 1;
        private int subi = 0;

        public Navigator(Entity entity, Location startingLocation, ArrayList<Tile> path, int speed, PathingBehaviours behaviours, Runnable callback) {
            this.entity = RpgPlus.getPlugin(RpgPlus.class).getEntityManager().getEntity(entity);
            originalMovementType = this.entity.getMovementType();
            this.startingLocation = startingLocation;
            this.path = path;
            this.speed = speed;
            this.behaviours = behaviours;
            this.callback = callback;
            this.entity.setMovementType(MovementType.MOVING);
        }

        public void update() {
            if (i < path.size()) {
                Location next = path.get(i).getLocation(startingLocation);
                Location current = path.get(i - 1).getLocation(startingLocation);
                if (subi == 0) {
                    enterBlock(current.getBlock());
                }
                Location dest = next.subtract(current).multiply((double) subi / speed).add(current);
                if (next.getBlockY() > current.getBlockY()) {
                    dest.setY(next.getBlockY());
                } else {
                    dest.setY(subi >= speed / 2 ? next.getBlockY() : current.getBlockY());
                }
                entity.moveTo(dest.add(0.5, 0, 0.5));

                subi++;
                if (subi == speed - 1) {
                    subi = 0;
                    i++;
                    leaveBlock(current.getBlock());
                }
            } else {
                navigators.remove(entity.getEntity().getUniqueId());
                entity.setMovementType(originalMovementType);
                if (callback != null) {
                    callback.run();
                }
            }
        }

        private void enterBlock(Block block) {
            switch (block.getType()) {
                case FENCE_GATE:
                case ACACIA_FENCE_GATE:
                case BIRCH_FENCE_GATE:
                case DARK_OAK_FENCE_GATE:
                case JUNGLE_FENCE_GATE:
                case SPRUCE_FENCE_GATE:
                    if (behaviours.canOpenFenceGates()) {
                        openOpenable(block);
                    }
                    break;
                case WOODEN_DOOR:
                case ACACIA_DOOR:
                case BIRCH_DOOR:
                case DARK_OAK_DOOR:
                case JUNGLE_DOOR:
                case SPRUCE_DOOR:
                    if (behaviours.canOpenDoors()) {
                        openOpenable(block);
                    }
                    break;
            }
        }

        private void leaveBlock(Block block) {
            switch (block.getType()) {
                case FENCE_GATE:
                case ACACIA_FENCE_GATE:
                case BIRCH_FENCE_GATE:
                case DARK_OAK_FENCE_GATE:
                case JUNGLE_FENCE_GATE:
                case SPRUCE_FENCE_GATE:
                    if (behaviours.canOpenFenceGates()) {
                        closeOpenable(block, false);
                    }
                    break;
                case WOODEN_DOOR:
                case ACACIA_DOOR:
                case BIRCH_DOOR:
                case DARK_OAK_DOOR:
                case JUNGLE_DOOR:
                case SPRUCE_DOOR:
                    if (behaviours.canOpenDoors()) {
                        closeOpenable(block, false);
                    }
                    break;
            }
        }

        private Set<Block> opened = new HashSet<>(2);

        private void openOpenable(Block block) {
            BlockState state = block.getState();
            if (!((Openable) state.getData()).isOpen()) {
                ((Openable) state.getData()).setOpen(true);
                state.update();
                opened.add(block);
            }
        }

        private void closeOpenable(Block block, boolean force) {
            if (opened.remove(block) || force) {
                BlockState state = block.getState();
                ((Openable) state.getData()).setOpen(false);
                state.update();
            }
        }
    }
}