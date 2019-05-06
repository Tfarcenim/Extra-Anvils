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
    }

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {

    IForgeRegistry<Block> registry = event.getRegistry();

    for (EnumVariants variant : EnumVariants.values()) {
      registry.register(new BlockGenericAnvil(
              new AnvilProperties("stone", 20, .8,1, .25), variant));
    }

    for (EnumVariants variant : EnumVariants.values()) {
      registry.register(new BlockGenericAnvil(
              new AnvilProperties("gold", 160, 2.8,1, 4), variant));
    }

    for (EnumVariants variant : EnumVariants.values()) {
      registry.register(new BlockGenericAnvil(
              new AnvilProperties("diamond", Integer.MAX_VALUE, .5,1, 64), variant));
    }

    }


  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    for (BlockGenericAnvil anvil : ExtraAnvils.anvils)
      registry.register(new ItemBlock(anvil).setRegistryName(anvil.getRegistryName()));

  /*  registry.register(new ItemBlock(VanillaAnvils.blockDiamondAnvil).setRegistryName(VanillaAnvils.blockDiamondAnvil.getRegistryName()));
    registry.register(new ItemBlock(VanillaAnvils.blockDiamondAnvilChipped).setRegistryName(VanillaAnvils.blockDiamondAnvilChipped.getRegistryName()));
    registry.register(new ItemBlock(VanillaAnvils.blockDiamondAnvilDamaged).setRegistryName(VanillaAnvils.blockDiamondAnvilDamaged.getRegistryName()));

    registry.register(new ItemBlock(VanillaAnvils.blockGoldAnvil).setRegistryName(VanillaAnvils.blockGoldAnvil.getRegistryName()));
    registry.register(new ItemBlock(VanillaAnvils.blockGoldAnvilChipped).setRegistryName(VanillaAnvils.blockGoldAnvilChipped.getRegistryName()));
    registry.register(new ItemBlock(VanillaAnvils.blockGoldAnvilDamaged).setRegistryName(VanillaAnvils.blockGoldAnvilDamaged.getRegistryName()));

    registry.register(new ItemBlock(VanillaAnvils.blockStoneAnvil).setRegistryName(VanillaAnvils.blockStoneAnvil.getRegistryName()));
    registry.register(new ItemBlock(VanillaAnvils.blockStoneAnvilChipped).setRegistryName(VanillaAnvils.blockStoneAnvilChipped.getRegistryName()));
    registry.register(new ItemBlock(VanillaAnvils.blockStoneAnvilDamaged).setRegistryName(VanillaAnvils.blockStoneAnvilDamaged.getRegistryName()));*/

  }
}
