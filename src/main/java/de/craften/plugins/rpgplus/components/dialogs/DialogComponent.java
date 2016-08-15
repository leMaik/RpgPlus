package de.craften.plugins.rpgplus.components.dialogs;

import de.craften.plugins.mcguilib.text.TextBuilder;
import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

/**
 * Component for dialogs with NPCs and mobs.
 */
public class DialogComponent extends PluginComponentBase implements Listener {
    private Map<Player, AnswerHandler> waitForChatAnswer;

    @Override
    protected void onActivated() {
        waitForChatAnswer = RpgPlus.getPlugin(RpgPlus.class).getWeakPlayerMaps().createMap(AnswerHandler.class);
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
    public void ask(String name, Player player, String question, final AnswerHandler callback) {
        tell(name, player, question);
        waitForChatAnswer.put(player, new AnswerHandler() {
            @Override
            public boolean handleAnswer(Player player, String answer) {
                TextBuilder
                        .create("> ").gray().italic()
                        .append(ChatColor.stripColor(answer)).gray().italic()
                        .sendTo(player);
                return callback.handleAnswer(player, answer);
            }
        });
    }

    /**
     * Ask a player a choice question. The player can answer by either clicking on a choice or by writing the index of the choice into the chat.
     *
     * @param name     name of the character that asks
     * @param player   player to ask
     * @param question question to ask
     * @param choices  possible answers
     * @param callback callback to call when the player answered the question
     */
    public void askChoices(String name, Player player, String question, final String[] choices, final ChoiceAnswerHandler callback) {
        tell(name, player, question);
        for (int i = 0; i < choices.length; i++) {
            TextComponent component = new TextComponent("[" + (i + 1) + "] ");
            component.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            TextComponent answer = new TextComponent(TextComponent.fromLegacyText(ChatColor.WHITE + choices[i]));
            answer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to select this answer.").create()));
            answer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "" + (i + 1)));
            component.addExtra(answer);
            player.spigot().sendMessage(component);
        }
        waitForChatAnswer.put(player, new AnswerHandler() {
            @Override
            public boolean handleAnswer(Player player, String answer) {
                if (answer.matches("\\d+")) {
                    int choice = Integer.parseInt(answer) - 1;
                    if (choice >= 0 && choice < choices.length) {
                        TextBuilder
                                .create("> ").gray().italic()
                                .append(ChatColor.stripColor(choices[choice])).gray().italic()
                                .sendTo(player);
                        return callback.handleAnswer(player, choice, choices[choice]);
                    }
                }

                player.sendMessage(ChatColor.RED +
                        "Invalid answer. Please enter a number from 1 to " +
                        choices.length + " or click on a choice.");
                return false;
            }
        });
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        AnswerHandler handler = waitForChatAnswer.get(event.getPlayer());
        if (handler != null) {
            if (handler.handleAnswer(event.getPlayer(), event.getMessage()) &&
                    waitForChatAnswer.get(event.getPlayer()) == handler) {
                waitForChatAnswer.remove(event.getPlayer());
            }
            event.setCancelled(true);
        }
    }

    /**
     * Resets any running conversations.
     */
    public void reset() {
        waitForChatAnswer.clear();
    }
}
