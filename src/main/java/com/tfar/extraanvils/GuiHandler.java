package com.tfar.extraanvils;

import com.tfar.extraanvils.diamond.GuiDiamondAnvil;
import com.tfar.extraanvils.gold.GuiGoldAnvil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;

  public class GuiHandler {

    public static final ResourceLocation GOLD_ANVIL_ID = new ResourceLocation(ExtraAnvils.MODID, "gold_anvil");
    public static final ResourceLocation DIAMOND_ANVIL_ID = new ResourceLocation(ExtraAnvils.MODID, "diamond_anvil");

    @Nullable
    public static GuiScreen getClientGuiElement(FMLPlayMessages.OpenContainer container) {

      World world = ExtraAnvils.proxy.getClientWorld();
      EntityPlayer player = ExtraAnvils.proxy.getClientPlayer();
      switch (container.getId().toString()) {
        case (ExtraAnvils.MODID+":gold_anvil"):
          return new GuiGoldAnvil(player.inventory,world);
        case (ExtraAnvils.MODID+":diamond_anvil"):
          return new GuiDiamondAnvil(player.inventory,world);
          default: return null;
      }
    }
  }




