package com.tfar.extraanvils;

import com.tfar.anviltweaks.AnvilTile;
import com.tfar.anviltweaks.AnvilTweaks;
import com.tfar.extraanvils.compat.AnvilTweaksCompat;
import com.tfar.extraanvils.compat.ApothCompat;
import com.tfar.extraanvils.compat.Compat;
import com.tfar.extraanvils.entity.FallingAnvilEntity;
import com.tfar.extraanvils.generic.*;
import com.tfar.extraanvils.network.Message;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.tfar.extraanvils.ExtraAnvils.ObjectHolders.generic_anvil_container_type;
import static com.tfar.extraanvils.compat.Compat.isAnvilTweaksHere;
import static com.tfar.extraanvils.compat.Compat.isApothesisHere;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = ExtraAnvils.MODID)
public class ExtraAnvils
{
  public static final HashMap<GenericAnvilBlock, GenericAnvilBlock> anvilDamageMap = new HashMap<>();
  public static final String MODID = "extraanvils";

  @ObjectHolder("apotheosis:splitting")
  public static final Enchantment splitting = null;

  public static ItemGroup creativeTab = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ObjectHolders.diamond_anvil);
        }
    };


    public static Logger logger = LogManager.getLogger();

    public static Set<GenericAnvilBlock> anvils = new HashSet<>();

    public ExtraAnvils(){
      //  ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> GuiHandler::getClientGuiElement);
    }

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
      Message.registerMessages(MODID);
    }

  @SubscribeEvent
  public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> event) {
    if (isAnvilTweaksHere)AnvilTweaksCompat.hax(event);
    else if(isApothesisHere) ApothCompat.hax(event);
  }

  @SubscribeEvent
  public static void clientSetup(final FMLClientSetupEvent event) {
    ScreenManager.registerFactory(generic_anvil_container_type, GenericAnvilScreen::new);
  }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        RegistryHandler.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        RegistryHandler.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event){

        ContainerType<AbstractGenericAnvilContainer> containerType;
        containerType = IForgeContainerType.create((int id, PlayerInventory playerInventory, PacketBuffer data) -> isAnvilTweaksHere ? new GenericAnvilAnvilTweaksContainer(id, playerInventory, data.readBlockPos()):
        new GenericAnvilContainer(id, playerInventory, data.readBlockPos()));
        containerType.setRegistryName(MODID+":generic_anvil_container_type");
        event.getRegistry().register(containerType);
    }

    @SubscribeEvent
  public static void registerEntities(RegistryEvent.Register<EntityType<?>> event){
      event.getRegistry().register(
              EntityType.Builder
                      .<FallingAnvilEntity>create(FallingAnvilEntity::new, EntityClassification.MISC)
                      .setShouldReceiveVelocityUpdates(true)
                      .setUpdateInterval(1)
                      .setTrackingRange(128)
                      .size(.98f, .98f)
                      .setCustomClientFactory((spawnEntity, world) -> ObjectHolders.falling_anvil_entity.create(world))
                      .build(MODID+":falling_anvil_entity")
                      .setRegistryName(MODID+":falling_anvil_entity"));
    }
    @SubscribeEvent
    public static void client(FMLClientSetupEvent event){
      RenderingRegistry.registerEntityRenderingHandler(FallingAnvilEntity.class, FallingBlockRenderer::new);
    }

  public static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
    register(obj,MODID,name,registry);
  }

  public static <T extends IForgeRegistryEntry<T>> void register(T obj,String modid, String name, IForgeRegistry<T> registry) {
    registry.register(obj.setRegistryName(new ResourceLocation(modid, name)));
  }



  @ObjectHolder(value = MODID)
  public static class ObjectHolders {
    public static final EntityType<FallingAnvilEntity> falling_anvil_entity = null;
    public static final Block diamond_anvil = null;
    public static final ContainerType<? extends AbstractGenericAnvilContainer> generic_anvil_container_type = null;
  }
}
