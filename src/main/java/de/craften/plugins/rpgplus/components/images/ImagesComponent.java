package de.craften.plugins.rpgplus.components.images;

import de.craften.plugins.rpgplus.components.images.listener.ChunkListener;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

/**
 * A component for map images and posters.
 */
public class ImagesComponent extends PluginComponentBase {
    private FileConfiguration mapConfig;
    private final File mapConfigFile;
    private final File scaledImagesDirectory;
    private final File imagesDirectory;

    public ImagesComponent() {
        registerEvents(new ChunkListener(this));

        imagesDirectory = new File(getDataFolder(), "images");
        scaledImagesDirectory = new File(imagesDirectory, "scaled");
        mapConfigFile = new File(imagesDirectory, "maps.yml");
    }

    @Override
    protected void onActivated() {
        if (mapConfigFile.exists()) {
            mapConfig = YamlConfiguration.loadConfiguration(mapConfigFile);
        } else {
            mapConfig = new YamlConfiguration();
        }

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
            public void posterReady(final Poster poster, final List<ItemStack> maps) {
                try {
                    for (int x = 0; x < poster.getWidth(); x++) {
                        for (int y = 0; y < poster.getHeight(); y++) {
                            Block block = Util.getRelative(topLeftBlock, facing, -y, -x, 0);
                            ItemStack map = maps.get(y * poster.getWidth() + x);
                            ItemMeta meta = map.getItemMeta();
                            meta.setDisplayName("");
                            map.setItemMeta(meta);
                            Util.attachItemFrame(block, map, facing);
                        }
                    }
                    propagateMaps(maps, topLeftBlock.getLocation());
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

    /**
     * Starts the interactive placement of a poster.
     *
     * @param p         player that places the poster
     * @param image     image
     * @param width     poster width in blocks
     * @param height    poster height in blocks
     * @param temporary whether the poster is temporary or persistent
     */
    public void startPlacePoster(final Player p, File image, int width, int height, boolean temporary) {
        final MapHandler mapHandler = new MapHandler(p.getWorld(), image, width, height, temporary, this);
        mapHandler.setCallback(new MapHandler.Callback() {
            @Override
            public void posterReady(final Poster poster, final List<ItemStack> maps) {
                p.sendMessage(ChatColor.YELLOW + "Rightclick on a wall to place the poster.");

                registerEvents(new Listener() {
                    @EventHandler
                    public void onBlockInteract(PlayerInteractEvent event) {
                        if (event.isCancelled() || !event.hasBlock() || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                            return;
                        }

                        final Block topLeftBlock = event.getClickedBlock();
                        final BlockFace facing = event.getBlockFace();

                        try {
                            for (int x = 0; x < poster.getWidth(); x++) {
                                for (int y = 0; y < poster.getHeight(); y++) {
                                    Block block = Util.getRelative(topLeftBlock, facing, -y, -x, 0);
                                    ItemStack map = maps.get(y * poster.getWidth() + x);
                                    ItemMeta meta = map.getItemMeta();
                                    meta.setDisplayName("");
                                    map.setItemMeta(meta);
                                    Util.attachItemFrame(block, map, facing);
                                }
                            }
                            propagateMaps(maps, topLeftBlock.getLocation());
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
                    ((Player) entity).sendMap(Bukkit.getServer().getMap(map.getDurability()));
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
}
