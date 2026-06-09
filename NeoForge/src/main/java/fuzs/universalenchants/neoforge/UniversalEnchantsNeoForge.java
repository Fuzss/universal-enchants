package fuzs.universalenchants.neoforge;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.universalenchants.common.UniversalEnchants;
import fuzs.universalenchants.common.data.ModDatapackRegistriesProvider;
import fuzs.universalenchants.common.data.tags.ModBlockTagsProvider;
import fuzs.universalenchants.common.data.tags.ModEnchantmentTagsProvider;
import fuzs.universalenchants.common.data.tags.ModItemTagsProvider;
import fuzs.universalenchants.common.handler.BetterEnchantsHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

@Mod(UniversalEnchants.MOD_ID)
public class UniversalEnchantsNeoForge {

    public UniversalEnchantsNeoForge() {
        ModConstructor.construct(UniversalEnchants.MOD_ID, UniversalEnchants::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderHelper.registerDataProviders(UniversalEnchants.MOD_ID,
                ModDatapackRegistriesProvider::new,
                ModItemTagsProvider.Impl::new,
                ModEnchantmentTagsProvider.Impl::new,
                ModBlockTagsProvider::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.ADDITIONAL_DAMAGE_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModItemTagsProvider.Damage::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.ADDITIONAL_WEAPON_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModItemTagsProvider.Weapon::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.ADDITIONAL_RANGED_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModItemTagsProvider.Ranged::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.ADDITIONAL_SHIELD_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModItemTagsProvider.Shield::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.ADDITIONAL_ANIMAL_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModItemTagsProvider.Animal::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.COMPATIBLE_DAMAGE_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModEnchantmentTagsProvider.Damage::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.COMPATIBLE_MACE_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModEnchantmentTagsProvider.Mace::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.COMPATIBLE_BOW_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModEnchantmentTagsProvider.Bow::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.COMPATIBLE_CROSSBOW_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModEnchantmentTagsProvider.Crossbow::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.COMPATIBLE_PROTECTION_ENCHANTMENTS_ID,
                PackType.SERVER_DATA,
                ModEnchantmentTagsProvider.Protection::new);
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
}
