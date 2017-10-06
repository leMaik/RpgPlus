package de.craften.plugins.rpgplus.scripting;

import org.luaj.vm2.LuaValue;

/**
 * A module for scripting.
 */
public interface ScriptingModule {
    /**
     * Gets the actual lua module.
     *
     * @return lua module
     */
    LuaValue getModule();

    /**
     * Resets the module.
     */
    default void reset() {}
}
