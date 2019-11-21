package com.tfar.extraanvils.compat;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class Compat {
  public static final boolean isApothesisHere = ModList.get().isLoaded("apotheosis");
  public static final boolean isAnvilTweaksHere = ModList.get().isLoaded("anviltweaks");
  public static final boolean hasTileEntity = isApothesisHere || isAnvilTweaksHere;

  public static int getMaxLevel(Enchantment enchantment){
    return isApothesisHere ? ApothCompat.getActualMaxLevel(enchantment) : enchantment.getMaxLevel();
  }

  public static TileEntity createTileEntity(){
    return isAnvilTweaksHere ? AnvilTweaksCompat.getAnvilTweaksTile() : Compat.isApothesisHere ? ApothCompat.getApothTile(): null;
  }
}
