package com.tfar.extraanvils.network;

import com.tfar.extraanvils.diamond.ContainerDiamondAnvil;
import com.tfar.extraanvils.gold.ContainerGoldAnvil;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAnvilRename {

  private String name;
  private int length;

  public PacketAnvilRename() {}

  public PacketAnvilRename(String newName) {
    this.name = newName;
  }

 public PacketAnvilRename(PacketBuffer buf) {
    length = buf.readInt();
   name = buf.readString(length);
  }

  public void encode(PacketBuffer buf) {
    buf.writeInt(name.length());
    buf.writeString(name);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      if (ctx.get() == null || ctx.get().getSender() == null)return;
      Container anvil = ctx.get().getSender().openContainer;
      if (anvil instanceof ContainerGoldAnvil){
        ((ContainerGoldAnvil)anvil).updateItemName(name);}
    else if (anvil instanceof ContainerDiamondAnvil){
        ((ContainerDiamondAnvil)anvil).updateItemName(name);
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
