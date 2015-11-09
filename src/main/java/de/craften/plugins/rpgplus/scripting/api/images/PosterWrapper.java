package de.craften.plugins.rpgplus.scripting.api.images;

import de.craften.plugins.rpgplus.components.images.ImagesComponent;
import de.craften.plugins.rpgplus.components.images.Poster;
import de.craften.plugins.rpgplus.scripting.api.Inventory;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * A lua wrapper for a {@link Poster}.
 */
public class PosterWrapper extends LuaTable {
    private final Poster poster;
    private final ImagesComponent imagesComponent;

    public PosterWrapper(final Poster poster, final ImagesComponent imagesComponent) {
        this.poster = poster;
        this.imagesComponent = imagesComponent;

        set("giveTo", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue player) {
                PlayerInventory inventory = ScriptUtil.getPlayer(player).getInventory();
                for (ItemStack itemStack : PosterWrapper.this.poster.createItemStacks()) {
                    inventory.addItem(itemStack);
                }
                return LuaValue.NIL;
            }
        });

        set("attach", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue, LuaValue location) {
                imagesComponent.placePoster(poster,
                        ScriptUtil.getLocation(location.checktable()).getBlock(),
                        BlockFace.valueOf(location.checktable().get("face").checkjstring()));
                return LuaValue.NIL;
            }
        });
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "width":
                    return LuaValue.valueOf(poster.getWidth());
                case "height":
                    return LuaValue.valueOf(poster.getHeight());
            }
        }
        return super.rawget(key);
    }
}
