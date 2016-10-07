package de.craften.plugins.rpgplus.components.cinematic;

import de.craften.plugins.rpgplus.RpgPlus;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A tracking shot.
 */
public class TrackingShot {
    private static final double START_DURATION = 2;
    private static final double STOP_DURATION = 1;
    private final Player player;
    private final List<Keyframe> keyframes;
    private final Consumer<Boolean> onCompleted;
    private GameMode initialGamemode;
    private Location initialLocation;
    private PreviewPlayer cameraMoving;

    public TrackingShot(Player player, List<Keyframe> keyframes, Consumer<Boolean> onCompleted) {
        this.player = player;
        this.keyframes = keyframes;
        this.onCompleted = onCompleted;
    }

    public void start() {
        initialGamemode = player.getGameMode();
        initialLocation = player.getLocation();

        List<Keyframe> keyframes = new ArrayList<>(this.keyframes.size() + 2);
        keyframes.add(new Keyframe(0, initialLocation));
        this.keyframes.stream().map(k -> k.shift(START_DURATION)).forEach(keyframes::add);
        keyframes.add(new Keyframe(this.keyframes.get(this.keyframes.size() - 1).getTime() + START_DURATION + STOP_DURATION, initialLocation));
        InterpolatedFrames frames = new InterpolatedFrames(keyframes);
        player.setGameMode(GameMode.SPECTATOR);
        cameraMoving = new PreviewPlayer(frames);
        cameraMoving.runTaskTimer(RpgPlus.getPlugin(RpgPlus.class), 0, 1);
    }

    void stop() {
        if (cameraMoving != null) {
            cameraMoving.cancel();
        }
        if (player.getWorld().equals(initialLocation.getWorld())) {
            player.setGameMode(initialGamemode);
            player.teleport(initialLocation);
        }
        onCompleted.accept(cameraMoving.isDone());
        cameraMoving = null;
    }

    private class PreviewPlayer extends BukkitRunnable {
        private final InterpolatedFrames keyframes;
        private double t = 0;

        PreviewPlayer(InterpolatedFrames keyframes) {
            this.keyframes = keyframes;
        }

        @Override
        public void run() {
            if (t <= keyframes.getLength()) {
                player.teleport(new Location(player.getWorld(),
                        keyframes.getX(t),
                        keyframes.getY(t),
                        keyframes.getZ(t),
                        (float) keyframes.getYaw(t),
                        (float) keyframes.getPitch(t)));
                t += 0.1;
            } else {
                stop();
            }
        }

        public boolean isDone() {
            return t >= keyframes.getLength();
        }
    }
}
