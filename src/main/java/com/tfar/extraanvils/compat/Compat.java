package com.tfar.extraanvils.compat;

import com.tfar.extraanvils.generic.GenericAnvilBlock;
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

  public static TileEntity createTileEntity(GenericAnvilBlock block){
    return isAnvilTweaksHere ? AnvilTweaksCompat.getAnvilTweaksTile(block) : Compat.isApothesisHere ? ApothCompat.getApothTile(block): null;
  }
}
