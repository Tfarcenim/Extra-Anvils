package com.tfar.extraanvils.compat;

import com.tfar.extraanvils.Client;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import tfar.anviltweaks.AnvilTileRenderer;
import tfar.anviltweaks.AnvilTweaks;

public class ClientCompatAnvilTweaks extends Client {
  public static void bindTESR(){
    ClientRegistry.bindTileEntityRenderer(AnvilTweaks.Stuff.anvil_tile, AnvilTileRenderer::new);
  }
}
