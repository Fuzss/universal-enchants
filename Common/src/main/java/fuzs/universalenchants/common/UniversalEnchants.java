package fuzs.universalenchants.common;

import com.google.common.base.Predicates;
import fuzs.puzzleslib.common.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.core.v1.context.ItemComponentsContext;
import fuzs.puzzleslib.common.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.common.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.common.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.common.api.event.v1.entity.living.*;
import fuzs.puzzleslib.common.api.event.v1.level.BlockEvents;
import fuzs.puzzleslib.common.api.event.v1.server.TagsUpdatedCallback;
import fuzs.universalenchants.common.config.ServerConfig;
import fuzs.universalenchants.common.handler.BetterEnchantsHandler;
import fuzs.universalenchants.common.handler.ItemCompatHandler;
import fuzs.universalenchants.common.init.ModRegistry;
import fuzs.universalenchants.common.network.ClientboundStopUsingItemMessage;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.equipment.Equippable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UniversalEnchants implements ModConstructor {
    public static final String MOD_ID = "universalenchants";
    public static final String MOD_NAME = "Universal Enchants";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);
    public static final Identifier COMPATIBLE_BOW_ENCHANTMENTS_LOCATION = id("compatible_bow_enchantments");
    public static final Identifier COMPATIBLE_CROSSBOW_ENCHANTMENTS_LOCATION = id("compatible_crossbow_enchantments");
    public static final Identifier COMPATIBLE_MACE_ENCHANTMENTS_LOCATION = id("compatible_mace_enchantments");
    public static final Identifier COMPATIBLE_DAMAGE_ENCHANTMENTS_LOCATION = id("compatible_damage_enchantments");
    public static final Identifier COMPATIBLE_PROTECTION_ENCHANTMENTS_LOCATION = id("compatible_protection_enchantments");

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        TagsUpdatedCallback.EVENT.register(ItemCompatHandler::onTagsUpdated);
        UseItemEvents.TICK.register(ItemCompatHandler::onUseItemTick);
        LivingHurtCallback.EVENT.register(BetterEnchantsHandler::onLivingHurt);
        PickProjectileCallback.EVENT.register(BetterEnchantsHandler::onPickProjectile);
        ShieldBlockCallback.EVENT.register(ItemCompatHandler::onShieldBlock);
        // run after other mods had a chance to change looting level
        LivingExperienceDropCallback.EVENT.register(EventPhase.AFTER, BetterEnchantsHandler::onLivingExperienceDrop);
        BlockEvents.DROP_EXPERIENCE.register(EventPhase.AFTER, BetterEnchantsHandler::onDropExperience);
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.optional();
        context.playToClient(ClientboundStopUsingItemMessage.class, ClientboundStopUsingItemMessage.STREAM_CODEC);
    }

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        context.registerBuiltInPack(COMPATIBLE_BOW_ENCHANTMENTS_LOCATION,
                Component.literal("Compatible Bow Enchantments"),
                true);
        context.registerBuiltInPack(COMPATIBLE_CROSSBOW_ENCHANTMENTS_LOCATION,
                Component.literal("Compatible Crossbow Enchantments"),
                true);
        context.registerBuiltInPack(COMPATIBLE_MACE_ENCHANTMENTS_LOCATION,
                Component.literal("Compatible Mace Enchantments"),
                true);
        context.registerBuiltInPack(COMPATIBLE_DAMAGE_ENCHANTMENTS_LOCATION,
                Component.literal("Compatible Damage Enchantments"),
                false);
        context.registerBuiltInPack(COMPATIBLE_PROTECTION_ENCHANTMENTS_LOCATION,
                Component.literal("Compatible Protection Enchantments"),
                false);
    }

    @Override
    public void onRegisterItemComponentPatches(ItemComponentsContext context) {
        context.registerItemComponentsPatch((Item item) -> {
                    return item instanceof ShearsItem || item instanceof ShieldItem;
                },
                (DataComponentGetter components, DataComponentMap.Builder builder, HolderLookup.Provider lookup, ResourceKey<Item> key) -> {
                    builder.set(DataComponents.ENCHANTABLE, new Enchantable(1)).build();
                });
        context.registerItemComponentsPatch(Predicates.alwaysTrue(),
                (DataComponentGetter components, DataComponentMap.Builder builder, HolderLookup.Provider lookup, ResourceKey<Item> key) -> {
                    if (components.get(DataComponents.ENCHANTABLE) == null) {
                        Equippable equippable = components.get(DataComponents.EQUIPPABLE);
                        if (equippable != null && equippable.slot() == EquipmentSlot.BODY) {
                            ItemAttributeModifiers itemAttributeModifiers = components.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,
                                    ItemAttributeModifiers.EMPTY);
                            double defenseValue = itemAttributeModifiers.modifiers()
                                    .stream()
                                    .filter((ItemAttributeModifiers.Entry entry) -> entry.attribute()
                                            .is(Attributes.ARMOR))
                                    .map(ItemAttributeModifiers.Entry::modifier)
                                    .mapToDouble(AttributeModifier::amount)
                                    .sum();
                            builder.set(DataComponents.ENCHANTABLE,
                                    new Enchantable(Math.max(1, Mth.ceil(defenseValue)))).build();
                        }
                    }
                });
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
