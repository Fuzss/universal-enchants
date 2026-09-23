package fuzs.universalenchants.neoforge;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import fuzs.universalenchants.common.UniversalEnchants;
import fuzs.universalenchants.common.data.tags.ModBlockTagsProvider;
import fuzs.universalenchants.common.data.tags.ModEnchantmentTagsProvider;
import fuzs.universalenchants.common.data.tags.ModItemTagsProvider;
import fuzs.universalenchants.common.handler.BetterEnchantsHandler;
import fuzs.universalenchants.common.handler.ModifyEnchantmentsHandler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;

@Mod(UniversalEnchants.MOD_ID)
public class UniversalEnchantsNeoForge {

    public UniversalEnchantsNeoForge() {
        ModConstructor.construct(UniversalEnchants.MOD_ID, UniversalEnchants::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderBuilder.of(UniversalEnchants.MOD_ID)
                .addProvider(ModItemTagsProvider.Impl::new,
                        ModEnchantmentTagsProvider.Impl::new,
                        ModBlockTagsProvider::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.ADDITIONAL_DAMAGE_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModItemTagsProvider.Damage::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.ADDITIONAL_WEAPON_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModItemTagsProvider.Weapon::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.ADDITIONAL_RANGED_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModItemTagsProvider.Ranged::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.ADDITIONAL_SHIELD_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModItemTagsProvider.Shield::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.ADDITIONAL_ANIMAL_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModItemTagsProvider.Animal::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.COMPATIBLE_DAMAGE_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModEnchantmentTagsProvider.Damage::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.COMPATIBLE_MACE_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModEnchantmentTagsProvider.Mace::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.COMPATIBLE_BOW_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModEnchantmentTagsProvider.Bow::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.COMPATIBLE_CROSSBOW_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModEnchantmentTagsProvider.Crossbow::new);
        DataProviderBuilder.ofBuiltIn(UniversalEnchants.COMPATIBLE_PROTECTION_ENCHANTMENTS_ID, PackType.SERVER_DATA)
                .addProvider(ModEnchantmentTagsProvider.Protection::new);
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        eventBus.addListener((final BlockEvent.FarmlandTrampleEvent evt) -> {
            if (!(evt.getLevel() instanceof ServerLevel serverLevel)) {
                return;
            }

            if (BetterEnchantsHandler.onFarmlandTrample(serverLevel,
                    evt.getPos(),
                    evt.getState(),
                    evt.getFallDistance(),
                    evt.getEntity()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static Enchantment modifyEnchantment(ResourceKey<Enchantment> key, Enchantment enchantment, RegistryOps.RegistryInfoLookup lookup) {
        Enchantment.Builder builder = Enchantment.enchantment(enchantment.definition());
        builder.exclusiveWith(enchantment.exclusiveSet());
        // copy the original effects so the builder starts out as a copy of the enchantment being modified
        builder.effectMapBuilder.addAll(enchantment.effects());
        enchantment.effects().forEach((TypedDataComponent<?> component) -> {
            if (component.value() instanceof List<?> valueList) {
                builder.getEffectsList((DataComponentType<List<Object>>) component.type()).addAll(valueList);
            }
        });
        if (ModifyEnchantmentsHandler.modifyEnchantment(key, builder, lookup)) {
            // keep the original description instead of deriving it from the resource key
            return new Enchantment(enchantment.description(),
                    enchantment.definition(),
                    enchantment.exclusiveSet(),
                    builder.effectMapBuilder.build());
        } else {
            return null;
        }
    }
}
