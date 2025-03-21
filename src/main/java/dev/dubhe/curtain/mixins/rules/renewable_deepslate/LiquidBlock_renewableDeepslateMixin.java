package dev.dubhe.curtain.mixins.rules.renewable_deepslate;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlock_renewableDeepslateMixin {

    @Shadow protected abstract void fizz(LevelAccessor world, BlockPos pos);

    @Inject(method = "shouldSpreadLiquid", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/material/FluidState;isSource()Z"), cancellable = true)
    private void receiveFluidToDeepslate(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir)
    {
        System.out.println(CurtainRules.renewableDeepslate);
        System.out.println(!world.getFluidState(pos).isSource());
        System.out.println(world.dimension() == Level.OVERWORLD);
        System.out.println(pos.getY() < 0);

        if(CurtainRules.renewableDeepslate && !world.getFluidState(pos).isSource() && world.dimension() == Level.OVERWORLD && pos.getY() < 0)
        {
            world.setBlockAndUpdate(pos, Blocks.COBBLED_DEEPSLATE.defaultBlockState());
            this.fizz(world, pos);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
