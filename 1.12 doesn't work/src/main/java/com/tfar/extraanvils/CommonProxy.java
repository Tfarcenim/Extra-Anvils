package com.tfar.extraanvils;


import com.tfar.extraanvils.diamond.BlockDiamondAnvil;
import com.tfar.extraanvils.gold.BlockGoldAnvil;
import com.tfar.extraanvils.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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

    registry.register(new BlockDiamondAnvil("diamond_anvil"));
    registry.register(new BlockDiamondAnvil("diamond_anvil_chipped"));
    registry.register(new BlockDiamondAnvil("diamond_anvil_damaged"));

    registry.register(new BlockGoldAnvil("gold_anvil"));
    registry.register(new BlockGoldAnvil("gold_anvil_chipped"));
    registry.register(new BlockGoldAnvil("gold_anvil_damaged"));
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    registry.register(new ItemBlock(ModAnvils.blockDiamondAnvil).setRegistryName(ModAnvils.blockDiamondAnvil.getRegistryName()));
    registry.register(new ItemBlock(ModAnvils.blockDiamondAnvilChipped).setRegistryName(ModAnvils.blockDiamondAnvilChipped.getRegistryName()));
    registry.register(new ItemBlock(ModAnvils.blockDiamondAnvilDamaged).setRegistryName(ModAnvils.blockDiamondAnvilDamaged.getRegistryName()));

    registry.register(new ItemBlock(ModAnvils.blockGoldAnvil).setRegistryName(ModAnvils.blockGoldAnvil.getRegistryName()));
    registry.register(new ItemBlock(ModAnvils.blockGoldAnvilChipped).setRegistryName(ModAnvils.blockGoldAnvilChipped.getRegistryName()));
    registry.register(new ItemBlock(ModAnvils.blockGoldAnvilDamaged).setRegistryName(ModAnvils.blockGoldAnvilDamaged.getRegistryName()));

  }
}
