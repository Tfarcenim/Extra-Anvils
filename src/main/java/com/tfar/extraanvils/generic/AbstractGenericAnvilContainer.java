package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.compat.Compat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class AbstractGenericAnvilContainer extends Container {

    /**Here comes out item you merged and/or renamed.*/
    final IInventory outputSlot = new CraftResultInventory();

    /**The maximum cost of repairing/renaming in the anvil.*/

    /**The cap for the maximum cost, varies by Anvil.*/
    public int maximumCap;
    /**determined by damage of input item and stackSize of repair materials*/
    public int materialCost;
    String repairedItemName;

    /**The player that has this container open.*/
    PlayerEntity player;

    final IntReferenceHolder maximumCost = IntReferenceHolder.single();
    public BlockPos pos;
    World world;

    public AbstractGenericAnvilContainer(int id, PlayerInventory playerInventory, BlockPos pos) {
      super(ExtraAnvils.ObjectHolders.generic_anvil_container_type,id);
      this.pos = pos;
      this.player = playerInventory.player;
      this.world = player.world;
      this.trackInt(this.maximumCost);
      this.maximumCap = ((GenericAnvilBlock)world.getBlockState(pos).getBlock()).anvilProperties.cap;
    }

    public void addPlayerSlots(PlayerInventory inv){
      for(int i = 0; i < 3; ++i) {
        for(int j = 0; j < 9; ++j) {
          this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        }
      }

      for(int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
      }
    }

    public abstract ItemStackHandler getHandler();

    public static int xpLevelToAmount(int level){
      if (level < 17) return level*level + 6 * level;
      else if (level < 32) return (int)(2.5 * level*level - 40.5 * level + 360);
      else return (int)(4.5 * level*level - 162.5 * level + 2220);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
      detectAndSendChanges();
      this.updateRepairOutput();
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput() {
      ItemStack itemstack = this.getHandler().getStackInSlot(0);
      this.maximumCost.set(1);
      int i = 0;
      int j = 0;
      int k = 0;
      if (itemstack.isEmpty()) {
        this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
        this.maximumCost.set(0);
      } else {
        ItemStack itemstack1 = itemstack.copy();
        ItemStack itemstack2 = this.getHandler().getStackInSlot(1);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
        j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
        this.materialCost = 0;
        boolean flag = false;

        if (!itemstack2.isEmpty()) {
          if (!onAnvilChange(this, itemstack, itemstack2, outputSlot, this.repairedItemName, j)) return;
          flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(itemstack2).isEmpty();
          if (itemstack1.isDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
            int l2 = Math.min(itemstack1.getDamage(), itemstack1.getMaxDamage() / 4);
            if (l2 <= 0) {
              this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
              this.maximumCost.set(0);
              return;
            }

            int i3;
            for(i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
              int j3 = itemstack1.getDamage() - l2;
              itemstack1.setDamage(j3);
              ++i;
              l2 = Math.min(itemstack1.getDamage(), itemstack1.getMaxDamage() / 4);
            }

            this.materialCost = i3;
          } else {
            if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isDamageable())) {
              this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
              this.maximumCost.set(0);
              return;
            }

            if (itemstack1.isDamageable() && !flag) {
              int l = itemstack.getMaxDamage() - itemstack.getDamage();
              int i1 = itemstack2.getMaxDamage() - itemstack2.getDamage();
              int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
              int k1 = l + j1;
              int l1 = itemstack1.getMaxDamage() - k1;
              if (l1 < 0) {
                l1 = 0;
              }

              if (l1 < itemstack1.getDamage()) {
                itemstack1.setDamage(l1);
                i += 2;
              }
            }

            Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
            boolean flag2 = false;
            boolean flag3 = false;

            for(Enchantment enchantment1 : map1.keySet()) {
              if (enchantment1 != null) {
                int i2 = map.getOrDefault(enchantment1, 0);
                int j2 = map1.get(enchantment1);
                j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                boolean flag1 = enchantment1.canApply(itemstack);
                if (this.player.abilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                  flag1 = true;
                }

                for(Enchantment enchantment : map.keySet()) {
                  if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                    flag1 = false;
                    ++i;
                  }
                }

                if (!flag1) {
                  flag3 = true;
                } else {
                  flag2 = true;
                  if (j2 > Compat.getMaxLevel(enchantment1)) {
                    j2 = Compat.getMaxLevel(enchantment1);
                  }

                  map.put(enchantment1, j2);
                  int k3 = 0;
                  switch(enchantment1.getRarity()) {
                    case COMMON:
                      k3 = 1;
                      break;
                    case UNCOMMON:
                      k3 = 2;
                      break;
                    case RARE:
                      k3 = 4;
                      break;
                    case VERY_RARE:
                      k3 = 8;
                  }

                  if (flag) {
                    k3 = Math.max(1, k3 / 2);
                  }

                  i += k3 * j2;
                  if (itemstack.getCount() > 1) {
                    i = maximumCap;
                  }
                }
              }
            }

            if (flag3 && !flag2) {
              this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
              this.maximumCost.set(0);
              return;
            }
          }
        }

        if (StringUtils.isBlank(this.repairedItemName)) {
          if (itemstack.hasDisplayName()) {
            k = 1;
            i += k;
            itemstack1.clearCustomName();
          }
        } else if (!this.repairedItemName.equals(itemstack.getDisplayName().getString())) {
          k = 1;
          i += k;
          itemstack1.setDisplayName(new StringTextComponent(this.repairedItemName));
        }
        if (flag && !itemstack1.isBookEnchantable(itemstack2)) itemstack1 = ItemStack.EMPTY;

        this.maximumCost.set((int)((j + i)/ ((GenericAnvilBlock)world.getBlockState(pos).getBlock()).anvilProperties.enchantability));
        if (i <= 0) {
          itemstack1 = ItemStack.EMPTY;
        }

        if (k == i && k > 0 && this.maximumCost.get() >= maximumCap) {
          this.maximumCost.set(maximumCap - 1);
        }
//The operation cost is too high
        if (this.maximumCost.get() >= maximumCap && !this.player.abilities.isCreativeMode) {
          itemstack1 = ItemStack.EMPTY;
        }

        if (!itemstack1.isEmpty()) {
          int k2 = itemstack1.getRepairCost();
          if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost()) {
            k2 = itemstack2.getRepairCost();
          }

          if (k != i || k == 0) {
            k2 = func_216977_d(k2);
          }

          itemstack1.setRepairCost(k2);
          EnchantmentHelper.setEnchantments(map, itemstack1);
        }

        this.outputSlot.setInventorySlotContents(0, itemstack1);
        this.detectAndSendChanges();
      }
    }

    public static int func_216977_d(int p_216977_0_) {
      return p_216977_0_ * 2 + 1;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
      super.onContainerClosed(playerIn);
      if (!Compat.isAnvilTweaksHere)this.clearContainer(playerIn, world);
    }

    @Override
    protected void clearContainer(PlayerEntity playerIn, World worldIn, IInventory inventoryIn) {
      if (!Compat.isAnvilTweaksHere)
        clearContainer(playerIn, worldIn);
    }

    protected void clearContainer(PlayerEntity playerIn, World worldIn) {
      if (!Compat.isAnvilTweaksHere) {
        if (!playerIn.isAlive() || playerIn instanceof ServerPlayerEntity && ((ServerPlayerEntity)playerIn).hasDisconnected()) {
          for(int j = 0; j < getHandler().getSlots(); ++j) {
            playerIn.dropItem(getHandler().extractItem(j,Integer.MAX_VALUE,false), false);
          }

        } else {
          for(int i = 0; i < getHandler().getSlots(); ++i) {
            playerIn.inventory.placeItemBackInInventory(worldIn, getHandler().extractItem(i,Integer.MAX_VALUE,false));
          }

        }
      }
    }


    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
      return world.getBlockState(pos).getBlock() instanceof GenericAnvilBlock && playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */@Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
        ItemStack itemstack1 = slot.getStack();
        itemstack = itemstack1.copy();
        if (index == 2) {
          if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
            return ItemStack.EMPTY;
          }

          slot.onSlotChange(itemstack1, itemstack);
        } else if (index != 0 && index != 1) {
          if (index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
            return ItemStack.EMPTY;
          }
        } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
          return ItemStack.EMPTY;
        }

        if (itemstack1.isEmpty()) {
          slot.putStack(ItemStack.EMPTY);
        } else {
          slot.onSlotChanged();
        }

        if (itemstack1.getCount() == itemstack.getCount()) {
          return ItemStack.EMPTY;
        }

        slot.onTake(playerIn, itemstack1);
      }

      return itemstack;
    }

    /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    public void updateItemName(String newName) {
      this.repairedItemName = newName;
      if (this.getSlot(2).getHasStack()) {
        ItemStack itemstack = this.getSlot(2).getStack();
        if (StringUtils.isBlank(newName)) {
          itemstack.clearCustomName();
        } else {
          itemstack.setDisplayName(new StringTextComponent(this.repairedItemName));
        }
      }

      this.updateRepairOutput();
    }

    public int getMaxCost() {
      return this.maximumCost.get();
    }

    public static boolean onAnvilChange(AbstractGenericAnvilContainer container, ItemStack left, ItemStack right, IInventory outputSlot, String name, int baseCost) {
      AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost);
      if (MinecraftForge.EVENT_BUS.post(e)) return false;
      if (e.getOutput().isEmpty()) return true;

      outputSlot.setInventorySlotContents(0, e.getOutput());
      container.maximumCost.set(e.getCost());
      container.materialCost = e.getMaterialCost();
      return false;
    }

    public void setMaximumCost(int value) {
      this.maximumCost.set(value);
    }
  }

