package com.tfar.extraanvils;

import com.tfar.extraanvils.diamond.BlockDiamondAnvil;
import com.tfar.extraanvils.gold.BlockGoldAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class ModAnvils {

  @ObjectHolder("extraanvils:gold_anvil")
  public static BlockGoldAnvil blockGoldAnvil;

  @ObjectHolder("extraanvils:gold_anvil_chipped")
  public static BlockGoldAnvil blockGoldAnvilChipped;

  @ObjectHolder("extraanvils:gold_anvil_damaged")
  public static BlockGoldAnvil blockGoldAnvilDamaged;

  @ObjectHolder("extraanvils:diamond_anvil")
  public static BlockDiamondAnvil blockDiamondAnvil;

  @ObjectHolder("extraanvils:diamond_anvil_chipped")
  public static BlockDiamondAnvil blockDiamondAnvilChipped;

  @ObjectHolder("extraanvils:diamond_anvil_damaged")
  public static BlockDiamondAnvil blockDiamondAnvilDamaged;

  public static void registerBlocks(IForgeRegistry<Block> registry) {
    registry.register(new BlockGoldAnvil("gold_anvil",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    registry.register(new BlockGoldAnvil("gold_anvil_chipped",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    registry.register(new BlockGoldAnvil("gold_anvil_damaged",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));

    registry.register(new BlockDiamondAnvil("diamond_anvil",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    registry.register(new BlockDiamondAnvil("diamond_anvil_chipped",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    registry.register(new BlockDiamondAnvil("diamond_anvil_damaged",Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)));
  }

  public static void registerItems(IForgeRegistry<Item> registry) {
    registry.register(new ItemBlock(blockGoldAnvil, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(blockGoldAnvil.getRegistryName()));
    registry.register(new ItemBlock(blockGoldAnvilChipped, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(blockGoldAnvilChipped.getRegistryName()));
    registry.register(new ItemBlock(blockGoldAnvilDamaged, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(blockGoldAnvilDamaged.getRegistryName()));

    registry.register(new ItemBlock(blockDiamondAnvil, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(blockDiamondAnvil.getRegistryName()));
    registry.register(new ItemBlock(blockDiamondAnvilChipped, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(blockDiamondAnvilChipped.getRegistryName()));
    registry.register(new ItemBlock(blockDiamondAnvilDamaged, new Item.Properties().group(ExtraAnvils.creativeTab)).setRegistryName(blockDiamondAnvilDamaged.getRegistryName()));
  }
}
