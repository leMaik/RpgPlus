--- A poster.
-- @classmod Poster

--- Width of this poster, in blocks. This field is read-only.
width = _

--- Height of this poster, in blocks. This field is read-only.
height = _

--- Gives all items of this poster to the given player, as maps.
-- @param player player
-- @param[opt] callback callback that is invoked after giving the items to the player, parameter is a boolean that is
-- `false` if giving the items failed and `true` otherwise
--
function giveTo(player, callback) end

--- Attaches this poster to a wall. Note that a poster may be attached multiple times.
-- @param location location of the top left block to attach this poster to, including a `face` field that is either
-- `north`, `east`, `south` or `west`
-- @param[opt] callback callback that is invoked after attaching the poster, parameter is a boolean that is `false`
-- if attaching the poster failed and `true` otherwise
--
function attach(location, callback) end