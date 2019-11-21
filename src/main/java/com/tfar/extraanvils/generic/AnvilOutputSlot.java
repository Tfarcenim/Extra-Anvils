package com.tfar.extraanvils.generic;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import static com.tfar.extraanvils.generic.AbstractGenericAnvilContainer.xpLevelToAmount;

public class AnvilOutputSlot extends Slot {

  private final AbstractGenericAnvilContainer container;

  public AnvilOutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, AbstractGenericAnvilContainer container) {
    super(inventoryIn, index, xPosition, yPosition);
    this.container = container;
  }

  /**
   * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
   */
  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  /**
   * Return whether this slot's stack can be taken from this slot.
   */
  @Override
  public boolean canTakeStack(PlayerEntity playerIn) {
    return (playerIn.abilities.isCreativeMode || playerIn.experienceLevel >= container.maximumCost.get()) && this.getHasStack();
  }

  @Nonnull
  @Override
  public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
    if (!thePlayer.abilities.isCreativeMode) {
      thePlayer.giveExperiencePoints(-xpLevelToAmount(container.maximumCost.get()));
    }

    float breakChance = net.minecraftforge.common.ForgeHooks.onAnvilRepair(thePlayer, stack, container.getHandler().getStackInSlot(0), container.getHandler().getStackInSlot(1));

    double durability = ((GenericAnvilBlock)container.world.getBlockState(container.pos).getBlock()).anvilProperties.durability;

    if (durability > 0)
      breakChance /= durability;
    else if (durability == 0)breakChance = 1;

    container.getHandler().setStackInSlot(0, ItemStack.EMPTY);
    if (container.materialCost > 0) {
      ItemStack itemstack = container.getHandler().getStackInSlot(1);
      if (!itemstack.isEmpty() && itemstack.getCount() > container.materialCost) {
        itemstack.shrink(container.materialCost);
        container.getHandler().setStackInSlot(1, itemstack);
      } else {
        container.getHandler().setStackInSlot(1, ItemStack.EMPTY);
      }
    } else {
      container.getHandler().setStackInSlot(1, ItemStack.EMPTY);
    }

    container.maximumCost.set(0);
    BlockState blockstate = container.player.world.getBlockState(container.pos);
    if (durability >= 0 && !thePlayer.abilities.isCreativeMode && blockstate.getBlock()
            instanceof GenericAnvilBlock && thePlayer.getRNG().nextFloat() < breakChance) {
      BlockState blockstate1 = GenericAnvilBlock.damage(blockstate);
      if (blockstate1 == null) {
        container.world.removeBlock(container.pos, false);
        container.world.playEvent(1029, container.pos, 0);
      } else {
        container.world.setBlockState(container.pos, blockstate1, 2);
        container.world.playEvent(1030, container.pos, 0);
      }
    } else {
      container.world.playEvent(1030, container.pos, 0);
    }
    return stack;
  }
}