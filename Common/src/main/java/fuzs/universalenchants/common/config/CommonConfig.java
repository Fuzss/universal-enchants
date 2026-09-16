package fuzs.universalenchants.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;

public class CommonConfig implements ConfigCore {
    private static final String CATEGORY_ADDITIONAL_ENCHANTS_DATA_PACKS = "additional_enchants_data_packs";
    private static final String CATEGORY_COMPATIBILITY_DATA_PACKS = "compatibility_data_packs";
    private static final String HINT_NEW_WORLDS_ONLY = "Automatically add the corresponding data pack when creating a new world.";

    @Config(category = CATEGORY_ADDITIONAL_ENCHANTS_DATA_PACKS, description = {
            "Allows basic damage enchantments to be applied to additional weapon items.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean damage = true;
    @Config(category = CATEGORY_ADDITIONAL_ENCHANTS_DATA_PACKS, description = {
            "Allows general weapon enchantments to be applied to additional weapon items.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean weapon = true;
    @Config(category = CATEGORY_ADDITIONAL_ENCHANTS_DATA_PACKS, description = {
            "Allows ranged weapon enchantments to be applied across bows and crossbows.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean ranged = true;
    @Config(category = CATEGORY_ADDITIONAL_ENCHANTS_DATA_PACKS, description = {
            "Allows additional weapon and defensive enchantments to be applied to shields.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean shield = true;
    @Config(category = CATEGORY_ADDITIONAL_ENCHANTS_DATA_PACKS, description = {
            "Allows armor enchantments to be applied to additional animal armor.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean animal = true;
    @Config(category = CATEGORY_COMPATIBILITY_DATA_PACKS, description = {
            "Allows normally incompatible bow enchantments to be used together.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean bow = true;
    @Config(category = CATEGORY_COMPATIBILITY_DATA_PACKS, description = {
            "Allows normally incompatible crossbow enchantments to be used together.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean crossbow = true;
    @Config(category = CATEGORY_COMPATIBILITY_DATA_PACKS, description = {
            "Allows the mace's damage enchantments to be combined with minecraft:density.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean mace = true;
    @Config(name = "damage", category = CATEGORY_COMPATIBILITY_DATA_PACKS, description = {
            "Allows the different damage enchantments to be combined with each other.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean compatibleDamage = false;
    @Config(category = CATEGORY_COMPATIBILITY_DATA_PACKS, description = {
            "Allows the different protection enchantments to be combined with each other.", HINT_NEW_WORLDS_ONLY
    }, worldRestart = true)
    public boolean protection = false;
}
