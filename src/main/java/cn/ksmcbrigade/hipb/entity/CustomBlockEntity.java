package cn.ksmcbrigade.hipb.entity;

import cn.ksmcbrigade.hipb.HIPB;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CustomBlockEntity extends BlockEntity {

    private ItemStack stack = ItemStack.EMPTY;
    private int pickTick = 2;

    public CustomBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(HIPB.CUSTOM_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        p_187471_.put("hipb_item",stack.save(new CompoundTag()));
    }

    @Override
    public void load(@NotNull CompoundTag p_155245_) {
        super.load(p_155245_);
        if(p_155245_.contains("hipb_item")){
            this.stack = ItemStack.of(p_155245_.getCompound("hipb_item"));

        }
    }

    public boolean canPick(){
        if(pickTick>0){
            pickTick--;
            return false;
        }
        else{
            pickTick = 2;
            return true;
        }
    }

    public void setStack(@NotNull ItemStack stack){
        this.stack = stack;
        this.pickTick = 2;
    }

    public ItemStack getStack(){
        return this.stack;
    }

    public void clearStack(){
        this.stack = ItemStack.EMPTY;
    }

    public boolean hasStack(){
        return !this.stack.isEmpty();
    }
}
