package fuzs.universalenchants.mixin.accessor;

import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Optional;

@Mixin(Enchantment.EnchantmentDefinition.class)
public interface Enchantment$EnchantmentDefinitionAccessor {
    @Accessor("supportedItems")
    @Mutable
    void universalenchants$setSupportedItems(HolderSet<Item> supportedItems);

    @Accessor("primaryItems")
    @Mutable
    void universalenchants$setPrimaryItems(Optional<HolderSet<Item>> primaryItems);

    @Accessor("slots")
    @Mutable
    void universalenchants$setSlots(List<EquipmentSlotGroup> slots);
}
