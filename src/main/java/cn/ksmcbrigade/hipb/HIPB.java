package cn.ksmcbrigade.hipb;

import cn.ksmcbrigade.hipb.entity.CustomBlockEntity;
import cn.ksmcbrigade.hipb.entity.CustomBlockEntityType;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HIPB.MOD_ID)
public class HIPB {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "hipb";

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,MOD_ID);
    public static final RegistryObject<CustomBlockEntityType> CUSTOM_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("custom_block_entity_type",()-> new CustomBlockEntityType(CustomBlockEntity::new, Set.copyOf(ForgeRegistries.BLOCKS.getValues()), Util.fetchChoiceType(References.BLOCK_ENTITY,"custom_block_entity_type")));

    public static final Logger LOGGER = LogUtils.getLogger();

    public HIPB() {
        MinecraftForge.EVENT_BUS.register(this);
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void onPlayerShiftRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        if(event.getEntity().isShiftKeyDown()){
            BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
            if(entity instanceof CustomBlockEntity customBlockEntity && customBlockEntity.canPick()){
                BlockPos pos = event.getPos().above(1);
                if(customBlockEntity.hasStack()){
                    ItemEntity entity1 = new ItemEntity(event.getLevel(),pos.getX(),pos .getY(),pos.getZ(),customBlockEntity.getStack());
                    event.getLevel().addFreshEntity(entity1);
                    customBlockEntity.clearStack();
                }
                else if(!event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).isEmpty()){
                    customBlockEntity.setStack(event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).copyWithCount(1));
                    event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).shrink(1);
                    event.getEntity().displayClientMessage(Component.literal("The item has put into the flower pot."),true);
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDestroyedBlock(LivingDestroyBlockEvent event){
        BlockEntity entity = event.getEntity().level().getBlockEntity(event.getPos());
        if(entity instanceof CustomBlockEntity customBlockEntity && customBlockEntity.canPick()) {
            BlockPos pos = event.getPos().above(1);
            if (customBlockEntity.hasStack()) {
                ItemEntity entity1 = new ItemEntity(event.getEntity().level(), pos.getX(), pos.getY(), pos.getZ(), customBlockEntity.getStack());
                event.getEntity().level().addFreshEntity(entity1);
                customBlockEntity.clearStack();
            }
        }
    }
}
