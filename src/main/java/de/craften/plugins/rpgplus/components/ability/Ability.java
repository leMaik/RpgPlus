package de.craften.plugins.rpgplus.components.ability;

import org.bukkit.entity.Player;

public interface Ability {

	String getIdentifier();
	String getDisplayName();
	default void giveTo(Player player) { }
	default void removeFrom(Player player) { }
	default void onActivated(Player player) { }
	default void onExpired(Player player) { }
}
