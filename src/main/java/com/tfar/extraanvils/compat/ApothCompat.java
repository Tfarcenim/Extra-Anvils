package com.tfar.extraanvils.compat;

import com.tfar.extraanvils.generic.GenericAnvilBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.tileentity.TileEntity;
import shadows.apotheosis.ench.asm.EnchHooks;

public class ApothCompat extends Compat {
  public static int getActualMaxLevel(Enchantment ench){
    return EnchHooks.getMaxLevel(ench);
  }
  public static TileEntity getApothTile(GenericAnvilBlock block){
    return new GenericAnvilAnvilTweaksCompatTile();
  }
}
