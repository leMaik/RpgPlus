package de.craften.plugins.rpgplus.components.dialogs;

import org.bukkit.entity.Player;

/**
 * Handler for answers of choice questions.
 *
 * @see DialogComponent
 */
public interface ChoiceAnswerHandler {
    /**
     * Invoked when the player selected an answer.
     *
     * @param player player
     * @param index  zero-based index of the answer
     * @param answer the answer as string
     * @return true if the answer was valid, false if not
     */
    boolean handleAnswer(Player player, int index, String answer);
}
