package com.tfar.extraanvils;

import com.tfar.extraanvils.generic.BlockGenericAnvil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

@JEIPlugin
@SuppressWarnings("unused")
public class ExtraAnvilsJeiPlugin implements IModPlugin {
  @Override
  public void register(IModRegistry registry) {
    for (BlockGenericAnvil anvil : ExtraAnvils.anvils) {
      if (anvil.variant != EnumVariants.NORMAL)continue;
      registry.addRecipeCatalyst(new ItemStack(anvil), VanillaRecipeCategoryUid.ANVIL);
    }
  }
}
