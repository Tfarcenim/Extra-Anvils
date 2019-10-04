package com.tfar.extraanvils.compat;

import com.tfar.anviltweaks.AnvilTile;
import com.tfar.anviltweaks.AnvilTweaks;
import com.tfar.extraanvils.generic.GenericAnvilContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;

public class GenericAnvilAnvilTweaksCompatTile extends AnvilTile {
  public GenericAnvilAnvilTweaksCompatTile() {
    super(AnvilTweaks.Stuff.anvil_tile);
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
    return new GenericAnvilContainer(i, playerInventory,this.pos);
  }
}
