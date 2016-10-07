package de.craften.plugins.rpgplus.scripting.api.cinematic;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.cinematic.Keyframe;
import de.craften.plugins.rpgplus.components.cinematic.TrackingShot;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CinematicModule extends LuaTable implements ScriptingModule {
    private final RpgPlus plugin;

    public CinematicModule(RpgPlus plugin) {
        this.plugin = plugin;
        Luaify.convertInPlace(this);
    }

    @LuaFunction("trackingShot")
    public void trackingShot(LuaValue player, LuaTable keyframes, org.luaj.vm2.LuaFunction callback) {
        TrackingShot shot = plugin.getTrackingShots().createTrackingShot(
                ScriptUtil.getPlayer(player),
                IntStream.range(1, keyframes.length() + 1).mapToObj(i -> {
                    LuaValue frame = keyframes.get(i);
                    LuaValue location = frame.get("location").checktable();
                    return new Keyframe(
                            frame.get("time").checkdouble(),
                            location.get("x").checkdouble(),
                            location.get("y").checkdouble(),
                            location.get("z").checkdouble(),
                            location.get("yaw").optdouble(0),
                            location.get("pitch").optdouble(0)
                    );
                }).collect(Collectors.toList()),
                completed -> callback.call(LuaValue.valueOf(completed)));
        shot.start();
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        plugin.getTrackingShots().reset();
    }
}
