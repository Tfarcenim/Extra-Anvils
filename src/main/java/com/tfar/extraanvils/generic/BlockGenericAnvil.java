package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.text.*;
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
import java.util.*;

public class BlockGenericAnvil extends BlockFalling {

  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  protected static final AxisAlignedBB X_AXIS_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D);
  protected static final AxisAlignedBB Z_AXIS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D);
  public AnvilProperties properties;
  public EnumVariants variant;

  public BlockGenericAnvil(AnvilProperties properties, EnumVariants variant) {

    super(Material.ANVIL);
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    setLightOpacity(0);
    setCreativeTab(ExtraAnvils.creativeTab);
    setHardness(5.0F);
    setSoundType(SoundType.ANVIL);
    setResistance(2000.0F);
    this.properties = properties;
    this.variant = variant;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @Nonnull
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    EnumFacing enumfacing = state.getValue(FACING);
    return enumfacing.getAxis() == EnumFacing.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
  }

  @Override
  public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
    worldIn.playEvent(1031, pos, 0);
  }

  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  /**
   * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
   * IBlockstate
   */
  @Override
  @Nonnull
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();

    try {
      return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing);
    } catch (IllegalArgumentException var11) {
      if (!worldIn.isRemote) {
        var11.printStackTrace();
      }

      return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(FACING, enumfacing);
    }
  }

  /**
   * Called when the block is right clicked by a player.
   */
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (!worldIn.isRemote) {
      if (!"infinity".equals(this.properties.name))
      playerIn.openGui(ExtraAnvils.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
      else playerIn.openGui(ExtraAnvils.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());

    }
    return true;
  }

  @Nullable
  public static IBlockState damage(IBlockState state) {
    BlockGenericAnvil block = (BlockGenericAnvil) state.getBlock();
    EnumFacing enumfacing = state.getValue(FACING);
    BlockGenericAnvil damagedBlock = ExtraAnvils.anvilDamageMap.get(block);
    return damagedBlock == null ? null : damagedBlock.getDefaultState().withProperty(FACING, enumfacing);
  }

  @Override
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
    items.add(new ItemStack(this));
  }

  /**
   * Convert the given metadata into a BlockState for this Block
   */
  @Override
  @Nonnull
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3));
  }

  /**
   * Convert the BlockState into the correct metadata value
   */
  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
  }

  @Override
  protected void onStartFalling(EntityFallingBlock fallingEntity) {
    onStartFall((EntityFallingAnvil) fallingEntity);
  }

  private void onStartFall(EntityFallingAnvil anvil) {
    anvil.setHurtEntities(true);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }


  @Override
  @Nonnull
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  private void checkFallable(World world, BlockPos pos) {
    if ((world.isAirBlock(pos.down()) || canFallThrough(world.getBlockState(pos.down()))) && pos.getY() >= 0 || this.hasTrait("dense") && world.getBlockState(pos.down()).getBlockHardness(world,pos.down()) <100 && world.getBlockState(pos.down()).getBlockHardness(world, pos.down()) >= 0) {
      int i = 32;

      if (!fallInstantly && world.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
        if (!world.isRemote) {
          EntityFallingAnvil entityfallinganvil = new EntityFallingAnvil(world, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, world.getBlockState(pos));
          this.onStartFalling(entityfallinganvil);
          world.spawnEntity(entityfallinganvil);
        }
      } else {
        IBlockState state = world.getBlockState(pos);
        world.setBlockToAir(pos);
        BlockPos blockpos;

        for (blockpos = pos.down(); (world.isAirBlock(blockpos) || canFallThrough(world.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down()) {
          ;
        }

        if (blockpos.getY() > 0) {
          world.setBlockState(blockpos.up(), state); //Forge: Fix loss of state information during world gen.
        }
      }
    }
  }

  @Override
  public void updateTick(World world, @Nonnull BlockPos pos, IBlockState state, Random rand) {
    if (!world.isRemote) {
      if (this.hasTrait("redstone") && world.isBlockPowered(pos))
        return;
      this.checkFallable(world, pos);
    }
  }

  public boolean hasTrait(String s) {
    return this.properties.traits != null && Arrays.asList(properties.traits).contains(s);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
      tooltip.add("Level Cap: " + this.properties.cap);
      tooltip.add("Durability Multiplier: " + (this.properties.durability >= 0 ? this.properties.durability : "Infinite"));
      tooltip.add("Enchantability: " + this.properties.enchantability);
      if (this.properties.playerDamage) tooltip.add("Causes Player Damage");
    }
    if (this.properties.traits != null) {
      for (String trait : properties.traits) {
        tooltip.add(TextColors.colors.get(trait) + trait.substring(0,1).toUpperCase()+trait.substring(1));
      }
    }
    if ("infinity".equals(this.properties.name))tooltip.add(stringToRainbow(this.properties.name));
  }

  public static String stringToRainbow(String parString)
  {
    int stringLength = parString.length();
    if (stringLength < 1)
    {
      return "";
    }
    StringBuilder outputString = new StringBuilder();
    TextFormatting[] colorChar =
            {
                    TextFormatting.RED,
                    TextFormatting.GOLD,
                    TextFormatting.YELLOW,
                    TextFormatting.GREEN,
                    TextFormatting.AQUA,
                    TextFormatting.BLUE,
                    TextFormatting.DARK_PURPLE,
                    TextFormatting.LIGHT_PURPLE,
            };
    for (int i = 0; i < stringLength; i++)
    {
      outputString.append(colorChar[(i + Math.abs((int)Minecraft.getSystemTime()) / 50) % 8]).append(parString, i, i + 1);
    }
    return outputString.toString();
  }



  public static class TextColors {
    public static Map<String, TextFormatting> colors = new HashMap<>();

    static {
      colors.put("dense", TextFormatting.BLACK);
      colors.put("redstone", TextFormatting.DARK_RED);
      colors.put("teleporting", TextFormatting.GREEN);
      colors.put("reverse", TextFormatting.DARK_PURPLE);
    }
  }
}
