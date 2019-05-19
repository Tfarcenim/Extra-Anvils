package com.tfar.extraanvils;


import com.tfar.extraanvils.generic.BlockGenericAnvil;
import com.tfar.extraanvils.network.PacketHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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

@Mod.EventBusSubscriber//(modid = ExtraAnvils.MODID)
public class CommonProxy {

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

  public void preInit(FMLPreInitializationEvent e) {

    PacketHandler.registerMessages(ExtraAnvils.MODID);
  }

  public void init(FMLInitializationEvent e) {
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
  }


  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    for (BlockGenericAnvil anvil : ExtraAnvils.anvils)
      registry.register(new ItemBlock(anvil).setRegistryName(anvil.getRegistryName()));
  }
}
