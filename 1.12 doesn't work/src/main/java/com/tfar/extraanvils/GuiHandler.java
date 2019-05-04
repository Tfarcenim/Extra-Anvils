package com.tfar.extraanvils;

import com.tfar.extraanvils.diamond.BlockDiamondAnvil;
import com.tfar.extraanvils.diamond.ContainerDiamondAnvil;
import com.tfar.extraanvils.diamond.GuiDiamondAnvil;
import com.tfar.extraanvils.gold.BlockGoldAnvil;
import com.tfar.extraanvils.gold.ContainerGoldAnvil;
import com.tfar.extraanvils.gold.GuiGoldAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    switch (ID) {
      case 0:
        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockGoldAnvil)
        return new ContainerGoldAnvil(player.inventory, world, new BlockPos(x, y, z), player);
      case 1:
        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockDiamondAnvil)
          return new ContainerDiamondAnvil(player.inventory, world, new BlockPos(x, y, z), player);
      default:
        return null;
    }
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    switch (ID) {
      case 0:
        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockGoldAnvil)
          return new GuiGoldAnvil(player.inventory, world);
      case 1:
        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockDiamondAnvil)
          return new GuiDiamondAnvil(player.inventory, world);
      default:
        return null;
    }
  }
}




