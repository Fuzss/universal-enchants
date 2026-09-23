package fuzs.universalenchants.common.data.tags;

import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v3.tags.AbstractTagsProvider;
import fuzs.universalenchants.common.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.level.block.Block;

public class ModBlockTagsProvider extends AbstractTagsProvider<Block> {

    public ModBlockTagsProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ModRegistry.FROSTED_ICE_REPLACEABLES_BLOCK_TAG)
                .add(BlockIds.WATER,
                        BlockIds.BUBBLE_COLUMN,
                        BlockItemIds.KELP.block(),
                        BlockIds.KELP_PLANT,
                        BlockItemIds.SEAGRASS.block(),
                        BlockIds.TALL_SEAGRASS);
    }
}
