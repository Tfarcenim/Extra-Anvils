package com.tfar.extraanvils.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.world.World;
//todo reimplement special behavior
public class FallingAnvilEntity extends FallingBlockEntity {

  public FallingAnvilEntity(EntityType<? extends FallingBlockEntity> p_i50218_1_, World p_i50218_2_) {
    super(p_i50218_1_, p_i50218_2_);
  }

  public FallingAnvilEntity(World worldIn, double v, double y, double v1, BlockState blockState) {
    super(worldIn, v, y, v1, blockState);
  }
}