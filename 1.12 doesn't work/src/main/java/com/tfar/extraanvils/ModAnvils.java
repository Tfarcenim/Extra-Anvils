package com.tfar.extraanvils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.tfar.extraanvils.ExtraAnvils.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ModAnvils {

  public static final List<Block> anvils = new ArrayList<>();

  public static Block goldAnvilVeryDamaged;
  public static Block goldAnvilSlightlyDamaged;
  public static Block goldAnvilUndamaged;

  public static final List<Item> anvilitems = new ArrayList<>();

  public static void init() {
    goldAnvilUndamaged = new BlockGoldAnvil("gold_anvil","_undamaged");
    goldAnvilSlightlyDamaged = new BlockGoldAnvil("gold_anvil","_slightly_damaged");
    goldAnvilVeryDamaged = new BlockGoldAnvil("gold_anvil","_very_damaged");
  }
  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> e){

      e.getRegistry().registerAll(goldAnvilVeryDamaged,goldAnvilSlightlyDamaged,goldAnvilUndamaged);

  }

  @SubscribeEvent
  public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
    event.getRegistry().registerAll(new ItemBlock(goldAnvilUndamaged).setRegistryName(goldAnvilUndamaged.getRegistryName()));
    event.getRegistry().registerAll(new ItemBlock(goldAnvilSlightlyDamaged).setRegistryName(goldAnvilSlightlyDamaged.getRegistryName()));
    event.getRegistry().registerAll(new ItemBlock(goldAnvilVeryDamaged).setRegistryName(goldAnvilVeryDamaged.getRegistryName()));

  }

  @SubscribeEvent
  public static void registerRenders(ModelRegistryEvent event) {
    registerRender(Item.getItemFromBlock(goldAnvilUndamaged));
    registerRender(Item.getItemFromBlock(goldAnvilSlightlyDamaged));
    registerRender(Item.getItemFromBlock(goldAnvilVeryDamaged));

  }
  public static void registerRender(Item item) {
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
  }

}
