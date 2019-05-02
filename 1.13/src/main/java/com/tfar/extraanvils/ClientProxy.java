package com.tfar.extraanvils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

  @Override
  public EntityPlayer getClientPlayer() {
    return Minecraft.getInstance().player;
  }

  @Override
  public World getClientWorld() {
    return Minecraft.getInstance().world;
  }
}
