package com.tfar.extraanvils;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.ModList;

public class Compat {
  public static final boolean isApothesisHere = ModList.get().isLoaded("apotheosis");
  public static int getMaxLevel(Enchantment enchantment){
    return isApothesisHere ? ApothCompat.getActualMaxLevel(enchantment) : enchantment.getMaxLevel();
  }
}
