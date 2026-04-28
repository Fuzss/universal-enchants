package fuzs.universalenchants.fabric.client;

import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.universalenchants.common.UniversalEnchants;
import fuzs.universalenchants.common.client.UniversalEnchantsClient;
import net.fabricmc.api.ClientModInitializer;

public class UniversalEnchantsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(UniversalEnchants.MOD_ID, UniversalEnchantsClient::new);
    }
}
