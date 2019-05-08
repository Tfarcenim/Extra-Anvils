package com.tfar.extraanvils.network;

import com.tfar.extraanvils.generic.ContainerGenericAnvil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAnvilRename implements IMessage {
  private String name;
  private int length;

  public PacketAnvilRename() {}

  public PacketAnvilRename(String s) {
      name = s;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    name = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf,name);
  }

    public static class Handler implements IMessageHandler<PacketAnvilRename, IMessage> {
      @Override
      public IMessage onMessage(PacketAnvilRename message, MessageContext ctx) {
        // Always use a construct like this to actually handle your message. This ensures that
        // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
        // is called on the networking thread so it is not safe to do a lot of things
        // here.
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
        return null;
      }

      private void handle(PacketAnvilRename message, MessageContext ctx) {
        // This code is run on the server side. So you can do server-side calculations here
        EntityPlayerMP playerEntity = ctx.getServerHandler().player;
        Container container = playerEntity.openContainer;
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
        mainThread.addScheduledTask(() -> {
          if (container instanceof ContainerGenericAnvil){
            ContainerGenericAnvil anvil = (ContainerGenericAnvil) ctx.getServerHandler().player.openContainer;
            anvil.updateItemName(message.name);
          }
        });
      }
    }
  }

