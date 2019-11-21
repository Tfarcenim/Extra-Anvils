package com.tfar.extraanvils.compat;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.tileentity.TileEntity;

public class ApothCompat extends Compat {
  public static int getActualMaxLevel(Enchantment ench){
    return shadows.apotheosis.ench.asm.EnchHooks.getMaxLevel(ench);
  }
  public static TileEntity getApothTile(){
    return new GenericAnvilAnvilTweaksCompatTile();
  }
}
