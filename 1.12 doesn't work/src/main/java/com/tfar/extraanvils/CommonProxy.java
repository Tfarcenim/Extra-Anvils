package com.tfar.extraanvils;


import com.tfar.extraanvils.generic.AnvilProperties;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import com.tfar.extraanvils.network.PacketHandler;
import com.tfar.extraanvils.vanilla.VanillaAnvils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber//(modid = ExtraAnvils.MODID)
public class CommonProxy {

  public void preInit(FMLPreInitializationEvent e){
    PacketHandler.registerMessages(ExtraAnvils.MODID);
  }

  public void init(FMLInitializationEvent e){
      NetworkRegistry.INSTANCE.registerGuiHandler(ExtraAnvils.instance, new GuiHandler());

      for (BlockGenericAnvil anvil : ExtraAnvils.anvils){
switch (anvil.variant) {
  case NORMAL:ExtraAnvils.anvilDamageMap
          .put(anvil,(BlockGenericAnvil)ForgeRegistries
                  .BLOCKS.getValue(new ResourceLocation(anvil.getRegistryName()+"_chipped")));break;
  case CHIPPED:ExtraAnvils.anvilDamageMap
          .put(anvil,(BlockGenericAnvil)ForgeRegistries
                  .BLOCKS.getValue(new ResourceLocation(anvil.getRegistryName().toString()
                          .replace(EnumVariants.CHIPPED.getString(), "")+EnumVariants.DAMAGED.getString())));break;
  case DAMAGED:ExtraAnvils.anvilDamageMap.put(anvil,null);break;
}
      }
    }

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {

    IForgeRegistry<Block> registry = event.getRegistry();

    for (EnumVariants variant : EnumVariants.values()) {
      registry.register(new BlockGenericAnvil(
              new AnvilProperties("stone", 20, .8,.5, .25), variant));
    }

    for (EnumVariants variant : EnumVariants.values()) {
      registry.register(new BlockGenericAnvil(
              new AnvilProperties("gold", 160, 2.8,1, 4), variant));
    }

    for (EnumVariants variant : EnumVariants.values()) {
      registry.register(new BlockGenericAnvil(
              new AnvilProperties("diamond", Integer.MAX_VALUE, .5,10, 64,1,true), variant));
    }

    }


  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    for (BlockGenericAnvil anvil : ExtraAnvils.anvils)
      registry.register(new ItemBlock(anvil).setRegistryName(anvil.getRegistryName()));
  }
}
