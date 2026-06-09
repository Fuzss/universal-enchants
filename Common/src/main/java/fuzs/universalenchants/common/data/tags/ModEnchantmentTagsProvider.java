package fuzs.universalenchants.common.data.tags;

import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import fuzs.universalenchants.common.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public abstract class ModEnchantmentTagsProvider extends AbstractTagProvider<Enchantment> {

    public ModEnchantmentTagsProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @SafeVarargs
    protected final void addExclusiveEnchantments(ResourceKey<Enchantment> primaryEnchantment, ResourceKey<Enchantment>... secondaryEnchantments) {
        for (ResourceKey<Enchantment> secondaryEnchantment : secondaryEnchantments) {
            this.addExclusiveEnchantments(primaryEnchantment, secondaryEnchantment);
        }
    }

    protected final void addExclusiveEnchantments(ResourceKey<Enchantment> primaryEnchantment, ResourceKey<Enchantment> secondaryEnchantment) {
        this.tag(ModRegistry.getExclusiveSetEnchantmentTag(primaryEnchantment)).addKey(secondaryEnchantment);
        this.tag(ModRegistry.getExclusiveSetEnchantmentTag(secondaryEnchantment)).addKey(primaryEnchantment);
    }

    @SafeVarargs
    protected final void addInclusiveEnchantments(ResourceKey<Enchantment> primaryEnchantment, ResourceKey<Enchantment>... secondaryEnchantments) {
        for (ResourceKey<Enchantment> secondaryEnchantment : secondaryEnchantments) {
            this.addInclusiveEnchantments(primaryEnchantment, secondaryEnchantment);
        }
    }

    protected final void addInclusiveEnchantments(ResourceKey<Enchantment> primaryEnchantment, ResourceKey<Enchantment> secondaryEnchantment) {
        this.tag(ModRegistry.getInclusiveSetEnchantmentTag(primaryEnchantment)).addKey(secondaryEnchantment);
        this.tag(ModRegistry.getInclusiveSetEnchantmentTag(secondaryEnchantment)).addKey(primaryEnchantment);
    }

    public static class Impl extends ModEnchantmentTagsProvider {

        public Impl(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider registries) {
            this.addExclusiveEnchantments(Enchantments.WIND_BURST, Enchantments.CHANNELING);
        }
    }

    public static class Damage extends ModEnchantmentTagsProvider {

        public Damage(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider registries) {
            this.addInclusiveEnchantments(Enchantments.SHARPNESS,
                    Enchantments.SMITE,
                    Enchantments.BANE_OF_ARTHROPODS,
                    Enchantments.IMPALING,
                    Enchantments.BREACH);
        }
    }

    public static class Mace extends ModEnchantmentTagsProvider {

        public Mace(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider registries) {
            this.addInclusiveEnchantments(Enchantments.DENSITY,
                    Enchantments.BREACH,
                    Enchantments.SHARPNESS,
                    Enchantments.SMITE,
                    Enchantments.BANE_OF_ARTHROPODS,
                    Enchantments.IMPALING);
        }
    }

    public static class Bow extends ModEnchantmentTagsProvider {

        public Bow(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider registries) {
            this.addInclusiveEnchantments(Enchantments.INFINITY, Enchantments.MENDING);
        }
    }

    public static class Crossbow extends ModEnchantmentTagsProvider {

        public Crossbow(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider registries) {
            this.addInclusiveEnchantments(Enchantments.MULTISHOT, Enchantments.PIERCING);
        }
    }

    public static class Protection extends ModEnchantmentTagsProvider {

        public Protection(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider registries) {
            this.addInclusiveEnchantments(Enchantments.PROTECTION,
                    Enchantments.BLAST_PROTECTION,
                    Enchantments.FIRE_PROTECTION,
                    Enchantments.PROJECTILE_PROTECTION);
        }
    }
}
