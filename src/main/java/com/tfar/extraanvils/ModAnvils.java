package com.tfar.extraanvils;

import com.tfar.extraanvils.gold.GenericAnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModAnvils {

  @ObjectHolder("extraanvils:gold_anvil")
  public static GenericAnvilBlock genericAnvilBlock;

  @ObjectHolder("extraanvils:gold_anvil_chipped")
  public static GenericAnvilBlock genericAnvilBlockChipped;

  @ObjectHolder("extraanvils:gold_anvil_damaged")
  public static GenericAnvilBlock genericAnvilBlockDamaged;


  public static void registerBlocks(IForgeRegistry<Block> registry) {
    registry.register(new GenericAnvilBlock("gold_anvil",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    registry.register(new GenericAnvilBlock("gold_anvil_chipped",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    registry.register(new GenericAnvilBlock("gold_anvil_damaged",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));

  }

  public static void registerItems(IForgeRegistry<Item> registry) {
    registry.register(new BlockItem(genericAnvilBlock, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(genericAnvilBlock.getRegistryName()));
    registry.register(new BlockItem(genericAnvilBlockChipped, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(genericAnvilBlockChipped.getRegistryName()));
    registry.register(new BlockItem(genericAnvilBlockDamaged, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(genericAnvilBlockDamaged.getRegistryName()));

  }
}
