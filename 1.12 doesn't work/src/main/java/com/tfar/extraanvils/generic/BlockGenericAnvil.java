package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockGenericAnvil extends BlockFalling {

  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  protected static final AxisAlignedBB X_AXIS_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D);
  protected static final AxisAlignedBB Z_AXIS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D);
  protected static final Logger LOGGER = LogManager.getLogger();
  public AnvilProperties properties;
  public EnumVariants variant;

  public BlockGenericAnvil(AnvilProperties properties,EnumVariants variant) {

    super(Material.ANVIL);
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    setLightOpacity(0);
    setCreativeTab(ExtraAnvils.creativeTab);
    setHardness(5.0F);
    setSoundType(SoundType.ANVIL);
    setResistance(2000.0F);
    setRegistryName(properties.material+variant.getString());
    setTranslationKey(getRegistryName().toString());
    this.properties = properties;
    this.variant = variant;
    ExtraAnvils.anvils.add(this);
  }

  @Override
  public boolean isFullCube(IBlockState state)
  {
    return false;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
    EnumFacing enumfacing = state.getValue(FACING);
    return enumfacing.getAxis() == EnumFacing.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
  }

  @Override
  public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
    DamageHelper.itsAnvilTime(worldIn,pos,this);
  }

  /**
   * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
   * IBlockstate
   */
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();

    try
    {
      return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing);
    }
    catch (IllegalArgumentException var11)
    {
      if (!worldIn.isRemote)
      {
        LOGGER.warn(String.format("Invalid damage property for anvil at %s. Found %d, must be in [0, 1, 2]", pos, meta >> 2));

        if (placer instanceof EntityPlayer)
        {
          placer.sendMessage(new TextComponentTranslation("Invalid damage property. Please pick in [0, 1, 2]"));
        }
      }

      return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(FACING, enumfacing);
    }
  }

  /**
   * Called when the block is right clicked by a player.
   */
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  {
    if (!worldIn.isRemote) {
      playerIn.openGui(ExtraAnvils.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
    }
    return true;
  }

  public void damage(IBlockState state, World world,BlockPos pos) {
    BlockGenericAnvil block = (BlockGenericAnvil) state.getBlock();
    EnumFacing enumfacing = state.getValue(FACING);
    switch (block.variant){
      case NORMAL:world.setBlockState(pos,ForgeRegistries.BLOCKS.getValue(new ResourceLocation(getRegistryName()+"_chipped")).getDefaultState().withProperty(FACING,enumfacing));world.playEvent(1030,pos, 0);return;
      case CHIPPED:world.setBlockState(pos,ForgeRegistries.BLOCKS.getValue(new ResourceLocation(getRegistryName().toString().replace(EnumVariants.CHIPPED.getString(), "")+EnumVariants.DAMAGED.getString())).getDefaultState().withProperty(FACING,enumfacing));world.playEvent(1030,pos, 0);return;
      case DAMAGED:world.setBlockToAir(pos);world.playEvent(1029, pos, 0);

    }
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
  protected void onStartFalling(EntityFallingBlock fallingEntity)
  {
    onStartFall((EntityFallingAnvil)fallingEntity);
  }

  private void onStartFall(EntityFallingAnvil anvil){
    anvil.setHurtEntities(true);
  }

  public boolean isOpaqueCube(IBlockState state)
  {
    return false;
  }


  @Override
  @Nonnull
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  private void checkFallable(World worldIn, BlockPos pos)
  {
    if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0)
    {
      int i = 32;

      if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i)))
      {
        if (!worldIn.isRemote)
        {
          EntityFallingAnvil entityfallinganvil = new EntityFallingAnvil(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
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
  public void updateTick(World worldIn, @Nonnull BlockPos pos, IBlockState state, Random rand)
  {
    if (!worldIn.isRemote)
    {
      this.checkFallable(worldIn, pos);
    }
  }

  @SideOnly(Side.CLIENT)
  public void registerModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
  }
}