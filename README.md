# Universal Enchants

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects)
and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://raw.githubusercontent.com/Fuzss/modresources/main/pages/data/universalenchants/banner.png)

## Configuration

Starting with Minecraft 1.21, enchantments received a major implementation overhaul and were fully migrated to
data-pack-driven definitions. For vanilla behavior and background details see the official documentation on
the [Minecraft Wiki](https://minecraft.wiki/w/Enchantment_definition).

Universal Enchants builds on top of this system with a small set of targeted extensions. The goal is to allow
fine-grained control over enchantment behavior while remaining fully compatible with vanilla and other mods.

### New Item Tags

In vanilla the items that an enchantment can be applied to are defined using the `supported_items` and `primary_items`
properties. These properties usually reference shared item tags that are reused by multiple enchantment

Because of this reuse, it is challenging to add or remove items for a single enchantment without affecting others.

Universal Enchants solves this by injecting two additional item tags per enchantment. These tags are merged into the
vanilla item lists at runtime and only affect the targeted enchantment.

#### Tag Format

- `<namespace>:secondary_enchantable/<path>` for adding to `supported_items`
- `<namespace>:primary_enchantable/<path>` for adding to `primary_items`

#### Example

- `minecraft:efficiency` → `data/minecraft/tags/item/secondary_enchantable/efficiency.json`
- `minecraft:efficiency` → `data/minecraft/tags/item/primary_enchantable/efficiency.json`

### New Enchantment Tags

Which enchantments are mutually exclusive is defined by the `exclusive_set` property. Similar to item tags, vanilla
frequently shares the same exclusion tag across multiple enchantments, which makes selective compatibility changes
difficult.

Universal Enchants introduces per-enchantment override tags that are applied in addition to vanilla definitions.

#### Tag Format

- `<namespace>:inclusive_set/<path>` for removing from `exclusive_set`
- `<namespace>:exclusive_set/<path>` for adding to `exclusive_set` (for Minecraft 1.21.1 & 1.21.11+)

#### Example

- `minecraft:efficiency` → `data/minecraft/tags/enchantment/inclusive_set/efficiency.json`
- `minecraft:efficiency` → `data/minecraft/tags/enchantment/exclusive_set/efficiency.json`

### Included Data Packs

The packs can be enabled or disabled from the `Data Packs` screen when creating a world or via the `/datapack` command
in existing worlds and when playing on multiplayer servers.

#### Additional Item Enchantments (for Minecraft 1.21.1 & 26.1+)

This section covers several optional data packs that extend which items can receive specific enchantments. These packs
primarily expose vanilla enchantment restrictions through data-driven item tags.

#### `universalenchants:additional_damage_enchantments` (default: `true`)

Allows basic damage enchantments to be applied to additional weapon items.

* `#minecraft:swords`: `minecraft:impaling`, `minecraft:breach`
* `#minecraft:axes`: `minecraft:sharpness`, `minecraft:smite`, `minecraft:bane_of_arthropods`, `minecraft:impaling`,
  `minecraft:breach`
* `#c:tools/trident`: `minecraft:sharpness`, `minecraft:smite`, `minecraft:bane_of_arthropods`, `minecraft:breach`
* `#c:tools/mace`: `minecraft:sharpness`, `minecraft:smite`, `minecraft:bane_of_arthropods`, `minecraft:impaling`
* `#minecraft:spears`: `minecraft:impaling`, `minecraft:breach`

#### `universalenchants:additional_weapon_enchantments` (default: `true`)

Allows general weapon enchantments to be applied to additional weapon items.

* `#minecraft:axes`: `minecraft:looting`, `minecraft:knockback`, `minecraft:fire_aspect`
* `#c:tools/trident`: `minecraft:looting`, `minecraft:knockback`, `minecraft:quick_charge`, `minecraft:piercing`
* `#c:tools/mace`: `minecraft:looting`, `minecraft:channeling`

#### `universalenchants:additional_ranged_enchantments` (default: `true`)

Allows ranged weapon enchantments to be applied across bows and crossbows.

* `#c:tools/bow`: `minecraft:piercing`, `minecraft:multishot`, `minecraft:quick_charge`, `minecraft:looting`
* `#c:tools/crossbow`: `minecraft:flame`, `minecraft:punch`, `minecraft:power`, `minecraft:infinity`,
  `minecraft:looting`

#### `universalenchants:additional_shield_enchantments` (default: `true`)

Allows additional weapon and defensive enchantments to be applied to shields.

* `#c:tools/shield`: `minecraft:thorns`, `minecraft:knockback`, `minecraft:fire_aspect`

#### `universalenchants:additional_animal_enchantments` (default: `true`)

Allows armor enchantments to be applied to additional animal armor.

* `#c:armors/horse`: `minecraft:protection`, `minecraft:fire_protection`, `minecraft:feather_falling`,
  `minecraft:blast_protection`, `minecraft:projectile_protection`, `minecraft:respiration`, `minecraft:thorns`,
  `minecraft:depth_strider`, `minecraft:frost_walker`, `minecraft:binding_curse`, `minecraft:soul_speed`,
  `minecraft:vanishing_curse`
* `#c:armors/wolf`: `minecraft:protection`, `minecraft:fire_protection`, `minecraft:feather_falling`,
  `minecraft:blast_protection`, `minecraft:projectile_protection`, `minecraft:respiration`, `minecraft:thorns`,
  `minecraft:depth_strider`, `minecraft:frost_walker`, `minecraft:binding_curse`, `minecraft:soul_speed`,
  `minecraft:vanishing_curse`
* `#c:armors/nautilus`: `minecraft:protection`, `minecraft:fire_protection`, `minecraft:feather_falling`,
  `minecraft:blast_protection`, `minecraft:projectile_protection`, `minecraft:thorns`, `minecraft:binding_curse`,
  `minecraft:vanishing_curse`

### Compatible Enchantments (for Minecraft 1.21.1 & 1.21.10+)

This section covers several optional data packs that relax common vanilla compatibility restrictions. Each pack focuses
on a specific item or enchantment group.

#### `universalenchants:compatible_bow_enchantments` (default: `true`)

Allows normally incompatible bow enchantments to be used together.

* `minecraft:mending`: `minecraft:infinity`
* `minecraft:infinity`: `minecraft:mending`

#### `universalenchants:compatible_crossbow_enchantments` (default: `true`)

Allows normally incompatible crossbow enchantments to be used together.

* `minecraft:multishot`: `minecraft:piercing`
* `minecraft:piercing`: `minecraft:multishot`

#### `universalenchants:compatible_mace_enchantments` (default: `true`)

Allows the mace's damage enchantments to be combined with `minecraft:density`.

* `minecraft:density`: `minecraft:sharpness`, `minecraft:smite`, `minecraft:bane_of_arthropods`, `minecraft:impaling`,
  `minecraft:breach`
* `minecraft:sharpness`: `minecraft:density`
* `minecraft:smite`: `minecraft:density`
* `minecraft:bane_of_arthropods`: `minecraft:density`
* `minecraft:impaling`: `minecraft:density`
* `minecraft:breach`: `minecraft:density`

#### `universalenchants:compatible_damage_enchantments` (default: `false`)

Allows the different damage enchantments to be combined with each other.

* `minecraft:sharpness`: `minecraft:smite`, `minecraft:bane_of_arthropods`, `minecraft:impaling`, `minecraft:breach`
* `minecraft:smite`: `minecraft:sharpness`
* `minecraft:bane_of_arthropods`: `minecraft:sharpness`
* `minecraft:impaling`: `minecraft:sharpness`
* `minecraft:breach`: `minecraft:sharpness`

#### `universalenchants:compatible_protection_enchantments` (default: `false`)

Allows the different protection enchantments to be combined with each other.

* `minecraft:protection`: `minecraft:blast_protection`, `minecraft:fire_protection`, `minecraft:projectile_protection`
* `minecraft:blast_protection`: `minecraft:protection`
* `minecraft:fire_protection`: `minecraft:protection`
* `minecraft:projectile_protection`: `minecraft:protection`
