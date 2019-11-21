package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.AnvilProperties;
import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.compat.Compat;
import com.tfar.extraanvils.entity.FallingAnvilEntity;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class GenericAnvilBlock extends AnvilBlock {

  public String material;
  public AnvilProperties anvilProperties;
  public Variant variant;

  public GenericAnvilBlock(String material, Properties properties, AnvilProperties anvilProperties,Variant variant) {
    super(properties);
    this.material = material;
    this.anvilProperties = anvilProperties;
    this.variant = variant;
  }

  @Override
  public boolean onBlockActivated(BlockState state,@Nonnull World worldIn,@Nonnull BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (!worldIn.isRemote) NetworkHooks.openGui((ServerPlayerEntity) player, new GenericAnvilContainerProvider(pos), pos);
    return true;
  }

  @Override
  public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
    if (!worldIn.isRemote) {
      this.checkFallable(worldIn, pos);
    }
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return Compat.hasTileEntity;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return Compat.createTileEntity();
  }

  private void checkFallable(World worldIn, BlockPos pos) {
    if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
      if (!worldIn.isRemote) {
        FallingAnvilEntity fallingblockentity = new FallingAnvilEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
        this.onStartFalling(fallingblockentity);
        worldIn.addEntity(fallingblockentity);
      }
    }
  }

  protected void onStartFalling(FallingAnvilEntity fallingEntity) {
    fallingEntity.setHurtEntities(true);
  }

  @Nullable
  public static BlockState damage(BlockState state) {
    Block block = ExtraAnvils.anvilDamageMap.get(state.getBlock());
    return block == null ? null : block.getDefaultState().with(FACING,state.get(FACING));
  }
  public enum Variant{
    NORMAL(""),
    CHIPPED("chipped_"),
    DAMAGED("damaged_");

    public String s;

    Variant(String variant){
      this.s = variant;
    }
  }
}