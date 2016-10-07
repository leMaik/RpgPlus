package de.craften.plugins.rpgplus.components.cinematic;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A module for tracking shots.
 */
public class TrackingShotComponent extends PluginComponentBase implements Listener {
    private Map<Player, TrackingShot> trackingShots;

    @Override
    protected void onActivated() {
        trackingShots = RpgPlus.getPlugin(RpgPlus.class).getWeakPlayerMaps().createMap(TrackingShot.class);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        stopTrackingShot(event.getPlayer());
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        stopTrackingShot(event.getPlayer());
    }

    public TrackingShot createTrackingShot(Player player, List<Keyframe> keyframes, Consumer<Boolean> callback) {
        TrackingShot trackingShot = new TrackingShot(player, keyframes, callback);
        trackingShots.put(player, trackingShot);
        return trackingShot;
    }

    public void stopTrackingShot(Player player) {
        TrackingShot trackingShot = trackingShots.remove(player);
        if (trackingShot != null) {
            trackingShot.stop();
        }
    }

    public void stopAll() {
        for (TrackingShot shot : new ArrayList<>(trackingShots.values())) {
            shot.stop();
        }
        trackingShots.clear();
    }

    public void reset() {
        stopAll();
    }
}
