package cn.ksmcbrigade.hipb.entity;

import com.mojang.datafixers.types.Type;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class CustomBlockEntityType extends BlockEntityType<CustomBlockEntity> {
    public CustomBlockEntityType(BlockEntitySupplier<? extends CustomBlockEntity> p_155259_, Set<Block> p_155260_, Type<?> p_155261_) {
        super(p_155259_, p_155260_, p_155261_);
    }
}
