package com.tfar.extraanvils;

import com.tfar.extraanvils.entity.FallingAnvilEntity;
import com.tfar.extraanvils.entity.FallingAnvilRenderer;
import com.tfar.extraanvils.generic.GenericAnvilBlock;
import com.tfar.extraanvils.generic.GenericAnvilContainer;
import com.tfar.extraanvils.generic.GenericAnvilScreen;
import com.tfar.extraanvils.network.Message;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = ExtraAnvils.MODID)
public class ExtraAnvils
{
  public static final HashMap<GenericAnvilBlock, GenericAnvilBlock> anvilDamageMap = new HashMap<>();
  public static final String MODID = "extraanvils";

    public static ContainerType<GenericAnvilContainer> GENERIC_ANVIL;

    public static ItemGroup creativeTab = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ObjectHolders.diamond_anvil);
        }
    };


    public static Logger logger = LogManager.getLogger();

    public static ExtraAnvils instance;
    public static Set<GenericAnvilBlock> anvils = new HashSet<>();


    public ExtraAnvils(){
        instance = this;
      //  ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> GuiHandler::getClientGuiElement);
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
      Message.registerMessages(MODID);
    }


  @SubscribeEvent
  public static void setup(final FMLClientSetupEvent event) {
    ScreenManager.registerFactory(GENERIC_ANVIL, GenericAnvilScreen::new);
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

        ContainerType<GenericAnvilContainer> containerType;
        containerType = IForgeContainerType.create(GenericAnvilContainer::new);
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
      RenderingRegistry.registerEntityRenderingHandler(FallingAnvilEntity.class, FallingAnvilRenderer::new);
    }

  @ObjectHolder(value = MODID)
  public static class ObjectHolders {
    public static final EntityType<FallingAnvilEntity> falling_anvil_entity = null;
    public static final Block diamond_anvil = null;
    }
}
