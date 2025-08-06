package cn.ksmcbrigade.hipb.mixin;

import com.tacz.guns.client.event.ClientPreventGunClick;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPreventGunClick.class)
public class TACZClientInterMixin {
    @Inject(method = "onClickInput", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/event/InputEvent$InteractionKeyMappingTriggered;setSwingHand(Z)V"), cancellable = true,remap = false)
    private static void click(InputEvent.InteractionKeyMappingTriggered event, CallbackInfo ci) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult) {
            if (Minecraft.getInstance().level.getBlockState(hitResult.getBlockPos()).getBlock() instanceof FlowerPotBlock) {
                ci.cancel();
            }
        }
    }
}