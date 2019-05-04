package com.tfar.extraanvils;

import com.tfar.extraanvils.diamond.BlockDiamondAnvil;
import com.tfar.extraanvils.gold.BlockGoldAnvil;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModAnvils {

  @GameRegistry.ObjectHolder("extraanvils:gold_anvil")
  public static BlockGoldAnvil blockGoldAnvil;

  @GameRegistry.ObjectHolder("extraanvils:gold_anvil_chipped")
  public static BlockGoldAnvil blockGoldAnvilChipped;

  @GameRegistry.ObjectHolder("extraanvils:gold_anvil_damaged")
  public static BlockGoldAnvil blockGoldAnvilDamaged;

@GameRegistry.ObjectHolder(ExtraAnvils.MODID+":diamond_anvil")
  public static BlockDiamondAnvil blockDiamondAnvil;

  @GameRegistry.ObjectHolder(ExtraAnvils.MODID+":diamond_anvil_chipped")
  public static BlockDiamondAnvil blockDiamondAnvilChipped;

  @GameRegistry.ObjectHolder(ExtraAnvils.MODID+":diamond_anvil_damaged")
  public static BlockDiamondAnvil blockDiamondAnvilDamaged;

  @SideOnly(Side.CLIENT)
  public static void initModels() {
    blockDiamondAnvil.registerModel();
    blockDiamondAnvilChipped.registerModel();
    blockDiamondAnvilDamaged.registerModel();

    blockGoldAnvil.registerModel();
    blockGoldAnvilChipped.registerModel();
    blockGoldAnvilDamaged.registerModel();
  }
}
