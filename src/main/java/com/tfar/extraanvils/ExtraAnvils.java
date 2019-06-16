package com.tfar.extraanvils;

import com.tfar.extraanvils.gold.GenericAnvilContainer;
import com.tfar.extraanvils.gold.GenericAnvilScreen;
import com.tfar.extraanvils.network.Message;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = ExtraAnvils.MODID)
public class ExtraAnvils
{

    public static final String MODID = "extraanvils";

    @ObjectHolder(MODID+":generic_anvil_container_type")
    public static ContainerType<?> GENERIC_ANVIL;

    public static ItemGroup creativeTab = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModAnvils.genericAnvilBlock);
        }
    };


    public static Logger logger;

    public static ExtraAnvils instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);


    public ExtraAnvils(){
        instance = this;
      //  ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> GuiHandler::getClientGuiElement);
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
        Message.registerMessages(MODID);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModAnvils.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModAnvils.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event){

        ContainerType<GenericAnvilContainer> obj = new ContainerType<>(GenericAnvilContainer::new);
        obj.setRegistryName(MODID+":generic_anvil_container_type");
        event.getRegistry().register(obj);
        ScreenManager.registerFactory(obj, GenericAnvilScreen::new);

    }
}
