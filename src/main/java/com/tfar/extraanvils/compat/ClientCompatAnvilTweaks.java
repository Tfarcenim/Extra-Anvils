package com.tfar.extraanvils.compat;

import tfar.anviltweaks.AnvilTileRenderer;
import tfar.anviltweaks.AnvilTweaks;
import com.tfar.extraanvils.Client;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientCompatAnvilTweaks extends Client {
  public static void bindTESR(){
    ClientRegistry.bindTileEntityRenderer(AnvilTweaks.Stuff.anvil_tile, AnvilTileRenderer::new);
  }
}
