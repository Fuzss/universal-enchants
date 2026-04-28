plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modCompileOnlyApi(sharedLibs.puzzleslib.common)
}

multiloader {
    mixins {
        mixin("LivingEntityMixin", "PlayerMixin", "ThrownTridentMixin")
        accessor("Enchantment\u0024EnchantmentDefinitionAccessor", "EnchantmentAccessor")
    }
}
