package fuzs.universalenchants.data.tags;

import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v3.tags.AbstractTagAppender;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import fuzs.universalenchants.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.Consumer;

public abstract class ModItemTagsProvider extends AbstractTagProvider<Item> {

    public ModItemTagsProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @SafeVarargs
    protected final void addSupportedItems(String tagKey, ResourceKey<Enchantment>... enchantments) {
        this.addSupportedItems((AbstractTagAppender<Item> tagAppender) -> {
            tagAppender.addOptionalTag(tagKey);
        }, enchantments);
    }

    @SafeVarargs
    protected final void addSupportedItems(TagKey<Item> tagKey, ResourceKey<Enchantment>... enchantments) {
        this.addSupportedItems((AbstractTagAppender<Item> tagAppender) -> {
            tagAppender.addTag(tagKey);
        }, enchantments);
    }

    @SafeVarargs
    protected final void addSupportedItems(Consumer<AbstractTagAppender<Item>> consumer, ResourceKey<Enchantment>... enchantments) {
        for (ResourceKey<Enchantment> enchantment : enchantments) {
            TagKey<Item> tagKey = ModRegistry.getSecondaryEnchantableItemTag(enchantment);
            consumer.accept(this.tag(tagKey));
        }
    }

    public static class Impl extends ModItemTagsProvider {

        public Impl(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider provider) {
            this.addSupportedItems("c:armors", Enchantments.THORNS);
        }
    }

    public static class Damage extends ModItemTagsProvider {

        public Damage(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider provider) {
            // All weapon items should support basic damage enchantments; these include the following:
            // Sharpness, Smite, Bane of Arthropods, Impaling, Breach
            this.addSupportedItems(ItemTags.SWORDS, Enchantments.IMPALING, Enchantments.BREACH);
            this.addSupportedItems(ItemTags.AXES,
                    Enchantments.SHARPNESS,
                    Enchantments.SMITE,
                    Enchantments.BANE_OF_ARTHROPODS,
                    Enchantments.IMPALING,
                    Enchantments.BREACH);
            this.addSupportedItems("c:tools/trident",
                    Enchantments.SHARPNESS,
                    Enchantments.SMITE,
                    Enchantments.BANE_OF_ARTHROPODS,
                    Enchantments.BREACH);
            this.addSupportedItems("c:tools/mace",
                    Enchantments.SHARPNESS,
                    Enchantments.SMITE,
                    Enchantments.BANE_OF_ARTHROPODS,
                    Enchantments.IMPALING);
            this.addSupportedItems(ItemTags.SPEARS, Enchantments.IMPALING, Enchantments.BREACH);
        }
    }

    public static class Weapon extends ModItemTagsProvider {

        public Weapon(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider provider) {
            // There are some more specialized weapon enchantments, which are generally supported on all weapons, but may have some variations:
            // Looting, Knockback, Fire Aspect
            this.addSupportedItems(ItemTags.AXES,
                    Enchantments.LOOTING,
                    Enchantments.KNOCKBACK,
                    Enchantments.FIRE_ASPECT);
            // Do not add Fire Aspect, it does not fit well with the aquatic theme of tridents.
            // Some crossbow enchantments are supported by thrown tridents.
            this.addSupportedItems("c:tools/trident",
                    Enchantments.LOOTING,
                    Enchantments.KNOCKBACK,
                    Enchantments.QUICK_CHARGE,
                    Enchantments.PIERCING);
            // Do not add Knockback, since maces already have their own knock back mechanic with an exclusive enchantment.
            // Fire Aspect is already supported in vanilla.
            this.addSupportedItems("c:tools/mace", Enchantments.LOOTING, Enchantments.CHANNELING);
        }
    }

    public static class Ranged extends ModItemTagsProvider {

        public Ranged(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider provider) {
            this.addSupportedItems("c:tools/bow",
                    Enchantments.PIERCING,
                    Enchantments.MULTISHOT,
                    Enchantments.QUICK_CHARGE,
                    Enchantments.LOOTING);
            this.addSupportedItems("c:tools/crossbow",
                    Enchantments.FLAME,
                    Enchantments.PUNCH,
                    Enchantments.POWER,
                    Enchantments.INFINITY,
                    Enchantments.LOOTING);
        }
    }

    public static class Shield extends ModItemTagsProvider {

        public Shield(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider provider) {
            this.addSupportedItems("c:tools/shield",
                    Enchantments.THORNS,
                    Enchantments.KNOCKBACK,
                    Enchantments.FIRE_ASPECT);
        }
    }

    public static class Animal extends ModItemTagsProvider {

        public Animal(DataProviderContext context) {
            super(context);
        }

        @Override
        public void addTags(HolderLookup.Provider provider) {
            this.addSupportedItemsForLand("c:armors/horse");
            this.addSupportedItemsForLand("c:armors/wolf");
            this.addSupportedItemsForWater("c:armors/nautilus");
        }

        private void addSupportedItemsForLand(String tagKey) {
            this.addSupportedItems(tagKey,
                    Enchantments.PROTECTION,
                    Enchantments.FIRE_PROTECTION,
                    Enchantments.FEATHER_FALLING,
                    Enchantments.BLAST_PROTECTION,
                    Enchantments.PROJECTILE_PROTECTION,
                    Enchantments.RESPIRATION,
                    Enchantments.THORNS,
                    Enchantments.DEPTH_STRIDER,
                    Enchantments.FROST_WALKER,
                    Enchantments.BINDING_CURSE,
                    Enchantments.SOUL_SPEED,
                    Enchantments.VANISHING_CURSE);
        }

        private void addSupportedItemsForWater(String tagKey) {
            this.addSupportedItems(tagKey,
                    Enchantments.PROTECTION,
                    Enchantments.FIRE_PROTECTION,
                    Enchantments.FEATHER_FALLING,
                    Enchantments.BLAST_PROTECTION,
                    Enchantments.PROJECTILE_PROTECTION,
                    Enchantments.THORNS,
                    Enchantments.BINDING_CURSE,
                    Enchantments.VANISHING_CURSE);
        }
    }
}
