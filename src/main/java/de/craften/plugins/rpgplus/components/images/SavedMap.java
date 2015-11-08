package de.craften.plugins.rpgplus.components.images;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SavedMap {
    private ImagesComponent component;
    private String imgName;
    private World world;
    private short id;
    private BufferedImage image;

    public SavedMap(ImagesComponent component, short id, BufferedImage img, World world) {
        this.component = component;
        this.id = id;
        this.image = img;
        this.imgName = ("map" + id);
        this.world = world;
    }

    public SavedMap(ImagesComponent component, ConfigurationSection config) {
        this.id = (short) config.getInt("id");
        this.component = component;

        this.imgName = config.getString("image");
        try {
            this.image = ImageIO.read(new File(component.getScaledImagesDirectory(), this.imgName + ".png"));
        } catch (IOException e) {
            component.getLogger().log(Level.WARNING, "Could not load map image " + imgName + ".png", e);
        }
    }

    public void saveMap() throws IOException {
        File outputfile = new File(component.getScaledImagesDirectory(), imgName + ".png");
        ImageIO.write(MapPalette.resizeImage(image), "png", outputfile);
        component.setMapConfig(imgName, getConfig());
    }

    public ConfigurationSection getConfig() {
        ConfigurationSection section = new MemoryConfiguration();
        section.set("id", id);
        section.set("world", world.getName());
        section.set("image", imgName);
        return section;
    }

    @SuppressWarnings("deprecation")
    public boolean loadMap() {
        MapView mapView = Bukkit.getMap(id);
        if (mapView != null) {
            Util.removeAllRenderers(mapView);
            mapView.addRenderer(new ImageMapRenderer(image));
            return true;
        }
        return false;
    }

    public String getName() {
        return imgName;
    }
}
