package de.craften.plugins.rpgplus.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Slime;

public class EntityUtil {

    public static final float NAME_TAG_HEIGHT = 0.2f;
    
    public static double getEntityHeight(Entity entity){
        
        switch (entity.getType()) {
        case ARMOR_STAND:
            return 1.8;
        case ARROW:
            return 0.5;
        case BAT:
            return 0.9;
        case BLAZE:
            return 1.8;
        case BOAT:
            return 0.6;
        case CAVE_SPIDER:
            return 0.5;
        case CHICKEN:
            return 0.7;
        case COMPLEX_PART:
            break;
        case COW:
            return 1.3;
        case CREEPER:
            return 1.8;
        case DROPPED_ITEM:
            return 0.25;
        case EGG:
            return 0.25;
        case ENDERMAN:
            return 2.9;
        case ENDERMITE:
            return 0.3;
        case ENDER_CRYSTAL:
            return 2;
        case ENDER_DRAGON:
            return 8.0;
        case ENDER_PEARL:
            return 0.25;
        case ENDER_SIGNAL:
            return 0.25;
        case EXPERIENCE_ORB:
            return 0.25;
        case FALLING_BLOCK:
            return 0.98;
        case FIREBALL:
            return 1;
        case FIREWORK:
            return 0.25;
        case FISHING_HOOK:
            return 0.25;
        case GHAST:
            return 4;
        case GIANT:
            break;
        case GUARDIAN:
            return 10.8;
        case HORSE:
            return 1.6;
        case IRON_GOLEM:
            return 2.9;
        case ITEM_FRAME:
            return 0.8;
        case LEASH_HITCH:
            break;
        case LIGHTNING:
            return 1.8;
        case MAGMA_CUBE:
            MagmaCube mc = (MagmaCube)entity;
            return mc.getSize()*0.6;
        case MINECART:
            return 0.7;
        case MINECART_CHEST:
            return 0.7;
        case MINECART_COMMAND:
            return 0.7;
        case MINECART_FURNACE:
            return 0.7;
        case MINECART_HOPPER:
            return 0.7;
        case MINECART_MOB_SPAWNER:
            return 0.7;
        case MINECART_TNT:
            return 0.7;
        case MUSHROOM_COW:
            return 1.3;
        case OCELOT:
            return 0.8;
        case PAINTING:
            break;
        case PIG:
            return 0.9;
        case PIG_ZOMBIE:
            return 1.8;
        case PLAYER:
            return 1.8;
        case PRIMED_TNT:
            return 0.98;
        case RABBIT:
            break;
        case SHEEP:
            return 1.3;
        case SILVERFISH:
            return 0.7;
        case SKELETON:
            return 1.8;
        case SLIME:
            Slime s = (Slime)entity;
            return s.getSize()*0.6;
        case SMALL_FIREBALL:
            return 0.3125;
        case SNOWBALL:
            return 0.25;
        case SNOWMAN:
            return 1.8;
        case SPIDER:
            return 0.9;
        case SPLASH_POTION:
            return 0.25;
        case SQUID:
            return 0.95;
        case THROWN_EXP_BOTTLE:
            return 0.25;
        case UNKNOWN:
            return 1.8;
        case VILLAGER:
            return 1.8;
        case WEATHER:
            return 0;
        case WITCH:
            return 1.8;
        case WITHER:
            return 4;
        case WITHER_SKULL:
            return 0.3125;
        case WOLF:
            return 0.8;
        case ZOMBIE:
            return 1.8;
        default:
            break;
        
        }
        return 0;
    }
    
}
