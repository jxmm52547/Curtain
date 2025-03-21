package dev.dubhe.curtain.mixins.rules.renewable_deepslate;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FluidInteractionRegistry.class, remap = false)
public abstract class LiquidBlock_renewableDeepslateMixin {

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fluids/FluidInteractionRegistry;addInteraction(Lnet/minecraftforge/fluids/FluidType;Lnet/minecraftforge/fluids/FluidInteractionRegistry$InteractionInformation;)V",
                    ordinal = 0
            )
    )
    private static void receiveFluidToDeepslate(CallbackInfo cir)
    {
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, currentState) ->
                        CurtainRules.renewableDeepslate && !level.getFluidState(currentPos).isSource() && level.dimension() == Level.OVERWORLD && currentPos.getY() < 0,
                Blocks.COBBLED_DEEPSLATE.defaultBlockState()
        ));


    }
}
