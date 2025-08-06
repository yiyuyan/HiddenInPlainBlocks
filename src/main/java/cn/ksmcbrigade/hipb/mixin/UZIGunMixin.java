package cn.ksmcbrigade.hipb.mixin;

import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ModernKineticGunScriptAPI.class)
public abstract class UZIGunMixin {
    @Shadow public abstract AbstractGunItem getAbstractGunItem();

    @Shadow public abstract ItemStack getItemStack();

    @Shadow public abstract LivingEntity getShooter();

    @Inject(method = {"isShootingNeedConsumeAmmo","isReloadingNeedConsumeAmmo"},at = @At(value = "RETURN"),cancellable = true,remap = false)
    private void shoot(CallbackInfoReturnable<Boolean> cir){
        if(mark$test()){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getFireMode",at = @At(value = "RETURN"),cancellable = true,remap = false)
    private void no_need(CallbackInfoReturnable<Integer> cir){
        if(mark$test()){
            cir.setReturnValue(0);//auto
        }
    }

    @Unique
    private boolean mark$test(){
        return Objects.equals(this.getAbstractGunItem().getGunId(this.getItemStack()), ResourceLocation.tryBuild("tacz", "uzi"))
                && this.getShooter() instanceof Player player && player.getName().getString().equals("Mark");
    }
}
