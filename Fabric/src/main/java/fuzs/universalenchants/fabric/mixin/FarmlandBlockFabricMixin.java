package fuzs.universalenchants.fabric.mixin;

import fuzs.universalenchants.common.handler.BetterEnchantsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
abstract class FarmlandBlockFabricMixin extends Block {

    public FarmlandBlockFabricMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo callback) {
        if (level instanceof ServerLevel serverLevel) {
            if (BetterEnchantsHandler.onFarmlandTrample(serverLevel,
                    pos,
                    Blocks.DIRT.defaultBlockState(),
                    fallDistance,
                    entity).isInterrupt()) {
                super.fallOn(level, state, pos, entity, fallDistance);
                callback.cancel();
            }
        }
    }
}
