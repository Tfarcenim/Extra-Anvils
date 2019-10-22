package com.tfar.extraanvils;

import com.tfar.extraanvils.aether.EntityAetherAnvil;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import com.tfar.extraanvils.generic.EntityFallingAnvil;
import com.tfar.extraanvils.network.PacketHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = ExtraAnvils.MODID, name = ExtraAnvils.NAME, version = ExtraAnvils.MODVERSION/*, dependencies = "required-after:forge@[14.23.5.2796)"*/)
public class ExtraAnvils {
  public static final String MODID = "extraanvils";
  public static final String NAME = "Extra Anvils";
  public static final String MODVERSION = "@VERSION@";

  private static final boolean developerEnvironment = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");


  public static final HashMap<BlockGenericAnvil, BlockGenericAnvil> anvilDamageMap = new HashMap<>();

  public static final List<BlockGenericAnvil> anvils = new ArrayList<>();

  @SuppressWarnings("ConstantConditions")
  public static CreativeTabs creativeTab = new CreativeTabs(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(ObjectHolders.diamond_anvil);
    }
  };

  public static Logger logger = LogManager.getLogger();

  @Mod.Instance
  public static ExtraAnvils instance;

  @Mod.EventHandler
  public void preInit(final FMLPreInitializationEvent event) {
    PacketHandler.registerMessages(ExtraAnvils.MODID);
  }

  @Mod.EventHandler
  public void init(final FMLInitializationEvent event) {

    NetworkRegistry.INSTANCE.registerGuiHandler(ExtraAnvils.instance, new GuiHandler());

    for (BlockGenericAnvil anvil : ExtraAnvils.anvils) {
      switch (anvil.variant) {
        case NORMAL:
          ExtraAnvils.anvilDamageMap
                  .put(anvil, (BlockGenericAnvil) ForgeRegistries
                          .BLOCKS.getValue(new ResourceLocation(anvil.getRegistryName() + "_chipped")));
          break;
        case CHIPPED:
          ExtraAnvils.anvilDamageMap
                  .put(anvil, (BlockGenericAnvil) ForgeRegistries
                          .BLOCKS.getValue(new ResourceLocation(anvil.getRegistryName().toString()
                                  .replace(EnumVariants.CHIPPED.getString(), "") + EnumVariants.DAMAGED.getString())));
          break;
        case DAMAGED:
          ExtraAnvils.anvilDamageMap.put(anvil, null);
          break;
      }
    }

    //    for (Item item : ForgeRegistries.ITEMS){
    //        if (item instanceof ItemArmor)System.out.println(item.getRegistryName().toString() +" "+ item.getItemEnchantability());
    //    }
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    for (BlockGenericAnvil anvil : ExtraAnvils.anvils) {
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(anvil), 0, new ModelResourceLocation(anvil.getRegistryName(), "inventory"));
    }
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    for (BlockGenericAnvil anvil : ExtraAnvils.anvils)
      registry.register(new ItemBlock(anvil).setRegistryName(anvil.getRegistryName()));
  }

  @SubscribeEvent
  public static void registerEntity(RegistryEvent.Register<EntityEntry> e) {

    final ResourceLocation resourceLocation = new ResourceLocation(ExtraAnvils.MODID, "falling_anvil");
    final ResourceLocation resourceLocation2 = new ResourceLocation(ExtraAnvils.MODID, "aether_anvil");


    e.getRegistry().register(
            EntityEntryBuilder.create()
                    .entity(EntityFallingAnvil.class)
                    .id(resourceLocation, 0)
                    .name(resourceLocation.getPath())
                    .tracker(64, 1, true)
                    .build());

    e.getRegistry().register(
            EntityEntryBuilder.create()
                    .entity(EntityAetherAnvil.class)
                    .id(resourceLocation2, 1)
                    .name(resourceLocation2.getPath())
                    .tracker(64, 1, true)
                    .build());
  }
}
