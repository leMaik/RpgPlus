package de.craften.plugins.rpgplus.scripting.util;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.concurrent.Callable;

/**
 * Caller for lua functions that handles errors.
 */
public interface SafeInvoker {
    /**
     * Invokes the given function and handles errors.
     *
     * @param callable a function
     * @return return value of the function
     */
    Varargs invokeSafely(Callable<Varargs> callable);

    /**
     * Invokes the given function and handles errors.
     *
     * @param callable  a function
     * @param arguments arguments
     * @return return value of the function
     */
    Varargs invokeSafely(LuaValue callable, LuaValue... arguments);

    /**
     * Invokes the given function and handles errors.
     *
     * @param callable  a function
     * @param arguments arguments
     * @return return value of the function
     */
    Varargs invokeSafely(LuaValue callable, Varargs arguments);
}
