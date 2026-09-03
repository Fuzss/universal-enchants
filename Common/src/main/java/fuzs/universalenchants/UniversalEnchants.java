package fuzs.universalenchants;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.api.event.v1.entity.living.*;
import fuzs.puzzleslib.api.event.v1.level.BlockEvents;
import fuzs.puzzleslib.api.event.v1.server.TagsUpdatedCallback;
import fuzs.universalenchants.config.ServerConfig;
import fuzs.universalenchants.handler.BetterEnchantsHandler;
import fuzs.universalenchants.handler.ItemCompatHandler;
import fuzs.universalenchants.init.ModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UniversalEnchants implements ModConstructor {
    public static final String MOD_ID = "universalenchants";
    public static final String MOD_NAME = "Universal Enchants";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);
    public static final ResourceLocation ADDITIONAL_DAMAGE_ENCHANTMENTS_ID = id("additional_damage_enchantments");
    public static final ResourceLocation ADDITIONAL_WEAPON_ENCHANTMENTS_ID = id("additional_weapon_enchantments");
    public static final ResourceLocation ADDITIONAL_RANGED_ENCHANTMENTS_ID = id("additional_ranged_enchantments");
    public static final ResourceLocation ADDITIONAL_SHIELD_ENCHANTMENTS_ID = id("additional_shield_enchantments");
    public static final ResourceLocation ADDITIONAL_ANIMAL_ENCHANTMENTS_ID = id("additional_animal_enchantments");
    public static final ResourceLocation COMPATIBLE_BOW_ENCHANTMENTS_ID = id("compatible_bow_enchantments");
    public static final ResourceLocation COMPATIBLE_CROSSBOW_ENCHANTMENTS_ID = id("compatible_crossbow_enchantments");
    public static final ResourceLocation COMPATIBLE_MACE_ENCHANTMENTS_ID = id("compatible_mace_enchantments");
    public static final ResourceLocation COMPATIBLE_DAMAGE_ENCHANTMENTS_ID = id("compatible_damage_enchantments");
    public static final ResourceLocation COMPATIBLE_PROTECTION_ENCHANTMENTS_ID = id("compatible_protection_enchantments");

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        TagsUpdatedCallback.EVENT.register(ItemCompatHandler::onTagsUpdated);
        UseItemEvents.TICK.register(ItemCompatHandler::onUseItemTick);
        ComputeEnchantedLootBonusCallback.EVENT.register(ItemCompatHandler::onComputeEnchantedLootBonus);
        LivingHurtCallback.EVENT.register(BetterEnchantsHandler::onLivingHurt);
        BlockEvents.FARMLAND_TRAMPLE.register(BetterEnchantsHandler::onFarmlandTrample);
        ShieldBlockCallback.EVENT.register(ItemCompatHandler::onShieldBlock);
        // run after other mods had a chance to change looting level
        LivingExperienceDropCallback.EVENT.register(EventPhase.AFTER, BetterEnchantsHandler::onLivingExperienceDrop);
        BlockEvents.DROP_EXPERIENCE.register(EventPhase.AFTER, BetterEnchantsHandler::onDropExperience);
    }

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        context.registerBuiltInPack(ADDITIONAL_DAMAGE_ENCHANTMENTS_ID,
                Component.literal("Additional Damage Enchantments"),
                true);
        context.registerBuiltInPack(ADDITIONAL_WEAPON_ENCHANTMENTS_ID,
                Component.literal("Additional Weapon Enchantments"),
                true);
        context.registerBuiltInPack(ADDITIONAL_RANGED_ENCHANTMENTS_ID,
                Component.literal("Additional Ranged Enchantments"),
                true);
        context.registerBuiltInPack(ADDITIONAL_SHIELD_ENCHANTMENTS_ID,
                Component.literal("Additional Shield Enchantments"),
                true);
        context.registerBuiltInPack(ADDITIONAL_ANIMAL_ENCHANTMENTS_ID,
                Component.literal("Additional Animal Enchantments"),
                true);
        context.registerBuiltInPack(COMPATIBLE_BOW_ENCHANTMENTS_ID,
                Component.literal("Compatible Bow Enchantments"),
                true);
        context.registerBuiltInPack(COMPATIBLE_CROSSBOW_ENCHANTMENTS_ID,
                Component.literal("Compatible Crossbow Enchantments"),
                true);
        context.registerBuiltInPack(COMPATIBLE_MACE_ENCHANTMENTS_ID,
                Component.literal("Compatible Mace Enchantments"),
                true);
        context.registerBuiltInPack(COMPATIBLE_DAMAGE_ENCHANTMENTS_ID,
                Component.literal("Compatible Damage Enchantments"),
                false);
        context.registerBuiltInPack(COMPATIBLE_PROTECTION_ENCHANTMENTS_ID,
                Component.literal("Compatible Protection Enchantments"),
                false);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
