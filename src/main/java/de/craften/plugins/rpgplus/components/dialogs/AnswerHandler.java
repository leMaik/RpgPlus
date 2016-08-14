package de.craften.plugins.rpgplus.components.dialogs;

import org.bukkit.entity.Player;

/**
 * Handler for answers of questions.
 *
 * @see DialogComponent
 */
public interface AnswerHandler {
    /**
     * Invoked when the player gave an answer.
     *
     * @param player player
     * @param answer answer
     * @return true if the answer was valid, false if not
     */
    boolean handleAnswer(Player player, String answer);
}
