package de.craften.plugins.rpgplus.scripting.api.dialogs;

import de.craften.plugins.rpgplus.components.dialogs.Dialog;
import de.craften.plugins.rpgplus.components.dialogs.DialogLine;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.HashMap;
import java.util.stream.Collectors;

public class DialogParser {

    /**
     * Creates a new dialog from the given dialog definition.
     *
     * @param dialogDefinition a dialog definition
     */
    public static Dialog parseDialog(LuaTable dialogDefinition) {
        HashMap<String, DialogLine> lines = new HashMap<>();

        for (int i = 1; i <= dialogDefinition.length(); i++) {
            LuaTable line = dialogDefinition.get(i).checktable();
            if (line.get(2).isfunction()) {
                lines.put(line.get(1).checkjstring(), new DialogLine.CustomCode(
                        (next) -> line.get(2).invoke(
                                new OneArgFunction() {
                                    @Override
                                    public LuaValue call(LuaValue arg) {
                                        next.accept(arg.optjstring(null));
                                        return LuaValue.NIL;
                                    }
                                }
                        ).optjstring(1, null),
                        line.get(4).opttable(LuaValue.tableOf()).get("delay").optint(0)
                ));
            } else if (line.get(3).istable()) {
                // question
                lines.put(line.get(1).checkjstring(), new DialogLine.Question(
                        line.get(2).checkjstring(),
                        ScriptUtil.toListTableStream(line.get(3).checktable())
                                .map(LuaValue::checkjstring)
                                .collect(Collectors.toList()),
                        line.get(4).opttable(LuaValue.tableOf()).get("delay").optint(0)
                ));
            } else {
                lines.put(line.get(1).checkjstring(), new DialogLine.Statement(
                        line.get(2).checkjstring(),
                        line.get(3).optjstring(null),
                        line.get(4).opttable(LuaValue.tableOf()).get("delay").optint(0)
                ));
            }
        }

        return new Dialog(lines);
    }
}
