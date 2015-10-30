package de.craften.plugins.rpgplus.components.dialogs;

import de.craften.plugins.mcguilib.text.TextBuilder;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Component for dialogs with NPCs and mobs.
 */
public class DialogComponent extends PluginComponentBase implements Listener {
    private Map<UUID, AnswerHandler> waitForChatAnswer;

    @Override
    protected void onActivated() {
        waitForChatAnswer = new HashMap<>();
    }

    /**
     * Tell a message to a player.
     *
     * @param name    name of the character that tells the message
     * @param player  player to tell the message to
     * @param message message
     */
    public void tell(String name, Player player, String message) {
        TextBuilder
                .create("[").gold().append(name).gold().append("] ").gold()
                .append(message)
                .sendTo(player);
    }

    /**
     * Ask a player something.
     *
     * @param name     name of the character that asks
     * @param player   player to ask
     * @param question question to ask
     * @param callback callback to call when the player answered the question
     */
    public void ask(String name, Player player, String question, AnswerHandler callback) {
        tell(name, player, question);
        waitForChatAnswer.put(player.getUniqueId(), callback);
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        AnswerHandler handler = waitForChatAnswer.remove(event.getPlayer().getUniqueId());
        if (handler != null) {
            TextBuilder
                    .create("> ").gray().italic()
                    .append(ChatColor.stripColor(event.getMessage())).gray().italic()
                    .sendTo(event.getPlayer());

            handler.handleAnswer(event.getPlayer(), event.getMessage());
            event.setCancelled(true);
        }
    }
}