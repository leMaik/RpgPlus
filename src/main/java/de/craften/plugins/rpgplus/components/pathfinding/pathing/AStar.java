package de.craften.plugins.rpgplus.components.pathfinding.pathing;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.Openable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * An A* implementation, based on code by Adamkills.
 *
 * @author Adamki11s
 * @see <a href="https://bukkit.org/threads/lib-a-pathfinding-algorithm.129786/">Post in Bukkit forums</a>
 * @see <a href="https://github.com/Adamki11s/QuestX/blob/2c3d5f0166ab53d561e01b4318289d4baa4c2f67/src/com/adamki11s/pathing/AStar.java">Improved version on GitHub</a>
 */
public class AStar {
    private final int sx, sy, sz, ex, ey, ez;
    private final World w;
    private final int maxIterations;
    private final String endUID;
    private final PathingBehaviours behaviours;

    private PathingResult result;

    private HashMap<String, Tile> open = new HashMap<>();
    private HashMap<String, Tile> closed = new HashMap<>();

    private void addToOpenList(Tile t, boolean modify) {
        if (modify || !open.containsKey(t.getUID())) {
            open.put(t.getUID(), t);
        }
    }

    private void addToClosedList(Tile t) {
        if (!closed.containsKey(t.getUID())) {
            closed.put(t.getUID(), t);
        }
    }

    public AStar(Location start, Location end, int maxIterations, PathingBehaviours behaviours) throws InvalidPathException {
        boolean s = true, e = true;

        if (!(s = this.isLocationWalkable(start)) || !(e = this.isLocationWalkable(end))) {
            throw new InvalidPathException(s, e);
        }

        this.w = start.getWorld();
        this.sx = start.getBlockX();
        this.sy = start.getBlockY();
        this.sz = start.getBlockZ();
        this.ex = end.getBlockX();
        this.ey = end.getBlockY();
        this.ez = end.getBlockZ();

        this.maxIterations = maxIterations;
        this.behaviours = behaviours;

        short sh = 0;
        Tile t = new Tile(sh, sh, sh, null);
        t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
        this.open.put(t.getUID(), t);
        this.processAdjacentTiles(t);

        this.endUID = (ex - sx) + "," + (ey - sy) + "," + (ez - sz);
    }

    public Location getEndLocation() {
        return new Location(w, ex, ey, ez);
    }

    public PathingResult getPathingResult() {
        return this.result;
    }

    public ArrayList<Tile> iterate() {
        // while not at end
        Tile current = null;

        int iterations = 0;

        while (canContinue()) {

            iterations++;

            if (iterations > this.maxIterations) {
                this.result = PathingResult.ITERATIONS_EXCEEDED;
                break;
            }

            // get lowest F cost square on open list
            current = this.getLowestFTile();

            // process tiles
            this.processAdjacentTiles(current);
        }

        assert current != null;

        if (this.result != PathingResult.SUCCESS) {
            return null;
        } else {
            // path found
            ArrayList<Tile> routeTrace = new ArrayList<>();
            Tile parent;

            routeTrace.add(current);

            while ((parent = current.getParent()) != null) {
                routeTrace.add(parent);
                current = parent;
            }

            Collections.reverse(routeTrace);

            return routeTrace;
        }
    }

    private boolean canContinue() {
        // if open list is empty, no path has been found
        if (open.isEmpty()) {
            this.result = PathingResult.NO_PATH;
            return false;
        } else {
            if (closed.containsKey(this.endUID)) {
                this.result = PathingResult.SUCCESS;
                return false;
            } else {
                return true;
            }
        }
    }

    private Tile getLowestFTile() {
        double f = 0;
        Tile drop = null;

        // get lowest F cost square
        for (Tile t : open.values()) {
            if (f == 0) {
                t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                f = t.getF();
                drop = t;
            } else {
                t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                double posF = t.getF();
                if (posF < f) {
                    f = posF;
                    drop = t;
                }
            }
        }

        // drop from open list and add to closed
        assert drop != null;
        this.open.remove(drop.getUID());
        this.addToClosedList(drop);

        return drop;
    }

    private boolean isOnClosedList(Tile t) {
        return closed.containsKey(t.getUID());
    }

    // pass in the current tile as the parent
    private void processAdjacentTiles(Tile current) {

        // set of possible walk to locations adjacent to current tile
        HashSet<Tile> possible = new HashSet<>(26);

        for (byte x = -1; x <= 1; x++) {
            for (byte y = -1; y <= 1; y++) {
                for (byte z = -1; z <= 1; z++) {

                    if (x == 0 && y == 0 && z == 0) {
                        continue;// don't check current square
                    }

                    Tile t = new Tile((short) (current.getX() + x), (short) (current.getY() + y), (short) (current.getZ() + z), current);

                    if (x != 0 && z != 0 && (y == 0 || y == 1)) {
                        // check to stop jumping through diagonal blocks
                        Tile xOff = new Tile((short) (current.getX() + x), (short) (current.getY() + y), (short) (current.getZ()), current), zOff = new Tile((short) (current.getX()),
                                (short) (current.getY() + y), (short) (current.getZ() + z), current);
                        if (!this.isTileWalkable(xOff) && !this.isTileWalkable(zOff)) {
                            continue;
                        }
                    }

                    if (this.isOnClosedList(t)) {
                        // ignore tile
                        continue;
                    }

                    // only process the tile if it can be walked on
                    if (this.isTileWalkable(t)) {
                        t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                        possible.add(t);
                    }

                }
            }
        }

        for (Tile t : possible) {
            // get the reference of the object in the array
            Tile openRef;
            if ((openRef = this.isOnOpenList(t)) == null) {
                // not on open list, so add
                this.addToOpenList(t, false);
            } else {
                // is on open list, check if path to that square is better using
                // G cost
                if (t.getG() < openRef.getG()) {
                    // if current path is better, change parent
                    openRef.setParent(current);
                    // force updates of F, G and H values.
                    openRef.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                }

            }
        }
    }

    private Tile isOnOpenList(Tile t) {
        return open.get(t.getUID());
    }

    private boolean isTileWalkable(Tile t) {
        return isLocationWalkable(new Location(w, (sx + t.getX()), (sy + t.getY()), (sz + t.getZ())));
    }

    private boolean isLocationWalkable(Location l) {
        Block b = l.getBlock();

        if (canBlockBeWalkedOn(b)) {
            // make sure the blocks above can be walked through
            return (canBlockBeWalkedThrough(b.getRelative(0, 1, 0)) && canBlockBeWalkedThrough(b.getRelative(0, 2, 0)));
        } else {
            return false;
        }
    }

    private boolean canBlockBeWalkedOn(Block block) {
        switch (block.getType()) {
            case LAVA:
            case STATIONARY_LAVA:
            case FIRE:
            case CROPS:
            case LADDER:
            case AIR:
            case FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case SPRUCE_FENCE:
            case NETHER_FENCE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return false;
            default:
                return !canBlockBeWalkedThrough(block);
        }
    }

    private boolean canBlockBeWalkedThrough(Block block) {
        switch (block.getType()) {
            case AIR:
            case SAPLING:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case LONG_GRASS:
            case DEAD_BUSH:
            case YELLOW_FLOWER:
            case RED_ROSE:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case TORCH:
            case REDSTONE_WIRE:
            case CROPS:
            case SIGN_POST:
            case WALL_SIGN:
            case RAILS:
            case LEVER:
            case STONE_PLATE:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case STONE_BUTTON:
            case WOOD_BUTTON:
            case SUGAR_CANE_BLOCK:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case NETHER_WARTS:
            case TRIPWIRE_HOOK:
            case TRIPWIRE:
            case VINE:
            case CARROT:
            case POTATO:
            case PORTAL:
            case ENDER_PORTAL:
                return true;
            case IRON_DOOR_BLOCK:
            case WOODEN_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case DARK_OAK_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
                if (block.getState().getData().getData() == 8) {
                    return canBlockBeWalkedThrough(block.getRelative(0, -1, 0));
                }
                return behaviours.canOpenDoors() || ((Openable) block.getState().getData()).isOpen();
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return behaviours.canOpenFenceGates() || ((Openable) block.getState().getData()).isOpen();
        }
        return false;
    }

    @SuppressWarnings("serial")
    public static class InvalidPathException extends Exception {
        private final boolean isStartSolid, isEndSolid;

        public InvalidPathException(boolean isStartSolid, boolean isEndSolid) {
            this.isStartSolid = isStartSolid;
            this.isEndSolid = isEndSolid;
        }

        public String getErrorReason() {
            StringBuilder sb = new StringBuilder();
            if (!isStartSolid) {
                sb.append("Start Location was air. ");
            }
            if (!isEndSolid) {
                sb.append("End Location was air.");
            }
            return sb.toString();
        }

        public boolean isStartNotSolid() {
            return (!isStartSolid);
        }

        public boolean isEndNotSolid() {
            return (!isEndSolid);
        }
    }
}