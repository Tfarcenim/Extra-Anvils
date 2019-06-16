package com.tfar.extraanvils.gold;

import com.tfar.extraanvils.ModAnvils;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class GenericAnvilBlock extends AnvilBlock {

  protected static final Logger LOGGER = LogManager.getLogger();

  public GenericAnvilBlock(String name, Properties properties) {
    super(properties);
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    setRegistryName(name);
  }

  private static final TranslationTextComponent name = new TranslationTextComponent("container.repair");

  @Override
  public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    player.openContainer(state.getContainer(worldIn, pos));
    return true;
  }

  @Nullable
  public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
    return new SimpleNamedContainerProvider((i, playerInventory, player) -> new GenericAnvilContainer(i, playerInventory, IWorldPosCallable.of(worldIn, pos)), name);
  }

  @Nullable
  public static BlockState damage(BlockState p_196433_0_) {
    Block block = p_196433_0_.getBlock();
    if (block == ModAnvils.genericAnvilBlock) {
      return ModAnvils.genericAnvilBlockChipped.getDefaultState().with(FACING, p_196433_0_.get(FACING));
    } else {
      return block == ModAnvils.genericAnvilBlockChipped ? ModAnvils.genericAnvilBlockDamaged.getDefaultState().with(FACING, p_196433_0_.get(FACING)) : null;
    }
  }
}