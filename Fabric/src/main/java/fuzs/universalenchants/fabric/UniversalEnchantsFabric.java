package fuzs.universalenchants.fabric;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.universalenchants.common.UniversalEnchants;
import net.fabricmc.api.ModInitializer;

public class UniversalEnchantsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(UniversalEnchants.MOD_ID, UniversalEnchants::new);
    }
}
