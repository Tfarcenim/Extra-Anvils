package com.tfar.extraanvils;

import com.tfar.anviltweaks.AnvilTweaks;
import com.tfar.extraanvils.compat.AnvilTweaksCompat;
import com.tfar.extraanvils.compat.Compat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AnvilTweaks.MODID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class Client {
  @SubscribeEvent
  public static void doClientStuff(final FMLClientSetupEvent event) {
    if (Compat.isAnvilTweaksHere) AnvilTweaksCompat.bindTESR();
  }
}