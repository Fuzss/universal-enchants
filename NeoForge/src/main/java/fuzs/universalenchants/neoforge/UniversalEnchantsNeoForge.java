package fuzs.universalenchants.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.universalenchants.UniversalEnchants;
import fuzs.universalenchants.data.ModBlockTagProvider;
import fuzs.universalenchants.data.ModEnchantmentTagProvider;
import fuzs.universalenchants.data.ModItemTagProvider;
import fuzs.universalenchants.data.ModRegistriesDatapackProvider;
import fuzs.universalenchants.data.tags.ModBlockTagsProvider;
import fuzs.universalenchants.data.tags.ModEnchantmentTagsProvider;
import fuzs.universalenchants.data.tags.ModItemTagsProvider;
import fuzs.universalenchants.handler.BetterEnchantsHandler;
import fuzs.universalenchants.init.ModRegistry;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

@Mod(UniversalEnchants.MOD_ID)
public class UniversalEnchantsNeoForge {

    public UniversalEnchantsNeoForge() {
        ModConstructor.construct(UniversalEnchants.MOD_ID, UniversalEnchants::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderHelper.registerDataProviders(UniversalEnchants.MOD_ID,
                ModItemTagProvider::new,
                ModBlockTagProvider::new,
                ModEnchantmentTagProvider::new,
                ModRegistriesDatapackProvider::new);
        DataProviderHelper.registerDataProviders(UniversalEnchants.MOD_ID,
                ModRegistry.REGISTRY_SET_BUILDER,
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

    @Deprecated
    private static void registerEventHandlers(IEventBus eventBus) {
        eventBus.addListener((final LivingGetProjectileEvent evt) -> {
            MutableValue<ItemStack> ammoItemStack = MutableValue.fromEvent(evt::setProjectileItemStack,
                    evt::getProjectileItemStack);
            BetterEnchantsHandler.onGetProjectile(evt.getEntity(), evt.getProjectileWeaponItemStack(), ammoItemStack);
        });
    }
}
