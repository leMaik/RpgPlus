package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Horse;

/**
 * A managed horse.
 */
public class ManagedHorse extends RpgPlusEntity<Horse> {
    private Horse.Color color;
    private Horse.Variant variant;
    private Horse.Style style;
    private Double jumpStrength;
    private Integer domestication;
    private Integer maxDomestication;

    public ManagedHorse(Location location) {
        super(location);
    }

    public Horse.Color getColor() {
        return color;
    }

    public void setColor(Horse.Color color) {
        this.color = color;
        if (getEntity() != null) {
            getEntity().setColor(color);
        }
    }

    public Horse.Variant getVariant() {
        return variant;
    }

    public void setVariant(Horse.Variant variant) {
        this.variant = variant;
        if (getEntity() != null) {
            getEntity().setVariant(variant);
        }
    }

    public Horse.Style getStyle() {
        return style;
    }

    public void setStyle(Horse.Style style) {
        this.style = style;
        if (getEntity() != null) {
            getEntity().setStyle(style);
        }
    }

    public Double getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpStrength(double jumpStrength) {
        this.jumpStrength = jumpStrength;
        if (getEntity() != null) {
            getEntity().setJumpStrength(jumpStrength);
        }
    }

    public Integer getDomestication() {
        return domestication;
    }

    public void setDomestication(int domestication) {
        this.domestication = domestication;
        if (getEntity() != null) {
            getEntity().setDomestication(domestication);
        }
    }

    public Integer getMaxDomestication() {
        return maxDomestication;
    }

    public void setMaxDomestication(int maxDomestication) {
        this.maxDomestication = maxDomestication;
        if (getEntity() != null) {
            getEntity().setMaxDomestication(maxDomestication);
        }
    }

    @Override
    protected Horse spawnEntity(Location location) {
        Horse horse = location.getWorld().spawn(location, Horse.class);
        if (color != null) {
            horse.setColor(color);
        } else {
            color = horse.getColor();
        }
        if (variant != null) {
            horse.setVariant(variant);
        } else {
            variant = horse.getVariant();
        }
        if (style != null) {
            horse.setStyle(style);
        } else {
            style = horse.getStyle();
        }
        if (jumpStrength != null) {
            horse.setJumpStrength(jumpStrength);
        } else {
            jumpStrength = horse.getJumpStrength();
        }
        if (maxDomestication != null) {
            horse.setMaxDomestication(maxDomestication);
        } else {
            maxDomestication = horse.getMaxDomestication();
        }
        if (domestication != null) {
            horse.setDomestication(domestication);
        } else {
            domestication = horse.getDomestication();
        }
        return horse;
    }
}
