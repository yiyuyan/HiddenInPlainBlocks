package cn.ksmcbrigade.hipb.mixin;

import cn.ksmcbrigade.hipb.entity.CustomBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowerPotBlock.class)
public abstract class BlockMixin extends Block implements EntityBlock {

    public BlockMixin(Properties p_60452_) {
        super(p_60452_);
    }

    @Unique
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_, @NotNull BlockState p_153216_){
        return new CustomBlockEntity(p_153215_,p_153216_);
    }

    @Inject(method = "use",at = @At(value = "HEAD"), cancellable = true)
    public void use(BlockState p_53540_, Level p_53541_, BlockPos p_53542_, Player p_53543_, InteractionHand p_53544_, BlockHitResult p_53545_, CallbackInfoReturnable<InteractionResult> cir){
        if(p_53543_.isShiftKeyDown()){
            cir.setReturnValue(InteractionResult.FAIL);
            cir.cancel();
        }
    }

    @Inject(method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
                    ,shift = At.Shift.BEFORE)
    )
    public void setBlock(BlockState p_53540_, Level p_53541_, BlockPos p_53542_, Player p_53543_, InteractionHand p_53544_, BlockHitResult p_53545_, CallbackInfoReturnable<InteractionResult> cir){
        BlockEntity entity = p_53541_.getBlockEntity(p_53542_);
        if(entity instanceof CustomBlockEntity customBlockEntity && customBlockEntity.hasStack()){
            BlockPos pos = p_53542_.above(1);
            ItemEntity entity1 = new ItemEntity(p_53541_,pos.getX(),pos .getY(),pos.getZ(),customBlockEntity.getStack());
            p_53541_.addFreshEntity(entity1);
            customBlockEntity.clearStack();
        }
    }

    @Unique
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState state2, boolean p_51542_) {
        BlockPos pos = blockPos.above(1);
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (entity instanceof CustomBlockEntity customBlockEntity && customBlockEntity.hasStack()) {
            ItemEntity entity1 = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), customBlockEntity.getStack());
            level.addFreshEntity(entity1);
            customBlockEntity.clearStack();
        }
        super.onRemove(state, level, blockPos, state2, p_51542_);
    }
}
