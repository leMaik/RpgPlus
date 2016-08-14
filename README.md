# RpgPlus [![Build Status](https://api.travis-ci.org/leMaik/RpgPlus.svg?branch=master)](https://travis-ci.org/leMaik/RpgPlus)
RpgPlus is a Bukkit plugin that allows easy scripting of complex RPG worlds using Lua. It is currently in development
and will support dialogs, NPCs, phasing, event handling and much more. We aim to provide a powerful framework for
scripting RPGs.

## Lua API
Read the [Luadocs][luadoc] to learn the API that RpgPlus provides. We try to keep it simple, intuitive and *Lua-like*.

New to RpgPlus and/or Lua? RpgPlus is not a full RPG plugin but rather an API for scripting your RPG with Lua. Lua is fun
and simple to use (and thus makes coding the game logic much more fun). You can find more information (and tutorials) about Lua [here][lua].

[lua]:http://www.lua.org/start.html
[luadoc]:http://lemaik.github.io/RpgPlus/lua-api

## Commands
The permission `rpgplus.manage` is required to use the following commands. By default, only operators have this permission.

| command              | Description                   |
|----------------------|-------------------------------|
| `/rpgplus reload`    | Reload scripts.               |
| `/rpgplus disable`   | Disable scripts¹.             |
| `/rpgplus enable`    | Enable and reload scripts.    |

¹Note that the scripts are still disabled after you restart/reload the server, until they are explicitly
enabled again.

## License
RpgPlus is licensed under the MIT license.
