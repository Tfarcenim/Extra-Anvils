package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.compat.Compat;
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

  private static final TranslationTextComponent name = new TranslationTextComponent("container.repair");

  @Nonnull
  @Override
  public ITextComponent getDisplayName() {
    return name;
  }

  @Override
  public Container createMenu(int p_createMenu_1_, @Nonnull PlayerInventory p_createMenu_2_, @Nonnull PlayerEntity p_createMenu_3_) {
    return Compat.isAnvilTweaksHere ? new GenericAnvilAnvilTweaksContainer(p_createMenu_1_, p_createMenu_2_, pos)
            : new GenericAnvilContainer(p_createMenu_1_, p_createMenu_2_, pos);
  }
}
