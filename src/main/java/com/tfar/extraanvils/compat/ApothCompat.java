package com.tfar.extraanvils.compat;

import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.generic.GenericAnvilBlock;
import com.tfar.extraanvils.generic.GenericAnvilBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BookItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.apotheosis.ApotheosisObjects;
import shadows.apotheosis.advancements.AdvancementTriggers;
import shadows.apotheosis.ench.anvil.TileAnvil;
import shadows.apotheosis.ench.anvil.compat.IAnvilTile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApothCompat extends Compat {
  public static int getActualMaxLevel(Enchantment ench) {
    return shadows.apotheosis.ench.asm.EnchHooks.getMaxLevel(ench);
  }

  public static TileEntity getApothTile() {
    return new GenericAnvilApothOnlyCompatTile();
  }

  public static void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    TileEntity te = world.getTileEntity(pos);
    if (te instanceof IAnvilTile) {
      ((IAnvilTile) te).setUnbreaking(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack));
      ((IAnvilTile) te).setSplitting(EnchantmentHelper.getEnchantmentLevel(ApotheosisObjects.SPLITTING, stack));
    }
  }

  public static List<ItemStack> getDrops(List<ItemStack> oldDrops, BlockState state, LootContext.Builder builder) {
    oldDrops.stream().filter(stack -> stack.getItem() instanceof GenericAnvilBlockItem).findFirst().ifPresent(stack -> {
      TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);
      if (tile instanceof IAnvilTile) {
        IAnvilTile anv = (IAnvilTile) tile;
        Map<Enchantment, Integer> ench = new HashMap<>();
        if (anv.getUnbreaking() > 0) {
          ench.put(Enchantments.UNBREAKING, anv.getUnbreaking());
        }

        if (anv.getSplitting() > 0) {
          ench.put(ExtraAnvils.splitting, anv.getSplitting());
        }
        EnchantmentHelper.setEnchantments(ench, stack);
      }
    });
    return oldDrops;
  }

  public static void onEndFalling(World world, BlockPos pos, BlockState fallState, BlockState hitState) {
    List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
    FallingBlockEntity anvil = world.getEntitiesWithinAABB(FallingBlockEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1))).get(0);
    int split = anvil.tileEntityData.getInt("splitting");
    int ub = anvil.tileEntityData.getInt("ub");
    if (split > 0) for (ItemEntity entity : items) {
      ItemStack stack = entity.getItem();
      if (stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() instanceof BookItem) {
        if (world.rand.nextInt(Math.max(1, 6 - split)) == 0) {
          ListNBT enchants = EnchantedBookItem.getEnchantments(stack);
          if (stack.getItem() instanceof BookItem) enchants = stack.getEnchantmentTagList();
          if (enchants.size() < 1) continue;
          entity.remove();
          for (INBT nbt : enchants) {
            CompoundNBT tag = (CompoundNBT) nbt;
            ItemStack book = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(tag.getString("id"))), tag.getInt("lvl")));
            Block.spawnAsEntity(world, pos.up(), book);
          }
          world.getEntitiesWithinAABB(ServerPlayerEntity.class, new AxisAlignedBB(pos).grow(5, 5, 5), EntityPredicates.NOT_SPECTATING).forEach(p -> {
            AdvancementTriggers.SPLIT_BOOK.trigger(p.getAdvancements());
          });
        }
        double durability = fallState.getBlock() instanceof GenericAnvilBlock ? ((GenericAnvilBlock)fallState.getBlock()).anvilProperties.durability : 1;
        if ((durability == 0 || world.rand.nextDouble()  > ( 1d/(ub+1) * 1 / durability)) && durability >= 0) {
          BlockState dmg = GenericAnvilBlock.damage(fallState);
          if (dmg == null) {
            world.removeBlock(pos, false);
            world.playEvent(1029, pos, 0);
          } else world.setBlockState(pos, dmg);
        }
        break;
      }
    }
  }

  public static void onStartFalling(FallingBlockEntity e){
    TileEntity te = e.world.getTileEntity(new BlockPos(e));
    e.tileEntityData = new CompoundNBT();
    if (te instanceof TileAnvil) {
      te.write(e.tileEntityData);
    }
  }
}
