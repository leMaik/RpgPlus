package de.craften.plugins.rpgplus.components.dialogs;

import org.bukkit.entity.Player;

/**
 * Handler for answers of questions by the {@link DialogComponent}.
 */
public interface AnswerHandler {
    void handleAnswer(Player player, String answer);
}
