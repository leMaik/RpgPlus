package de.craften.plugins.rpgplus.test.util;

import de.craften.plugins.rpgplus.scripting.util.SafeInvoker;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.concurrent.Callable;

/**
 * A "safe" caller for lua functions that is actually unsafe.
 */
public class DummySafeInvoker implements SafeInvoker {
    @Override
    public Varargs invokeSafely(Callable<Varargs> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new LuaError(e);
        }
    }

    @Override
    public Varargs invokeSafely(LuaValue callable, LuaValue... arguments) {
        try {
            return callable.invoke(arguments);
        } catch (Exception e) {
            throw new LuaError(e);
        }
    }

    @Override
    public Varargs invokeSafely(LuaValue callable, Varargs arguments) {
        try {
            return callable.invoke(arguments);
        } catch (Exception e) {
            throw new LuaError(e);
        }
    }
}
