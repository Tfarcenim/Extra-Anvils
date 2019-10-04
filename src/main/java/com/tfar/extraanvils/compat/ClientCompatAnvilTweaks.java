package com.tfar.extraanvils.compat;

import com.tfar.anviltweaks.AnvilTileSpecialRenderer;
import com.tfar.extraanvils.Client;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientCompatAnvilTweaks extends Client {
  public static void bindTESR(){
    ClientRegistry.bindTileEntitySpecialRenderer(GenericAnvilAnvilTweaksCompatTile.class, new AnvilTileSpecialRenderer());
  }
}
