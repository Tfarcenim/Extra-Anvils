package com.tfar.extraanvils;

import net.minecraft.enchantment.Enchantment;
import shadows.apotheosis.ench.asm.EnchHooks;

public class ApothCompat {
  public static int getActualMaxLevel(Enchantment ench){
    return EnchHooks.getMaxLevel(ench);
  }
}
