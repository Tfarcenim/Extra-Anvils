package com.tfar.extraanvils.generic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class GenericAnvilContainerProvider implements INamedContainerProvider {

  public BlockPos pos;
  public GenericAnvilContainerProvider(BlockPos pos){
    this.pos = pos;
  }

  @Nonnull
  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("container.repair");
  }

  @Override
  public Container createMenu(int p_createMenu_1_, @Nonnull PlayerInventory p_createMenu_2_, @Nonnull PlayerEntity p_createMenu_3_) {
    return new GenericAnvilContainer(p_createMenu_1_, p_createMenu_2_, pos);
  }
}
