package com.tfar.extraanvils.jei;

import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.generic.GenericAnvilBlock;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
@SuppressWarnings("unused")
public class ExtraAnvilsJeiPlugin implements IModPlugin {

  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(ExtraAnvils.MODID,"minecraft");
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    for (GenericAnvilBlock anvil : ExtraAnvils.anvils) {
      if (anvil.variant != GenericAnvilBlock.Variant.NORMAL) continue;
      registration.addRecipeCatalyst(new ItemStack(anvil), VanillaRecipeCategoryUid.ANVIL);
    }
  }
}
