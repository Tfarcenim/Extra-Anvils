package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.compat.Compat;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class GenericAnvilBlockItem extends BlockItem {

  public GenericAnvilBlockItem(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return Compat.isApothesisHere && stack.getCount() == 1 && (enchantment == Enchantments.UNBREAKING || enchantment == ExtraAnvils.splitting);
  }

  public boolean isEnchantable(ItemStack stack) {
    return Compat.isApothesisHere && stack.getCount() == 1;
  }

  public int getItemEnchantability(ItemStack stack) {
    return Compat.isApothesisHere ? 50: 0;
  }

}
