package com.tfar.extraanvils;

import com.google.common.collect.Lists;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.List;



public class EntityFallingAnvil extends EntityFallingBlock {
  public EntityFallingAnvil(World worldIn) {
    super(worldIn);
  }

  private IBlockState fallTile;
  public int fallTime;
  public boolean shouldDropItem = true;
  private boolean dontSetBlock;
  private boolean causesPlayerDamage;
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

  private static final String anvilDamageName = "anvildamage";
  public static final DamageSource ExtraAnvil = new DamageSource(anvilDamageName);

  /** what happens when the block is done falling*/
  @Override
  public void fall(float distance, float damageMultiplier) {
    BlockGenericAnvil block = (BlockGenericAnvil) this.fallTile.getBlock();

    if (this.hurtEntities) {
      int i = MathHelper.ceil(distance - 1);

      if (i > 0) {
        List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
        boolean flag = block.properties.causesPlayerDamage;
        FakePlayer fakePlayer = (flag) ? FakePlayerFactory.getMinecraft((WorldServer) world) : null;

        float amount = Math.min(MathHelper.floor(i * this.fallHurtAmount), this.fallHurtMax);
        for (Entity entity : list) {
          if (flag)
            entity.attackEntityFrom(new EntityDamageSource(anvilDamageName, fakePlayer), amount);
          else
            entity.attackEntityFrom(DamageSource.ANVIL, amount);
        }


        //check to damage anvil
        if (rand.nextFloat() * fallResistance < .05 * (i + 1) || true) {
            IBlockState iblockstate = BlockGenericAnvil.damage(this.fallTile);
            if (iblockstate == null) this.dontSetBlock = true;
            else {this.fallTile = iblockstate;}
          }
        }
      }
    }

  /**
   * Called to update the entity's position/logic.
   */
  @Override
  public void onUpdate()
  {
    Block block = this.fallTile.getBlock();

    if (this.fallTile.getMaterial() == Material.AIR)
    {
      this.setDead();
    }
    else
    {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;

      if (this.fallTime++ == 0)
      {
        BlockPos blockpos = new BlockPos(this);

        if (this.world.getBlockState(blockpos).getBlock() == block)
        {
          this.world.setBlockToAir(blockpos);
        }
        else if (!this.world.isRemote)
        {
          this.setDead();
          return;
        }
      }

      if (!this.hasNoGravity())
      {
        this.motionY -= 0.04 * this.fallHurtAmount/2;
      }

      this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

      if (!this.world.isRemote)
      {
        BlockPos blockpos1 = new BlockPos(this);

        if (!this.onGround)
        {
          if (this.fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)
          {
            if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
            {
              this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
            }

            this.setDead();
          }
        }
        else
        {
          IBlockState iblockstate = this.world.getBlockState(blockpos1);

          if (this.world.isAirBlock(new BlockPos(this.posX, this.posY - 0.01, this.posZ))) //Forge: Don't indent below.
            if (BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))))
            {
              this.onGround = false;
              return;
            }

          this.motionX *= 0.7;
          this.motionZ *= 0.7;
          this.motionY *= -0.5;

          if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION)
          {
            this.setDead();

            if (!this.dontSetBlock)
            {
              if (this.world.mayPlace(block, blockpos1, true, EnumFacing.UP, null) && !BlockFalling.canFallThrough(this.world.getBlockState(blockpos1.down())) && this.world.setBlockState(blockpos1, this.fallTile, 3))
              {
                if (block instanceof BlockFalling)
                {
                  ((BlockFalling)block).onEndFalling(this.world, blockpos1, this.fallTile, iblockstate);
                }

                if (this.tileEntityData != null && block.hasTileEntity(this.fallTile))
                {
                  TileEntity tileentity = this.world.getTileEntity(blockpos1);

                  if (tileentity != null)
                  {
                    NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                    for (String s : this.tileEntityData.getKeySet())
                    {
                      NBTBase nbtbase = this.tileEntityData.getTag(s);

                      if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
                      {
                        nbttagcompound.setTag(s, nbtbase.copy());
                      }
                    }

                    tileentity.readFromNBT(nbttagcompound);
                    tileentity.markDirty();
                  }
                }
              }
              else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
              {
                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0);
              }
            }
            else if (block instanceof BlockFalling)
            {
              ((BlockFalling)block).onBroken(this.world, blockpos1);
            }
          }
        }
      }

      this.motionX *= 0.98;
      this.motionY *= 0.98;
      this.motionZ *= 0.98;
    }
  }


  @Override
  public void setHurtEntities(boolean hurtEntities) {
    this.hurtEntities = hurtEntities;
  }
}
