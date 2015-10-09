package de.craften.plugins.rpgplus.components.commands;

import org.bukkit.command.CommandSender;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

public class CommandDispatcherTest {
    private CommandDispatcher dispatcher;

    @org.junit.Before
    public void setUp() throws Exception {
        dispatcher = new CommandDispatcher.Root();
    }

    @Test
    public void testRootCommand() {
        final AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        dispatcher.registerCommand("command", new CommandHandler() {
            @Override
            public boolean onCommand(CommandSender sender, String command, List<String> args) {
                handlerInvoked.set(true);
                assertThat(command, equalTo("command"));
                assertThat(args, hasItems("param1", "param2"));
                return true;
            }
        });

        dispatcher.onCommand(null, "command", Arrays.asList("param1", "param2"));

        assertTrue("Command handler should be invoked", handlerInvoked.get());
    }

    @Test
    public void testNestedCommand() {
        final AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        dispatcher.registerCommand(Arrays.asList("a", "very", "deeply", "nested", "command"), new CommandHandler() {
            @Override
            public boolean onCommand(CommandSender sender, String command, List<String> args) {
                handlerInvoked.set(true);
                assertThat(command, equalTo("command"));
                assertTrue(args.isEmpty());
                return true;
            }
        });

        dispatcher.onCommand(null, "a", Arrays.asList("very", "deeply", "nested", "command"));

        assertTrue("Command handler should be invoked", handlerInvoked.get());
    }

    @Test
    public void testNestedCommandWithParams() {
        final AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        dispatcher.registerCommand(Arrays.asList("a", "very", "deeply", "nested", "command"), new CommandHandler() {
            @Override
            public boolean onCommand(CommandSender sender, String command, List<String> args) {
                handlerInvoked.set(true);
                assertThat(command, equalTo("command"));
                assertThat(args, hasItems("param1", "param2"));
                return true;
            }
        });

        dispatcher.onCommand(null, "a", Arrays.asList("very", "deeply", "nested", "command", "param1", "param2"));

        assertTrue("Command handler should be invoked", handlerInvoked.get());
    }

    @Test
    public void testOverlappingCommands() {
        final AtomicBoolean commandHandlerInvoked = new AtomicBoolean(false);
        final AtomicBoolean subCommandHandlerInvoked = new AtomicBoolean(false);


        dispatcher.registerCommand(Arrays.asList("command"), new CommandHandler() {
            @Override
            public boolean onCommand(CommandSender sender, String command, List<String> args) {
                commandHandlerInvoked.set(true);
                assertThat(command, equalTo("command"));
                assertThat(args, hasItems("param1", "param2"));
                return true;
            }
        });

        dispatcher.registerCommand(Arrays.asList("command", "subcommand"), new CommandHandler() {
            @Override
            public boolean onCommand(CommandSender sender, String command, List<String> args) {
                subCommandHandlerInvoked.set(true);
                assertThat(command, equalTo("subcommand"));
                assertThat(args, hasItems("param1", "param2"));
                return true;
            }
        });

        dispatcher.onCommand(null, "command", Arrays.asList("param1", "param2"));
        assertTrue(commandHandlerInvoked.get());
        assertFalse(subCommandHandlerInvoked.get());

        commandHandlerInvoked.set(false);
        subCommandHandlerInvoked.set(false);
        dispatcher.onCommand(null, "command", Arrays.asList("subcommand", "param1", "param2"));
        assertFalse(commandHandlerInvoked.get());
        assertTrue(subCommandHandlerInvoked.get());
    }

    @Test
    public void testOverrideRootCommand() {
        final AtomicBoolean commandHandlerInvoked = new AtomicBoolean(false);
        final AtomicBoolean subCommandHandlerInvoked = new AtomicBoolean(false);

        dispatcher.registerCommand(Arrays.asList("command", "subcommand"), new CommandHandler() {
            @Override
            public boolean onCommand(CommandSender sender, String command, List<String> args) {
                subCommandHandlerInvoked.set(true);
                return true;
            }
        });

        dispatcher.registerCommand("command", new CommandHandler() {
            @Override
            public boolean onCommand(CommandSender sender, String command, List<String> args) {
                commandHandlerInvoked.set(true);
                return true;
            }
        });

        dispatcher.onCommand(null, "command", Arrays.asList("subcommand"));
        assertFalse(commandHandlerInvoked.get());
        assertTrue(subCommandHandlerInvoked.get());

        commandHandlerInvoked.set(false);
        subCommandHandlerInvoked.set(false);
        dispatcher.onCommand(null, "command", Arrays.asList("param1"));
        assertTrue(commandHandlerInvoked.get());
        assertFalse(subCommandHandlerInvoked.get());
    }
}