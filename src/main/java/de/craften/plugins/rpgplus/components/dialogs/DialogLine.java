package de.craften.plugins.rpgplus.components.dialogs;

import java.util.List;
import java.util.function.Consumer;

/**
 * A line of a dialog.
 */
public abstract class DialogLine {
    private final String line;
    private final int delay;

    public DialogLine(String line) {
        this.line = line;
        this.delay = 0;
    }

    public String getLine() {
        return line;
    }

    public int getDelay() {
        return delay;
    }

    public static class Question extends DialogLine {
        public List<String> answers;

        public Question(String question, List<String> answers) {
            super(question);
            this.answers = answers;
        }

        public List<String> getAnswers() {
            return answers;
        }
    }

    public static class Statement extends DialogLine {
        public String nextState;

        public Statement(String line, String nextState) {
            super(line);
            this.nextState = nextState;
        }

        public boolean hasNextState() {
            return nextState != null;
        }

        public String getNextState() {
            return nextState;
        }
    }

    public static class CustomCode extends DialogLine {
        private final Consumer<Consumer<String>> customFunction;

        public CustomCode(Consumer<Consumer<String>> customFunction) {
            super("");
            this.customFunction = customFunction;
        }

        public Consumer<Consumer<String>> getCustomFunction() {
            return customFunction;
        }
    }
}
