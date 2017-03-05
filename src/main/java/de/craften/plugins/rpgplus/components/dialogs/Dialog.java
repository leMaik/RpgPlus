package de.craften.plugins.rpgplus.components.dialogs;

import de.craften.plugins.rpgplus.RpgPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * A dialogue.
 */
public class Dialog {
    private HashMap<String, DialogLine> lines;

    /**
     * Creates a new dialog from the given lines.
     *
     * @param lines lines
     */
    public Dialog(HashMap<String, DialogLine> lines) {
        this.lines = lines;
    }

    public DialogLine getLine(String state) {
        return lines.get(state);
    }

    public String getInitialState() {
        return "0";
    }

    public void performInteraction(DialogComponent dialogComponent, String name, Player player, String state, Consumer<String> next) {
        DialogLine dialogLine = getLine(state);

        Consumer<String> callNext = nextState -> {
            int delay = dialogLine.getDelay();
            if (delay > 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                        RpgPlus.getPlugin(RpgPlus.class),
                        () -> next.accept(nextState),
                        delay);
            } else {
                next.accept(nextState);
            }
        };

        if (dialogLine instanceof DialogLine.Question) {
            dialogComponent.askChoices(name, player, dialogLine.getLine(),
                    ((DialogLine.Question) dialogLine).getAnswers().stream().map(this::getLine).map(DialogLine::getLine).toArray(String[]::new),
                    ((player1, index, answer) -> {
                        callNext.accept(((DialogLine.Question) dialogLine).getAnswers().get(index));
                        return true;
                    }));
        } else if (dialogLine instanceof DialogLine.Statement) {
            dialogComponent.tell(name, player, dialogLine.getLine());
            if (((DialogLine.Statement) dialogLine).hasNextState()) {
                callNext.accept(((DialogLine.Statement) dialogLine).getNextState());
            }
        } else if (dialogLine instanceof DialogLine.CustomCode) {
            ((DialogLine.CustomCode) dialogLine).getCustomFunction().accept(callNext);
        }
    }
}
