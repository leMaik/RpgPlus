--- This _pseudo-module_ defines tables that are used as parameters in various functions. Neither this module nor its
-- fields actually exist. The fields of this module are only used to document tables that various functions require.
-- @module rpgplus.types

--- A normal table that defines an item stack. I.e. it is used in the Inventory API.
-- @field type type of the item, must be a [Bukkit Material](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
-- @field[opt=nil] data data of the item, if `nil`, the data is ignored when matching and set to 0 when giving the item to a player
-- @field[opt=1] amount amount of the item
-- @field[opt] name item name
-- @field[opt] lore list of lines for the lore of this item
-- @field[opt=false] unbreakable whether the item should be unbreakable
-- @field[opt] texture base64-encoded texture for skull items (only works with type=skull_item, data=3)
-- @field[opt] bookData table with the author, title und pages of a book item (only works with type=book_and_quill and type=written_book)
itemstack = _