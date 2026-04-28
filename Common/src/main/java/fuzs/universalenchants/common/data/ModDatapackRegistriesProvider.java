package fuzs.universalenchants.common.data;

import fuzs.puzzleslib.common.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.universalenchants.common.init.ModRegistry;
import net.minecraft.core.registries.Registries;

public class ModDatapackRegistriesProvider extends AbstractDatapackRegistriesProvider {

    public ModDatapackRegistriesProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBootstrap(RegistryBoostrapConsumer consumer) {
        consumer.add(Registries.ENCHANTMENT, ModRegistry::boostrapEnchantments);
    }
}
