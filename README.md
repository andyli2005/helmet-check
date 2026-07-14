# Equipment Check
Reminds you when you have an empty equipment slot.

Alerts you three ways when a watched gear slot is empty:
- a per-slot **Game chat** message,
- an optional **notification** (sound / screen flash / system tray), fired once
  per empty episode, and
- a live **overlay** listing each empty slot.

Every equipment slot (head, cape, amulet, ammo, weapon, body, shield, legs,
gloves, boots, ring) can be toggled in the plugin config, which is grouped into
collapsible sections:
- **Equipment Slots** — enable or disable the check for each slot.
- **Warning Colors** — set the overlay warning color per slot.
- **Misc.** — configure the empty-slot notification and the overlay background
  color.

The shield check is suppressed while a two-handed weapon is equipped, since the
shield slot is empty by design in that case.
