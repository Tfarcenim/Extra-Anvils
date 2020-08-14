package com.tfar.extraanvils.compat;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class Compat {
  public static final boolean isAnvilTweaksHere = ModList.get().isLoaded("anviltweaks");
  public static final boolean hasTileEntity = isAnvilTweaksHere;

  public static TileEntity createTileEntity() {
    return isAnvilTweaksHere ? AnvilTweaksCompat.getAnvilTweaksTile() : null;
  }
}
