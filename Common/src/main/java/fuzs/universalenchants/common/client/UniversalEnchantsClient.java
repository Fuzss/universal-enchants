package fuzs.universalenchants.common.client;

import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.event.v1.ClientTagsUpdatedCallback;
import fuzs.puzzleslib.common.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.universalenchants.common.client.handler.ItemTooltipHandler;
import fuzs.universalenchants.common.handler.ItemCompatHandler;

public class UniversalEnchantsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemTooltipCallback.EVENT.register(ItemTooltipHandler::onItemTooltip);
        ClientTagsUpdatedCallback.EVENT.register(ItemCompatHandler::onTagsUpdated);
    }
}
