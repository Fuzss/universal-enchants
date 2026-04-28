package fuzs.universalenchants.common.mixin.accessor;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Enchantment.class)
public interface EnchantmentAccessor {
    @Accessor("exclusiveSet")
    @Mutable
    void universalenchants$setExclusiveSet(HolderSet<Enchantment> exclusiveSet);
}
