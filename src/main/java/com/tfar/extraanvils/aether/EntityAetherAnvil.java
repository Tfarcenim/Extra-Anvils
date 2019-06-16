package com.tfar.extraanvils.aether;

import com.google.common.collect.Lists;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import com.tfar.extraanvils.generic.EntityFallingAnvil;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.util.List;


public class EntityAetherAnvil extends EntityFallingAnvil {
  public EntityAetherAnvil(World worldIn) {
    super(worldIn);
  }

  private IBlockState fallTile;
  private boolean dontSetBlock;
  private boolean hurtEntities;
  private int fallHurtMax = Integer.MAX_VALUE;
  private double fallHurtAmount; // Damage multiplier

  public EntityAetherAnvil(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
    super(worldIn, x, y, z, fallingBlockState);
    // this.setPosition(x, y + 0/*(1 - this.height) / 2*/, z);
    this.fallTile = fallingBlockState;
    this.fallHurtAmount = ((BlockGenericAnvil) fallingBlockState.getBlock()).properties.weight * 2;
  }


  private static final String anvilDamageName = "anvildamage";

  /**
   * what happens when the block is done falling; this doesn't fire for some reason and I don't know why
   */
  @Override
  public void fall(float distance, float damageMultiplier) {
    BlockAetherAnvil block = (BlockAetherAnvil) this.fallTile.getBlock();

    if (this.hurtEntities) {
      int i = MathHelper.ceil(distance - 1);

      if (i > 0) {
        List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
        boolean flag = block.properties.playerDamage && i > 20;
        boolean shouldDamage = true;
        FakePlayer fakePlayer = (flag) ? FakePlayerFactory.getMinecraft((WorldServer) world) : null;

        float amount = Math.min(MathHelper.floor(i * this.fallHurtAmount), this.fallHurtMax);
        for (Entity entity : list) {
          if (entity instanceof EntityLivingBase) shouldDamage = false;
          if (flag)
            entity.attackEntityFrom(new EntityDamageSource(anvilDamageName, fakePlayer), amount);
          else
            entity.attackEntityFrom(DamageSource.ANVIL, amount);
        }

        //check to damage anvil
        if (shouldDamage && rand.nextFloat() < .05 * (i + 1)) {
          IBlockState iblockstate = BlockGenericAnvil.damage(this.fallTile);
          if (iblockstate == null) this.dontSetBlock = true;
          else this.fallTile = iblockstate;
        }
      }
    }
  }

  /**
   * Called to update the entity's position/logic.
   */
  @Override
  public void onUpdate() {

    Block block = this.fallTile.getBlock();

    if (this.fallTile.getMaterial() == Material.AIR) {
      this.setDead();
    } else {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;

      if (this.fallTime++ == 0) {
        BlockPos blockpos = new BlockPos(this);

        if (this.world.getBlockState(blockpos).getBlock() == block) {
          this.world.setBlockToAir(blockpos);
        } else if (!this.world.isRemote) {
          this.setDead();
          return;
        }
      }

      if (!this.hasNoGravity()) {
        this.motionY += 0.04;
      }

      this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

      if (!this.world.isRemote) {
        BlockPos blockpos1 = new BlockPos(this);
        if (!this.collidedVertically) {
          //drop as item if too old or out of bounds
          if ((this.fallTime > 100 && blockpos1.getY() > 256) || this.fallTime > 100) {
            if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
              this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0);
            }

            this.setDead();
          }
        } else {
          IBlockState iblockstate = this.world.getBlockState(blockpos1);

          if (this.world.isAirBlock(new BlockPos(this.posX, this.posY + 1.01, this.posZ))) //Forge: Don't indent below.
            if (BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY + 1.01, this.posZ)))) {
              return;
            }

          this.motionX *= 0.7;
          this.motionZ *= 0.7;
          this.motionY *= +0.5;

          if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION) {
            this.setDead();

            if (!this.dontSetBlock) {
              if (this.world.mayPlace(block, blockpos1, true, EnumFacing.DOWN, null) && !BlockFalling.canFallThrough(this.world.getBlockState(blockpos1.up())) && this.world.setBlockState(blockpos1, this.fallTile, 3)) {
                if (block instanceof BlockFalling) {
                  ((BlockFalling) block).onEndFalling(this.world, blockpos1, this.fallTile, iblockstate);
                }

                if (this.tileEntityData != null && block.hasTileEntity(this.fallTile)) {
                  TileEntity tileentity = this.world.getTileEntity(blockpos1);

                  if (tileentity != null) {
                    NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                    for (String s : this.tileEntityData.getKeySet()) {
                      NBTBase nbtbase = this.tileEntityData.getTag(s);

                      if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                        nbttagcompound.setTag(s, nbtbase.copy());
                      }
                    }

                    tileentity.readFromNBT(nbttagcompound);
                    tileentity.markDirty();
                  }
                }
              } else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0);
              }
            } else if (block instanceof BlockFalling) {
              ((BlockFalling) block).onBroken(this.world, blockpos1);
            }
          }
        }
      }

      this.motionX *= 0.98;
      this.motionY *= 0.98;
      this.motionZ *= 0.98;
    }
  }


  /**
   * (abstract) Protected helper method to write subclass entity data to NBT.
   */
  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
    ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(block);
    compound.setString("Block", resourcelocation.toString());
    compound.setByte("Data", (byte) block.getMetaFromState(this.fallTile));
    compound.setInteger("Time", this.fallTime);
    compound.setBoolean("DropItem", this.shouldDropItem);
    compound.setBoolean("HurtEntities", this.hurtEntities);
    compound.setFloat("FallHurtAmount", (float) this.fallHurtAmount);
    compound.setInteger("FallHurtMax", this.fallHurtMax);

    if (this.tileEntityData != null) {
      compound.setTag("TileEntityData", this.tileEntityData);
    }
  }

  /**
   * (abstract) Protected helper method to read subclass entity data from NBT.
   */
  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    int i = compound.getByte("Data") & 255;

    if (compound.hasKey("Block", 8)) {
      this.fallTile = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
    } else if (compound.hasKey("TileID", 99)) {
      this.fallTile = Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i);
    } else {
      this.fallTile = Block.getBlockById(compound.getByte("Tile") & 255).getStateFromMeta(i);
    }

    this.fallTime = compound.getInteger("Time");
    Block block = this.fallTile.getBlock();

    if (compound.hasKey("HurtEntities", 99)) {
      this.hurtEntities = compound.getBoolean("HurtEntities");
      this.fallHurtAmount = compound.getFloat("FallHurtAmount");
      this.fallHurtMax = compound.getInteger("FallHurtMax");
    } else if (block == Blocks.ANVIL) {
      this.hurtEntities = true;
    }

    if (compound.hasKey("DropItem", 99)) {
      this.shouldDropItem = compound.getBoolean("DropItem");
    }

    if (compound.hasKey("TileEntityData", 10)) {
      this.tileEntityData = compound.getCompoundTag("TileEntityData");
    }

    if (block.getDefaultState().getMaterial() == Material.AIR) {
      this.fallTile = Blocks.SAND.getDefaultState();
    }
  }

  @Override
  @Nullable
  public IBlockState getBlock() {
    return fallTile;
  }

  @Override
  public void setHurtEntities(boolean hurtEntities) {
    this.hurtEntities = hurtEntities;
  }

  /**
   * Called by the server when constructing the spawn packet.
   * Data should be added to the provided stream.
   *
   * @param buffer The packet data stream
   */
  @Override
  public void writeSpawnData(ByteBuf buffer) {
    buffer.writeLong(this.getPosition().toLong());
  }

  /**
   * Called by the client when it receives a Entity spawn packet.
   * Data should be read out of the stream in the same way as it was written.
   *
   * @param additionalData The packet data stream
   */
  @Override
  public void readSpawnData(ByteBuf additionalData) {
    long blockPosLong = additionalData.readLong();
    this.fallTile = world.getBlockState(BlockPos.fromLong(blockPosLong));
  }
}
