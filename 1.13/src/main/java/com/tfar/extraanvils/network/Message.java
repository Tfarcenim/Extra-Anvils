package com.tfar.extraanvils.network;


import com.tfar.extraanvils.ExtraAnvils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * @author coolAlias
 * @author The_Fireplace
 */
public class Message {

  public static SimpleChannel INSTANCE;

  public static void registerMessages(String channelName) {
    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ExtraAnvils.MODID, channelName), () -> "1.0", s -> true, s -> true);
    INSTANCE.registerMessage(0, PacketAnvilRename.class,
            PacketAnvilRename::encode,
            PacketAnvilRename::new,
            PacketAnvilRename::handle);
  }
}