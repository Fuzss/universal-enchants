package fuzs.universalenchants.common.handler;

import com.google.common.collect.ImmutableList;
import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import fuzs.puzzleslib.common.api.event.v1.data.MutableFloat;
import fuzs.puzzleslib.common.api.event.v1.data.MutableInt;
import fuzs.universalenchants.common.core.AndHolderSet;
import fuzs.universalenchants.common.core.NotHolderSet;
import fuzs.universalenchants.common.core.OrHolderSet;
import fuzs.universalenchants.common.init.ModRegistry;
import fuzs.universalenchants.common.mixin.accessor.Enchantment$EnchantmentDefinitionAccessor;
import fuzs.universalenchants.common.mixin.accessor.EnchantmentAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ItemCompatHandler {
    public static final Set<EquipmentSlotGroup> ARMOR_EQUIPMENT_SLOT_GROUPS = Set.of(EquipmentSlotGroup.FEET,
            EquipmentSlotGroup.LEGS,
            EquipmentSlotGroup.CHEST,
            EquipmentSlotGroup.HEAD,
            EquipmentSlotGroup.ARMOR);
    private static final ThreadLocal<Unit> IS_BLOCKING_WITH_SHIELD = new ThreadLocal<>();

    public static void onTagsUpdated(HolderLookup.Provider registries, boolean isClientUpdate) {
        // Use this event to modify registered enchantments directly via accessor mixins.
        // Mutating record fields is not supported by NeoForge as source recompilation fails.
        registries.lookupOrThrow(Registries.ENCHANTMENT)
                .listElements()
                .forEach((Holder.Reference<Enchantment> holder) -> {
                    Enchantment.EnchantmentDefinition enchantmentDefinition = holder.value().definition();
                    // Allow all armor enchantments to also work for the body equipment slot.
                    // Those need to separately support such items, though which must then be set in the enchantment definitions.
                    if (!enchantmentDefinition.slots().contains(EquipmentSlotGroup.BODY)) {
                        for (EquipmentSlotGroup slot : enchantmentDefinition.slots()) {
                            if (ARMOR_EQUIPMENT_SLOT_GROUPS.contains(slot)) {
                                ImmutableList.Builder<EquipmentSlotGroup> builder = ImmutableList.builder();
                                builder.addAll(enchantmentDefinition.slots());
                                builder.add(EquipmentSlotGroup.BODY);
                                Enchantment$EnchantmentDefinitionAccessor.class.cast(enchantmentDefinition)
                                        .universalenchants$setSlots(builder.build());
                            }
                        }
                    }

                    modifyExclusiveEnchantmentSet(registries, holder);
                    modifySupportedEnchantmentItems(registries, holder);
                });
    }

    private static void modifyExclusiveEnchantmentSet(HolderLookup.Provider registries, Holder.Reference<Enchantment> holder) {
        HolderLookup.RegistryLookup<Enchantment> enchantmentLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        Enchantment enchantment = holder.value();
        // Support one exclusive set per enchantment; in addition to the potentially existing exclusive set defined in the enchantment definition.
        TagKey<Enchantment> exclusiveSetTagKey = ModRegistry.getExclusiveSetEnchantmentTag(holder.key());
        HolderSet<Enchantment> exclusiveSet = enchantmentLookup.get(exclusiveSetTagKey)
                .<HolderSet<Enchantment>>map((HolderSet.Named<Enchantment> holderSet) -> {
                    return new OrHolderSet<>(List.of(enchantment.exclusiveSet(), holderSet));
                })
                .orElseGet(enchantment::exclusiveSet);
        EnchantmentAccessor.class.cast(enchantment).universalenchants$setExclusiveSet(exclusiveSet);
        // Support an inclusive set per enchantment for removing enchantments from the existing exclusive sets without overriding them.
        TagKey<Enchantment> inclusiveSetTagKey = ModRegistry.getInclusiveSetEnchantmentTag(holder.key());
        exclusiveSet = enchantmentLookup.get(inclusiveSetTagKey).<HolderSet<Enchantment>>map(holderSet -> {
            return new AndHolderSet<>(List.of(enchantment.exclusiveSet(),
                    new NotHolderSet<>(enchantmentLookup, holderSet)));
        }).orElseGet(enchantment::exclusiveSet);
        EnchantmentAccessor.class.cast(enchantment).universalenchants$setExclusiveSet(exclusiveSet);
    }

    private static void modifySupportedEnchantmentItems(HolderLookup.Provider registries, Holder.Reference<Enchantment> holder) {
        HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
        Enchantment.EnchantmentDefinition enchantmentDefinition = holder.value().definition();
        // Allow one supported items tag per enchantment; in addition to the potentially existing supported items tag defined in the enchantment definition.
        TagKey<Item> secondaryEnchantableTagKey = ModRegistry.getSecondaryEnchantableItemTag(holder.key());
        itemLookup.get(secondaryEnchantableTagKey).ifPresent((HolderSet.Named<Item> holderSet) -> {
            HolderSet<Item> supportedItems = new OrHolderSet<>(List.of(enchantmentDefinition.supportedItems(),
                    holderSet));
            Enchantment$EnchantmentDefinitionAccessor.class.cast(enchantmentDefinition)
                    .universalenchants$setSupportedItems(supportedItems);
        });
        // Allow one primary items tag per enchantment; in addition to the potentially existing primary items tag defined in the enchantment definition.
        TagKey<Item> primaryEnchantableTagKey = ModRegistry.getPrimaryEnchantableItemTag(holder.key());
        itemLookup.get(primaryEnchantableTagKey).ifPresent((HolderSet.Named<Item> holderSet) -> {
            Optional<HolderSet<Item>> optionalPrimaryItems = enchantmentDefinition.primaryItems()
                    .<HolderSet<Item>>map((HolderSet<Item> primaryItems) -> {
                        return new OrHolderSet<>(List.of(primaryItems, holderSet));
                    })
                    .or(() -> Optional.of(holderSet));
            Enchantment$EnchantmentDefinitionAccessor.class.cast(enchantmentDefinition)
                    .universalenchants$setPrimaryItems(optionalPrimaryItems);
        });
    }

    public static EventResult onShieldBlock(LivingEntity blockingEntity, DamageSource damageSource, MutableFloat blockedDamage) {
        if (blockingEntity.level() instanceof ServerLevel serverLevel) {
            if (damageSource.isDirect() && damageSource.getEntity() instanceof LivingEntity attackingEntity) {
                // Workaround for mods hooking into post-attack effects and triggering this event (namely Apotheosis).
                if (IS_BLOCKING_WITH_SHIELD.get() == null) {
                    IS_BLOCKING_WITH_SHIELD.set(Unit.INSTANCE);
                    doPostAttackEffectsWithItemSource(serverLevel,
                            attackingEntity,
                            damageSource,
                            blockingEntity.getUseItem());
                    float attackKnockback = (float) blockingEntity.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    attackKnockback = EnchantmentHelper.modifyKnockback(serverLevel,
                            blockingEntity.getUseItem(),
                            attackingEntity,
                            damageSource,
                            attackKnockback);
                    // This implementation also fixes a vanilla bug where shields do not deal knockback in LivingEntity::blockedByShield,
                    // since the knockback method is called on the blocking entity and not the attacking entity.
                    // If that should not happen, so knockback only applies when the actual knockback enchantment is present,
                    // include a check here if the knockback is different from the original attribute value.
                    attackingEntity.knockback(0.5 * attackKnockback,
                            blockingEntity.getX() - attackingEntity.getX(),
                            blockingEntity.getZ() - attackingEntity.getZ());
                    IS_BLOCKING_WITH_SHIELD.remove();
                }
            }
        }

        return EventResult.PASS;
    }

    /**
     * An adjusted version of
     * {@link EnchantmentHelper#doPostAttackEffectsWithItemSource(ServerLevel, Entity, DamageSource, ItemStack)} that
     * runs the post-attack for the victim also for
     * {@link EnchantmentHelper#runIterationOnItem(ItemStack, EquipmentSlot, LivingEntity,
     * EnchantmentHelper.EnchantmentInSlotVisitor)}, and not just
     * {@link EnchantmentHelper#runIterationOnEquipment(LivingEntity, EnchantmentHelper.EnchantmentInSlotVisitor)}.
     * <p>
     * This allows shield enchantments such as thorns and fire aspect to work correctly.
     */
    public static void doPostAttackEffectsWithItemSource(ServerLevel serverLevel, Entity entity, DamageSource damageSource, @Nullable ItemStack itemSource) {
        if (itemSource != null) {
            if (entity instanceof LivingEntity livingEntity) {
                EnchantmentHelper.runIterationOnItem(itemSource,
                        EquipmentSlot.MAINHAND,
                        livingEntity,
                        (Holder<Enchantment> holder, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse) -> holder.value()
                                .doPostAttack(serverLevel,
                                        enchantmentLevel,
                                        enchantedItemInUse,
                                        EnchantmentTarget.VICTIM,
                                        entity,
                                        damageSource));
            }
        }

        EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, itemSource);
    }

    public static EventResult onUseItemTick(LivingEntity livingEntity, ItemStack itemStack, InteractionHand interactionHand, MutableInt remainingUseDuration) {
        Item item = itemStack.getItem();
        int itemUseDuration = itemStack.getUseDuration(livingEntity) - remainingUseDuration.getAsInt();
        if (item instanceof BowItem && itemUseDuration < 20 || item instanceof TridentItem && itemUseDuration < 10) {
            // Support quick charge enchantment for bows and tridents.
            // The values are the same as for crossbows, but speed improvement is not relative to actual item use duration now.
            float chargingTime = EnchantmentHelper.modifyCrossbowChargingTime(itemStack, livingEntity, 1.25F);
            remainingUseDuration.mapAsInt(duration -> duration - Mth.floor((1.25F - chargingTime) / 0.25F));
        }

        return EventResult.PASS;
    }
}
