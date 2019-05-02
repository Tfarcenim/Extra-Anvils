package com.tfar.extraanvils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class TabExtraAnvils extends CreativeTabs {
  public TabExtraAnvils(String label) {
    super(label);
  }

  @Override
  @Nonnull
  public ItemStack createIcon() {
    return new ItemStack(ModAnvils.anvils.get(0));  }

}
