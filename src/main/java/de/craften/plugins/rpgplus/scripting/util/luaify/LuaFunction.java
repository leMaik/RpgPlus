package de.craften.plugins.rpgplus.scripting.util.luaify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A function that will be converted to a field of the table by {@link Luaify}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LuaFunction {
    /**
     * The name of this function.
     *
     * @return name of this function
     */
    String value();
}
