package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.AnvilProperties;
import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.compat.Compat;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

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
  public ActionResultType func_225533_a_(BlockState p_225533_1_, World world, BlockPos pos, PlayerEntity player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
    if (!world.isRemote) NetworkHooks.openGui((ServerPlayerEntity) player, new GenericAnvilContainerProvider(pos), pos);
    return ActionResultType.SUCCESS;
  }

  private void checkFallable(World worldIn, BlockPos pos) {
    if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
      if (!worldIn.isRemote) {
        FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
        this.onStartFalling(fallingblockentity);
        worldIn.addEntity(fallingblockentity);
      }
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

  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    if (Compat.isApothesisHere){
      //ApothCompat.onBlockPlacedBy(world, pos, state, placer, stack);
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (Compat.isApothesisHere && !stack.hasEffect())
      tooltip.add(new TranslationTextComponent("info.apotheosis.anvil"));
  }

  @Override
  protected void onStartFalling(FallingBlockEntity e) {
    super.onStartFalling(e);
    if (Compat.isApothesisHere) {
      //ApothCompat.onStartFalling(e);
    }
  }

  @Override
  public void onEndFalling(World world, BlockPos pos, BlockState fallState, BlockState hitState) {
    super.onEndFalling(world, pos, fallState, hitState);
    if (Compat.isApothesisHere){
      //ApothCompat.onEndFalling(world, pos, fallState, hitState);
    }
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