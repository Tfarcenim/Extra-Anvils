package com.tfar.extraanvils;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockGoldAnvil extends BlockAnvil {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
  //  public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 2);
    protected static final Logger LOGGER = LogManager.getLogger();
    protected ItemArmor.ArmorMaterial material;
    private String suffix;
    private String name;

    public ItemArmor.ArmorMaterial getArmorMaterial() {
        return material;
    }

    public BlockGoldAnvil(String name, String suffix) {
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setLightOpacity(0);
        this.material = ItemArmor.ArmorMaterial.GOLD;
        setHardness(5.0F);
        setSoundType(SoundType.ANVIL);
        setResistance(2000.0F);
        setTranslationKey(name+suffix);
        setRegistryName(name+suffix);
        setCreativeTab(ExtraAnvils.tabExtraAnvils);
        this.suffix = suffix;
        this.name = name;
        ModAnvils.anvils.add(this);
        ModAnvils.anvilitems.add(new ItemBlock(this).setRegistryName(name+suffix));
    }

    public String getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
            items.add(new ItemStack(this));
        }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();

        try {
            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing).withProperty(DAMAGE, meta >> 2);
        } catch (IllegalArgumentException var11) {
            if (!worldIn.isRemote) {
                LOGGER.warn(String.format("Invalid damage property for anvil at %s. Found %d, must be in [0, 1, 2]", pos, meta >> 2));

                if (placer instanceof EntityPlayer) {
                    placer.sendMessage(new TextComponentTranslation("Invalid damage property. Please pick in [0, 1, 2]"));
                }
            }

            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(FACING, enumfacing).withProperty(DAMAGE, 0);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        if (!playerIn.isSneaking()) {
            playerIn.openGui(ExtraAnvils.MODID, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(DAMAGE);
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
        worldIn.playEvent(1031, pos, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.getBlock() != this ? state : state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, DAMAGE);
    }

}