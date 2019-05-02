package com.tfar.extraanvils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.tfar.extraanvils.ExtraAnvils.MODID;

public class ClientProxy extends CommonProxy {

  @Override
  public void registerItemRenders() {
  //  for(String key:anvils.keySet())
  //    registerAnvilRenderer(anvils.get(key));
  }
  public void registerAnvilRenderer(Block anvil) {
   // ModelBakery.registerItemVariants(Item.getItemFromBlock(anvil),
   //         new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_intact", "inventory"),
     //       new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_slightly_damaged", "inventory"),
  //          new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_very_damaged", "inventory"));
  }
  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event)
  {
    for (Block anvil: ModAnvils.anvils)
    {
    //  ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));

      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(anvil), 0, new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_intact", "inventory"));
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(anvil), 1, new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_slightly_damaged", "inventory"));
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(anvil), 2, new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_very_damaged", "inventory"));
    }
    for (Item anvil: ModAnvils.anvilitems)
    {
      //  ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));

      ModelLoader.setCustomModelResourceLocation(anvil, 0, new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_intact", "inventory"));
      ModelLoader.setCustomModelResourceLocation(anvil, 1, new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_slightly_damaged", "inventory"));
      ModelLoader.setCustomModelResourceLocation(anvil, 2, new ModelResourceLocation(MODID + ":" + anvil.getTranslationKey().substring(5) + "_very_damaged", "inventory"));
    }
    }
}
