# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v21.1.7-1.21.1] - 2026-09-03

### Added

- Add per-enchantment `exclusive_set` enchantment tags
- Add Channeling to the mace, which takes effect when striking with a smash attack
- This makes Channeling incompatible with the Wind Burst enchantment

### Changed

- Split additional item enchantment compatibilities into separate built-in data packs
- All enchantment compatibility changes are now controlled via separate built-in data packs
- Changes to bow, crossbow and mace enchantments remain enabled by default
- Changes to damage and protection enchantments remain disabled by default

### Fixed

- Fix firing a projectile weapon from the main hand with infinity but without any ammo not playing the item animation in
  survival mode when a usable item (like a shield) is held in the offhand
- Fix duplicate attribute tooltips for enchanted armor items

## [v21.1.6-1.21.1] - 2025-07-20

### Changed

- Support Fire Aspect being used on shields

## [v21.1.5-1.21.1] - 2025-04-13

### Fixed

- Fix crash with Apotheosis' Thunderstruck affix

## [v21.1.4-1.21.1] - 2025-02-14

### Fixed

- Fix crash when determining looting level for projectiles not fired from a weapon

## [v21.1.3-1.21.1] - 2025-01-25

### Changed

- The NeoForge Data Pack Extensions library is no longer bundled

## [v21.1.2-1.21.1] - 2025-01-20

### Added

- Add support for mace enchantments thanks to [Winter Veritas](https://github.com/winterveritas)

### Changed

- Not all sword enchantments are any longer made compatible with other weapons
    - The following enchantments now remain sword-exclusive to keep swords as a unique weapon type: knockback, fire
      aspect, sweeping edge
    - Of course, you can add those enchantments back manually to the appropriate item tags to revert this change

### Fixed

- Fix sharpness and protection not being compatible with other damage / protection enchantments

## [v21.1.1-1.21.1] - 2025-01-20

### Fixed

- Fix startup crash on Fabric due to a mixin being unable to remap

## [v21.1.0-1.21.1] - 2025-01-20

### Added

- New improvements for Frost Walker
    - Works when jumping and falling
    - Refreshes the ice below the wearer while standing still
    - Is able to replace waterlogged blocks such as kelp and seagrass

### Changed

- Update to Minecraft 1.21.1
- Arrows shot from bows enchanted with multishot are now spread out vertically
- Fortune affects experience dropped from blocks

### Removed

- Remove custom `/enchant` command
- Remove roman numerals fix, will come back in another QoL project
- Remove optional mending enchantment overhaul, as it was hardly used by anyone