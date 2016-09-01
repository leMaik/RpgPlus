package de.craften.plugins.rpgplus.scripting.util;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A collector that produces a {@link LuaTable} list.
 */
public class TableListCollector<T extends LuaValue> implements Collector<T, LuaTable, LuaTable> {
    @Override
    public Supplier<LuaTable> supplier() {
        return LuaTable::new;
    }

    @Override
    public BiConsumer<LuaTable, T> accumulator() {
        return (table, t) -> table.rawset(table.rawlen() + 1, t);
    }

    @Override
    public BinaryOperator<LuaTable> combiner() {
        return (t1, t2) -> {
            for (int i = 1; i <= t2.rawlen(); i++) {
                t1.rawset(t1.rawlen() + i, t2.get(i));
            }
            return t1;
        };
    }

    @Override
    public Function<LuaTable, LuaTable> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.IDENTITY_FINISH);
    }
}
