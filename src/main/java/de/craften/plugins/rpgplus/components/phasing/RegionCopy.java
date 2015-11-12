package de.craften.plugins.rpgplus.components.phasing;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * An actual instance of a phased region.
 *
 * @see PhasedRegion
 */
public class RegionCopy {
    private final UUID id;
    private final PhasedRegion phasedRegion;
    private final RectangularRegion targetRegion;

    /**
     * Creates a new phases region.
     *
     * @param original     the original region
     * @param targetRegion the target region for the phased region
     */
    RegionCopy(PhasedRegion original, RectangularRegion targetRegion) {
        this.id = UUID.randomUUID();
        this.phasedRegion = original;
        this.targetRegion = targetRegion;
    }

    /**
     * Gets the unique ID of this phased region.
     *
     * @return unique ID of this phased region
     */
    public UUID getId() {
        return id;
    }

    /**
     * Puts the given player into this region.
     *
     * @param player a player
     */
    public void joinPlayer(Player player) {
        final RectangularRegion originalRegion = phasedRegion.getRegion();
        final Location playerLocation = player.getLocation();

        if (originalRegion.contains(playerLocation)) {
            double deltaX = playerLocation.getX() - originalRegion.getX();
            double deltaZ = playerLocation.getZ() - originalRegion.getZ();
            player.teleport(targetRegion.getLocation(deltaX, playerLocation.getY(), deltaZ));
        } else {
            throw new IllegalArgumentException("Player is outside the original region.");
        }
    }

    /**
     * Copies the blocks from the original region into the target region.
     */
    public void copyBlocks() {
        final RectangularRegion originalRegion = phasedRegion.getRegion();

        for (int x = 0; x < originalRegion.getWidth(); x++) {
            for (int z = 0; z < originalRegion.getLength(); z++) {
                for (int y = 0; y < originalRegion.getWorld().getMaxHeight(); y++) {
                    BlockState original = originalRegion.getBlockRelative(x, y, z).getState();
                    BlockState target = targetRegion.getBlockRelative(x, y, z).getState();
                    target.setType(original.getType());
                    target.setData(original.getData());
                    target.update(true);
                }
            }
        }
    }
}
