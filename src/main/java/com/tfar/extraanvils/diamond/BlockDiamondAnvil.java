package com.tfar.extraanvils.diamond;

import com.tfar.extraanvils.GuiHandler;
import com.tfar.extraanvils.ModAnvils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDiamondAnvil extends BlockAnvil {

  protected static final Logger LOGGER = LogManager.getLogger();

  public BlockDiamondAnvil(String name, Properties properties) {
    super(properties);
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
    setRegistryName(name);
  }

  @Override
  public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
    worldIn.playEvent(1031, pos, 0);
  }


  @Override
  public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (!world.isRemote) {
      NetworkHooks.openGui((EntityPlayerMP) player, new DiamondAnvil(GuiHandler.DIAMOND_ANVIL_ID,world,pos), data -> data.writeBlockPos(pos));
    }
    return true;
  }

  @Nullable
  public static IBlockState damage(IBlockState p_196433_0_) {
    Block block = p_196433_0_.getBlock();
    if (block == ModAnvils.blockDiamondAnvil) {
      return ModAnvils.blockDiamondAnvilChipped.getDefaultState().with(FACING, p_196433_0_.get(FACING));
    } else {
      return block == ModAnvils.blockDiamondAnvilChipped ? ModAnvils.blockDiamondAnvilDamaged.getDefaultState().with(FACING, p_196433_0_.get(FACING)) : null;
    }
  }

  public static class DiamondAnvil implements IInteractionObject {
    private final ResourceLocation id;
    private final World world;
    private final BlockPos position;

    public DiamondAnvil(ResourceLocation id, World world, BlockPos pos) {
      this.id = id;
      this.world = world;
      this.position = pos;
    }

    @Override
    @Nonnull
    public ITextComponent getName() {
      return new TextComponentString(id.toString());
    }

    @Override
    public boolean hasCustomName() {
      return false;
    }

    @Override
    public ITextComponent getCustomName() {
      return null;
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory,@Nonnull EntityPlayer player) {
        return new ContainerDiamondAnvil(playerInventory, this.world, this.position, player);
    }

    @Override
    @Nonnull
    public String getGuiID() {
      return id.toString();
    }
  }
}