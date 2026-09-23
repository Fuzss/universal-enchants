package fuzs.universalenchants.common.handler;

import fuzs.universalenchants.common.init.ModRegistry;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.MovementPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.List;
import java.util.Optional;

public final class ModifyEnchantmentsHandler {

    private ModifyEnchantmentsHandler() {
        // NO-OP
    }

    public static boolean modifyEnchantment(ResourceKey<Enchantment> key, Enchantment.Builder builder, RegistryOps.RegistryInfoLookup lookup) {
        if (key == Enchantments.FROST_WALKER) {
            modifyFrostWalker(builder);
            return true;
        } else if (key == Enchantments.POWER) {
            modifyPower(builder);
            return true;
        } else if (key == Enchantments.CHANNELING) {
            modifyChanneling(builder, lookup);
            return true;
        } else {
            return false;
        }
    }

    private static void modifyFrostWalker(Enchantment.Builder builder) {
        // Allow frost walker to replace sea vegetation and itself, also remove on ground check to enable jump-sprinting across water.
        ReplaceDisk replaceDisk = new ReplaceDisk(new LevelBasedValue.Clamped(LevelBasedValue.perLevel(3.0F, 1.0F),
                0.0F,
                16.0F),
                LevelBasedValue.constant(1.0F),
                new Vec3i(0, -1, 0),
                Optional.of(BlockPredicate.anyOf(BlockPredicate.allOf(BlockPredicate.matchesTag(new Vec3i(0, 1, 0),
                                BlockTags.AIR),
                        BlockPredicate.matchesTag(ModRegistry.FROSTED_ICE_REPLACEABLES_BLOCK_TAG),
                        BlockPredicate.matchesFluids(Fluids.WATER),
                        BlockPredicate.unobstructed()), BlockPredicate.matchesBlocks(Blocks.FROSTED_ICE))),
                BlockStateProvider.holderOf(Blocks.FROSTED_ICE),
                Optional.of(GameEvent.BLOCK_PLACE));
        builder.getEffectsList(EnchantmentEffectComponents.LOCATION_CHANGED).clear();
        builder.withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, replaceDisk);
        builder.withEffect(EnchantmentEffectComponents.TICK, replaceDisk,
                // has a chance of about 90% to tick at least once every second, which should be enough
                LootItemRandomChanceCondition.randomChance(0.1F));
    }

    private static void modifyPower(Enchantment.Builder builder) {
        // Remove the arrow entity type check, so this also works for tridents.
        builder.getEffectsList(EnchantmentEffectComponents.DAMAGE).clear();
        builder.withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(0.5F)));
    }

    private static void modifyChanneling(Enchantment.Builder builder, RegistryOps.RegistryInfoLookup lookup) {
        // Allow entities attacking with a mace. Must be a smash attack; that is copied from the Wind Burst enchantment.
        LootItemCondition.Builder condition = AllOfCondition.allOf(WeatherCheck.weather().setThundering(true),
                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                                .located(LocationPredicate.Builder.location().setCanSeeSky(true))),
                AnyOfCondition.anyOf(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER,
                                EntityPredicate.Builder.entity()
                                        .of(lookup.lookup(Registries.ENTITY_TYPE).orElseThrow(), EntityTypes.TRIDENT)),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER,
                                EntityPredicate.Builder.entity()
                                        .equipment(EntityEquipmentPredicate.Builder.equipment()
                                                .mainhand(ItemPredicate.Builder.item()
                                                        .of(lookup.lookup(Registries.ITEM).orElseThrow(), Items.MACE)))
                                        .flags(EntityFlagsPredicate.Builder.flags().setIsFlying(false))
                                        .moving(MovementPredicate.fallDistance(MinMaxBounds.Doubles.atLeast(1.5))))));
        builder.getEffectsList(EnchantmentEffectComponents.POST_ATTACK).clear();
        builder.withEffect(EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                AllOf.entityEffects(new SummonEntityEffect(HolderSet.direct(EntityTypes.LIGHTNING_BOLT.builtInRegistryHolder()),
                                false),
                        new PlaySoundEffect(List.of(SoundEvents.TRIDENT_THUNDER),
                                ConstantFloat.of(5.0F),
                                ConstantFloat.of(1.0F))),
                condition);
    }
}
