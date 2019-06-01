package com.tfar.extraanvils.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

  public static SimpleNetworkWrapper INSTANCE = null;

  public PacketHandler() {
  }

  public static void registerMessages(String channelName) {
    INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
    registerMessages();
  }
  public static void registerMessages() {
    // Register messages which are sent from the client to the server here:
    INSTANCE.registerMessage(PacketAnvilRename.Handler.class, PacketAnvilRename.class, 0, Side.SERVER);
  }
}
