package de.craften.plugins.rpgplus.components.pathfinding.pathing;

/**
 * @author Adamki11s
 * @see <a href="https://bukkit.org/threads/lib-a-pathfinding-algorithm.129786/">Post in Bukkit forums</a>
 */
public enum PathingResult {

    SUCCESS(0),
    NO_PATH(-1),
    ITERATIONS_EXCEEDED(-2);

    private final int ec;

    PathingResult(int ec) {
        this.ec = ec;
    }

    public int getEndCode() {
        return this.ec;
    }

}
