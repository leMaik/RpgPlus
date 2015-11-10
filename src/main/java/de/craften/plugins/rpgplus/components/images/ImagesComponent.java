package de.craften.plugins.rpgplus.components.images;

import de.craften.plugins.rpgplus.components.images.listener.ChunkListener;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

/**
 * A component for map images and posters.
 */
public class ImagesComponent extends PluginComponentBase {
    private FileConfiguration mapConfig;
    private File mapConfigFile;
    private File scaledImagesDirectory;
    private File imagesDirectory;

    @Override
    protected void onActivated() {
        imagesDirectory = new File(getDataFolder(), "images");
        scaledImagesDirectory = new File(imagesDirectory, "scaled");
        mapConfigFile = new File(imagesDirectory, "maps.yml");

        if (mapConfigFile.exists()) {
            mapConfig = YamlConfiguration.loadConfiguration(mapConfigFile);
        } else {
            mapConfig = new YamlConfiguration();
        }

        registerEvents(new ChunkListener(this));

        for (String key : mapConfig.getKeys(false)) {
            new SavedMap(this, mapConfig.getConfigurationSection(key)).loadMap();
        }
    }

    /**
     * Places a poster starting at the given block on the given side.
     *
     * @param image        image
     * @param width        poster width in blocks
     * @param height       poster height in blocks
     * @param topLeftBlock the block that the top left side of the poster will be attached to
     * @param facing       the block face the poster will be attached to
     * @param temporary    whether the poster is temporary or persistent
     */
    public void placePoster(File image, int width, int height, final Block topLeftBlock, final BlockFace facing, boolean temporary) {
        final MapHandler mapHandler = new MapHandler(topLeftBlock.getWorld(), image, width, height, temporary, this);
        mapHandler.setCallback(new MapHandler.Callback() {
            @Override
            public void posterReady(final PosterImages poster, final List<ItemStack> maps) {
                try {
                    placePoster(maps, poster.getWidth(), poster.getHeight(), topLeftBlock, facing);
                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "Attaching the poster failed", e);
                }
            }

            @Override
            public void posterFailed(Throwable exception) {
                getLogger().log(Level.SEVERE, "Creating the poster failed", exception);
            }
        });
        mapHandler.run();
    }

    private void placePoster(List<ItemStack> maps, int width, int height, final Block topLeftBlock, final BlockFace facing) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Block block = Util.getRelative(topLeftBlock, facing, -y, x, 0);
                for (ItemFrame entity : block.getWorld().getEntitiesByClass(ItemFrame.class)) {
                    if (entity.getLocation().getBlock().getRelative(entity.getAttachedFace()).getLocation().equals(block.getLocation())) {
                        entity.teleport(new Location(entity.getWorld(), 0, -1, 0)); //workaround so that respawning an item frame immediately works
                        entity.remove();
                        break;
                    }
                }

                final ItemStack map = maps.get(y * width + x);
                ItemMeta meta = map.getItemMeta();
                meta.setDisplayName("");
                map.setItemMeta(meta);
                Util.attachItemFrame(Util.getRelative(topLeftBlock, facing, -y, x, 0), map, facing);
            }
        }
        propagateMaps(maps, topLeftBlock.getLocation());
    }

    public void placePoster(Poster poster, Block topLeftBlock, BlockFace facing) {
        placePoster(poster.createItemStacks(), poster.getWidth(), poster.getHeight(), topLeftBlock, facing);
    }

    /**
     * Asynchronously creates a poster and calls the callback when finished.
     *
     * @param image     image
     * @param width     width in blocks
     * @param height    height in blocks
     * @param world     world the poster will be created in
     * @param temporary whether the poster is temporary or persistent
     * @param callback  callback that is invoked when the poster is placed
     */
    public void createPoster(File image, final int width, final int height, World world, boolean temporary, final PosterCallback callback) {
        MapHandler mapHandler = new MapHandler(world, image, width, height, temporary, this);

        mapHandler.setCallback(new MapHandler.Callback() {
            @Override
            public void posterReady(PosterImages poster, List<ItemStack> maps) {
                callback.posterCreated(new Poster(width, height, maps));
            }

            @Override
            public void posterFailed(Throwable exception) {
                callback.creationFailed(exception);
            }
        });
        mapHandler.run();
    }

    /**
     * Starts the interactive placement of a poster.
     *
     * @param p         player that places the poster
     * @param image     image
     * @param width     poster width in blocks
     * @param height    poster height in blocks
     * @param temporary whether the poster is temporary or persistent
     */
    public void startPlacePoster(final Player p, File image, final int width, final int height, boolean temporary) {
        final MapHandler mapHandler = new MapHandler(p.getWorld(), image, width, height, temporary, this);
        mapHandler.setCallback(new MapHandler.Callback() {
            @Override
            public void posterReady(final PosterImages poster, final List<ItemStack> maps) {
                p.sendMessage(ChatColor.YELLOW + "Rightclick on a wall to place the poster.");

                registerEvents(new Listener() {
                    @EventHandler
                    public void onBlockInteract(PlayerInteractEvent event) {
                        if (event.isCancelled() || !event.hasBlock() || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                            return;
                        }

                        Block topLeftBlock = event.getClickedBlock();
                        BlockFace facing = event.getBlockFace();
                        try {
                            placePoster(maps, width, height, topLeftBlock, facing);
                        } catch (Exception e) {
                            p.sendMessage(ChatColor.RED + "Attaching the poster failed.");
                            getLogger().log(Level.SEVERE, "Attaching the poster failed", e);
                        }
                        event.setCancelled(true);
                        HandlerList.unregisterAll(this);
                    }

                    @EventHandler
                    public void onPlayerQuit(PlayerQuitEvent e) {
                        HandlerList.unregisterAll(this);
                    }
                });
            }

            @Override
            public void posterFailed(Throwable exception) {
                p.sendMessage(ChatColor.RED + "Creating the poster failed.");
                getLogger().log(Level.SEVERE, "Creating the poster failed", exception);
            }
        });
        mapHandler.run();
    }

    /**
     * Sends the map to players near the given location.
     *
     * @param maps     maps to send
     * @param location center of the area of players to send the maps to
     */
    private void propagateMaps(List<ItemStack> maps, Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 16, 16, 16)) {
            if (entity instanceof Player) {
                for (ItemStack map : maps) {
                    @SuppressWarnings("deprecation")
                    MapView mapView = Bukkit.getServer().getMap(map.getDurability());
                    ((Player) entity).sendMap(mapView);
                }
            }
        }
    }

    void setMapConfig(String mapName, ConfigurationSection config) {
        mapConfig.set(mapName, config);
    }

    File getScaledImagesDirectory() {
        return scaledImagesDirectory;
    }

    public interface PosterCallback {
        void posterCreated(Poster poster);

        void creationFailed(Throwable exception);
    }
}
