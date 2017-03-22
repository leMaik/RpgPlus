package de.craften.plugins.rpgplus.components.entitymanager.traits;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.entity.Horse;

public class HorseTrait extends Trait {
    private Horse.Color color;
    private Horse.Variant variant;
    private Horse.Style style;
    private Double jumpStrength;
    private Integer domestication;
    private Integer maxDomestication;

    public HorseTrait() {
        super("RpgPlus Horse Trait");
    }

    @Override
    public void onSpawn() {
        Horse horse = (Horse) getNPC().getEntity();
        if (color != null) horse.setColor(color);
        if (variant != null) horse.setVariant(variant);
        if (style != null) horse.setStyle(style);
        if (jumpStrength != null) horse.setJumpStrength(jumpStrength);
        if (domestication != null) horse.setDomestication(domestication);
        if (maxDomestication != null) horse.setMaxDomestication(maxDomestication);
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        if (key.keyExists("color")) color = Horse.Color.valueOf(key.getString("color"));
        if (key.keyExists("variant")) variant = Horse.Variant.valueOf(key.getString("variant"));
        if (key.keyExists("style")) style = Horse.Style.valueOf(key.getString("style"));
        if (key.keyExists("jumpStrength")) jumpStrength = key.getDouble("jumpStrength");
        if (key.keyExists("domestication")) domestication = key.getInt("domestication");
        if (key.keyExists("maxDomestication")) maxDomestication = key.getInt("maxDomestication");
    }

    @Override
    public void save(DataKey key) {
        if (color != null) key.setString("color", color.toString());
        if (variant != null) key.setString("variant", variant.toString());
        if (style != null) key.setString("style", style.toString());
        if (jumpStrength != null) key.setDouble("jumpStrength", jumpStrength);
        if (domestication != null) key.setInt("domestication", domestication);
        if (maxDomestication != null) key.setInt("maxDomestication", maxDomestication);
    }

    public Horse.Color getColor() {
        return color;
    }

    public void setColor(Horse.Color color) {
        this.color = color;
        if (getNPC().isSpawned()) ((Horse) getNPC().getEntity()).setColor(color);
    }

    public Horse.Variant getVariant() {
        return variant;
    }

    public void setVariant(Horse.Variant variant) {
        this.variant = variant;
        if (getNPC().isSpawned()) ((Horse) getNPC().getEntity()).setVariant(variant);
    }

    public Horse.Style getStyle() {
        return style;
    }

    public void setStyle(Horse.Style style) {
        this.style = style;
        if (getNPC().isSpawned()) ((Horse) getNPC().getEntity()).setStyle(style);
    }

    public Double getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpStrength(Double jumpStrength) {
        this.jumpStrength = jumpStrength;
        if (getNPC().isSpawned()) ((Horse) getNPC().getEntity()).setJumpStrength(jumpStrength);
    }

    public Integer getDomestication() {
        return domestication;
    }

    public void setDomestication(Integer domestication) {
        this.domestication = domestication;
        if (getNPC().isSpawned()) ((Horse) getNPC().getEntity()).setDomestication(domestication);
    }

    public Integer getMaxDomestication() {
        return maxDomestication;
    }

    public void setMaxDomestication(Integer maxDomestication) {
        this.maxDomestication = maxDomestication;
        if (getNPC().isSpawned()) ((Horse) getNPC().getEntity()).setMaxDomestication(maxDomestication);
    }
}
