package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.EntityAetherAnvil;
import com.tfar.extraanvils.EntityFallingAnvil;
import com.tfar.extraanvils.EnumVariants;
import com.tfar.extraanvils.ExtraAnvils;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockAetherAnvil extends BlockGenericAnvil {

  public BlockAetherAnvil(String material,AnvilProperties properties, EnumVariants variant) {
    super(material,properties,variant);
  }

  @Override
  public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
    worldIn.playEvent(1031, pos, 0);
  }

@Override
  protected void onStartFalling(EntityFallingBlock fallingEntity)
  {
    onStartFall((EntityAetherAnvil)fallingEntity);
  }

  private void onStartFall(EntityAetherAnvil anvil){
    anvil.setHurtEntities(true);
  }

  private void checkFallable(World worldIn, BlockPos pos)
  {
    if ((worldIn.isAirBlock(pos.up()) || canFallThrough(worldIn.getBlockState(pos.up()))) && pos.getY() <= 255)
    {
      int i = 32;

      if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i)))
      {
        if (!worldIn.isRemote)
        {
          EntityAetherAnvil entityfallinganvil = new EntityAetherAnvil(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
          this.onStartFalling(entityfallinganvil);
          worldIn.spawnEntity(entityfallinganvil);
        }
      }
      else
      {
        IBlockState state = worldIn.getBlockState(pos);
        worldIn.setBlockToAir(pos);
        BlockPos blockpos;

        for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down())
        {
          ;
        }

        if (blockpos.getY() > 0)
        {
          worldIn.setBlockState(blockpos.up(), state); //Forge: Fix loss of state information during world gen.
        }
      }
    }
  }

  @Override
  public void updateTick(World world, @Nonnull BlockPos pos, IBlockState state, Random rand)
  {
    if (!world.isRemote)
    {
      if (this.hasTrait("redstone") && world.isBlockPowered(pos))return;
      this.checkFallable(world, pos);
    }
  }
}