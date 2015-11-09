package de.craften.plugins.rpgplus.components.images;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapHandler implements Runnable {
    private final List<ItemStack> renderedMaps;
    private final World world;
    private final ImagesComponent component;
    private final File image;
    private final int width;
    private final int height;
    private final boolean temporary;
    private Callback callback;

    public MapHandler(World world, File image, int width, int height, boolean temporary, ImagesComponent component) {
        this.renderedMaps = new ArrayList<>(width * height);
        this.world = world;
        this.component = component;
        this.image = image;
        this.width = width;
        this.height = height;
        this.temporary = temporary;
    }

    /**
     * Asynchronously creates the images for the maps, then synchronously creates the map items and calls the callback.
     */
    public void run() {
        component.runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                final Poster poster;
                try {
                    poster = createPoster();
                } catch (IOException e) {
                    if (callback != null) {
                        callback.posterFailed(new Exception("Creating the poster failed", e));
                    }
                    return;
                }

                final BufferedImage[] images = poster.getImages();

                component.scheduleSyncDelayedTask(new Runnable() {
                    @Override
                    @SuppressWarnings("deprecation")
                    public void run() {
                        ItemStack map;
                        for (BufferedImage image : images) {
                            MapView mapView;

                            mapView = component.getServer().createMap(world);

                            Util.removeAllRenderers(mapView);
                            mapView.addRenderer(new ImageMapRenderer(image));
                            map = new ItemStack(Material.MAP, 1, mapView.getId());

                            renderedMaps.add(map);

                            if (!temporary) {
                                try {
                                    new SavedMap(component, mapView.getId(), image, world).saveMap();
                                } catch (IOException e) {
                                    callback.posterFailed(new Exception("Could not save the poster", e));
                                }
                            }
                        }

                        if (callback != null) {
                            callback.posterReady(poster, renderedMaps);
                        }
                    }
                });
            }
        });
    }

    public Poster createPoster() throws IOException {
        return new Poster(ImageIO.read(image), width, height);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void posterReady(Poster poster, List<ItemStack> maps);

        void posterFailed(Throwable exception);
    }
}
