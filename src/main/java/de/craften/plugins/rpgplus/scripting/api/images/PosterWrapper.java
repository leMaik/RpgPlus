package de.craften.plugins.rpgplus.scripting.api.images;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.images.ImagesComponent;
import de.craften.plugins.rpgplus.components.images.Poster;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A lua wrapper for a {@link Poster}.
 */
public class PosterWrapper extends LuaTable {
    private final ImagesComponent imagesComponent;
    private final Map<World, Poster> posters = new HashMap<>(1);
    private final int width;
    private final int height;
    private File image;

    public PosterWrapper(File image, int width, int height, final ImagesComponent imagesComponent) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.imagesComponent = imagesComponent;

        set("giveTo", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue playerArg, final LuaValue callback) {
                final Player player = ScriptUtil.getPlayer(playerArg);

                getPosterForWorld(player.getWorld(), new ImagesComponent.PosterCallback() {
                    @Override
                    public void posterCreated(Poster poster) {
                        PlayerInventory inventory = player.getInventory();
                        for (ItemStack itemStack : poster.createItemStacks()) {
                            inventory.addItem(itemStack);
                        }
                        if (!callback.isnil()) {
                            RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(callback.checkfunction(), LuaValue.TRUE);
                        }
                    }

                    @Override
                    public void creationFailed(Throwable exception) {
                        if (!callback.isnil()) {
                            RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(callback.checkfunction(), LuaValue.FALSE);
                        }
                    }
                });
                return LuaValue.NIL;
            }
        });

        set("attach", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, final LuaValue location, final LuaValue callback) {
                final Block block = ScriptUtil.getLocation(location.checktable()).getBlock();

                getPosterForWorld(block.getWorld(), new ImagesComponent.PosterCallback() {
                    @Override
                    public void posterCreated(Poster poster) {
                        imagesComponent.placePoster(poster,
                                block,
                                BlockFace.valueOf(location.checktable().get("face").checkjstring().toUpperCase()));
                        if (!callback.isnil()) {
                            RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(callback.checkfunction(), LuaValue.TRUE);
                        }
                    }

                    @Override
                    public void creationFailed(Throwable exception) {
                        if (!callback.isnil()) {
                            RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(callback.checkfunction(), LuaValue.FALSE);
                        }
                    }
                });
                return LuaValue.NIL;
            }
        });
    }

    private synchronized void getPosterForWorld(final World world, final ImagesComponent.PosterCallback callback) {
        Poster poster = posters.get(world);
        if (poster != null) {
            callback.posterCreated(poster);
        }
        imagesComponent.createPoster(image, width, height, world, true, new ImagesComponent.PosterCallback() {
            @Override
            public void posterCreated(Poster poster) {
                posters.put(world, poster);
                image = null;
                callback.posterCreated(poster);
            }

            @Override
            public void creationFailed(Throwable exception) {
                callback.creationFailed(exception);
            }
        });
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "width":
                    return LuaValue.valueOf(width);
                case "height":
                    return LuaValue.valueOf(height);
            }
        }
        return super.rawget(key);
    }
}
