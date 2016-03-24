package de.craften.plugins.rpgplus.scripting.util.luaify;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Luaify is a converter that takes {@link org.luaj.vm2.LuaTable}s with methods annotated with {@link @LuaMethod} and
 * wraps them so that they can be used in lua.
 */
public class Luaify {
    /**
     * Takes all public methods that are annotated with {@link LuaFunction} from the given object and puts the
     * corresponding {@link org.luaj.vm2.LuaFunction}s into the given table.
     *
     * @param input input object
     * @param table table to put the wrapper functions into
     */
    public static void convert(final Object input, final LuaTable table) {
        for (final Method method : input.getClass().getMethods()) {
            if (method.isAnnotationPresent(LuaFunction.class)) {
                LuaFunction annotation = method.getAnnotation(LuaFunction.class);
                table.set(annotation.value(), new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        try {
                            Object returnValue;
                            if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Varargs.class) {
                                returnValue = method.invoke(input, args);
                            } else {
                                returnValue = method.invoke(input, (Object[]) convertArguments(args, method.getParameterTypes()));
                            }

                            if (method.getReturnType() == Void.TYPE) {
                                return LuaValue.NONE;
                            } else {
                                return (Varargs) returnValue;
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new LuaError("Could not invoke method " + method.getName() + " of " + table.getClass());
                        }
                    }
                });
            }
        }
    }

    /**
     * Converts the given table in-place. This is the same as calling {@link #convert(Object, LuaTable)} with the table
     * as first and second parameter. Wrapped methods are put into the table.
     *
     * @param table input object and table to put the wrapper functions into
     */
    public static void convertInPlace(final LuaTable table) {
        convert(table, table);
    }

    private static Varargs[] convertArguments(Varargs args, Class[] parameterTypes) {
        Varargs[] finalArgs = new Varargs[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isInstance(args.arg(i + 1))) {
                finalArgs[i] = args.arg(i + 1);
            } else {
                throw new LuaError("Type mismatch");
            }
        }
        return finalArgs;
    }
}
