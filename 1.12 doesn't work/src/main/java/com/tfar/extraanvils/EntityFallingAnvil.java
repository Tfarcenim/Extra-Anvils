package com.tfar.extraanvils;

import com.google.common.collect.Lists;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;



public class EntityFallingAnvil extends EntityFallingBlock {
  public EntityFallingAnvil(World worldIn) {
    super(worldIn);
  }

  private IBlockState fallTile;
  public int fallTime;
  public boolean shouldDropItem = true;
  private boolean dontSetBlock;
  private boolean hurtEntities;
  private int fallHurtMax = Integer.MAX_VALUE;
  private double fallResistance;
  private double fallHurtAmount; // Damage multiplier
  public NBTTagCompound tileEntityData;

  public EntityFallingAnvil(World worldIn, double x, double y, double z, IBlockState fallingBlockState)
  {
    super(worldIn,x,y,z,fallingBlockState);
    this.fallTile = fallingBlockState;
    this.fallHurtAmount= ((BlockGenericAnvil)fallingBlockState.getBlock()).properties.weight * 2;
    this.fallResistance = ((BlockGenericAnvil)fallingBlockState.getBlock()).properties.fallResistance;
  }

  /** what happens when the block is done falling*/
  @Override
  public void fall(float distance, float damageMultiplier) {
    Block block = this.fallTile.getBlock();

    if (this.hurtEntities) {
      int i = MathHelper.ceil(distance - 1);

      if (i > 0) {
        List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
        boolean flag = block instanceof BlockGenericAnvil;
        DamageSource damagesource = DamageSource.ANVIL;

        for (Entity entity : list)
        {
          entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax));
        }


        //check to damage anvil
        if (flag && rand.nextFloat()*fallResistance < .05*(i+1)) {
          if (((BlockGenericAnvil)fallTile.getBlock()).variant != EnumVariants.DAMAGED)
          ((BlockGenericAnvil)fallTile.getBlock()).damage(fallTile,this.world,this.getPosition());
          else
          {
            this.dontSetBlock = true;
          }
        }
      }
    }
  }

  @Override
  public void setHurtEntities(boolean hurtEntities) {
    this.hurtEntities = hurtEntities;
  }
}
