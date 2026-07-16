# Equipment Check
Reminds you when a gear slot isn't holding what you wanted.

Every equipment slot (head, cape, amulet, ammo, weapon, shield, body, legs,
gloves, boots, ring) is set to one of three modes:
- **Off** — ignore the slot.
- **Empty** — warn when nothing is equipped.
- **Item-specific** — warn unless the slot holds a named item.

Item names match partially and ignore case, so `slayer helmet` covers every
variant. Leave the item box blank and the slot falls back to a plain empty check.

Alerts you three ways when a watched slot fails its check:
- a per-slot **Game chat** message,
- an optional **notification** (sound / screen flash / system tray), fired once
  per episode, and
- a live **overlay** listing each failing slot.

The plugin config is grouped into collapsible sections:
- **Equipment Slots** — the mode and required item for each slot.
- **Warning Colors** — set the overlay warning color per slot.
- **Misc.** — configure the notification and the overlay background color.

The shield check is suppressed while a two-handed weapon is equipped, since the
shield slot is empty by design in that case.
