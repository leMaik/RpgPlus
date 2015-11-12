package de.craften.plugins.rpgplus.components.phasing;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A region that can be phased.
 */
public class PhasedRegion {
    private Map<UUID, RegionCopy> instances;
    private final RectangularRegion region;

    public PhasedRegion(RectangularRegion region) {
        instances = new HashMap<>();
        this.region = region;
    }

    public RegionCopy createPhase(RegionFinder regionFinder) {
        RectangularRegion target = regionFinder.getRegion(region.getWidth(), region.getLength());
        RegionCopy phasedRegion = new RegionCopy(this, target);
        instances.put(phasedRegion.getId(), phasedRegion);
        phasedRegion.copyBlocks();
        return phasedRegion;
    }

    public void removePhase(RegionCopy phasedRegion) {
        instances.remove(phasedRegion.getId());
        //TODO tell the RegionFinder that the space of phasedRegion is available again
    }

    /**
     * Gets the region that is covered by this phased region.
     *
     * @return covered region
     */
    public RectangularRegion getRegion() {
        return region;
    }
}
