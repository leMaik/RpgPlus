package de.craften.plugins.rpgplus.components.pathfinding.pathing;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.TrapDoor;

/**
 * Helper methods for doors.
 *
 * @author nisovin
 * @see <a href="http://bukkit.org/threads/solved-how-to-set-a-door-to-closed.91596/#post-1265976">Bukkit Forums post</a>
 */
public class DoorUtil {
    public static boolean isDoorClosed(Block block) {
        if (block.getType() == Material.TRAP_DOOR) {
            TrapDoor trapdoor = (TrapDoor) block.getState().getData();
            return !trapdoor.isOpen();
        } else {
            byte data = block.getData();
            if ((data & 0x8) == 0x8) {
                block = block.getRelative(BlockFace.DOWN);
                data = block.getData();
            }
            return ((data & 0x4) == 0);
        }
    }

    public static void openDoor(Block block) {
        if (block.getType() == Material.TRAP_DOOR) {
            BlockState state = block.getState();
            TrapDoor trapdoor = (TrapDoor) state.getData();
            trapdoor.setOpen(true);
            state.update();
        } else {
            byte data = block.getData();
            if ((data & 0x8) == 0x8) {
                block = block.getRelative(BlockFace.DOWN);
                data = block.getData();
            }
            if (isDoorClosed(block)) {
                data = (byte) (data | 0x4);
                block.setData(data, true);
                block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
            }
        }
    }

    public static void closeDoor(Block block) {
        if (block.getType() == Material.TRAP_DOOR) {
            BlockState state = block.getState();
            TrapDoor trapdoor = (TrapDoor) state.getData();
            trapdoor.setOpen(false);
            state.update();
        } else {
            byte data = block.getData();
            if ((data & 0x8) == 0x8) {
                block = block.getRelative(BlockFace.DOWN);
                data = block.getData();
            }
            if (!isDoorClosed(block)) {
                data = (byte) (data & 0xb);
                block.setData(data, true);
                block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
            }
        }
    }
}
