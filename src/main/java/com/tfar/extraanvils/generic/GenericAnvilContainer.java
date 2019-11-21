package com.tfar.extraanvils.generic;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GenericAnvilContainer extends AbstractGenericAnvilContainer {

  /**The 2slots where you put your items in that you want to merge and/or rename.*/
  private final ItemStackHandler inputslots;

  public GenericAnvilContainer(int id, PlayerInventory playerInventory, BlockPos pos) {
    super(id, playerInventory, pos);
    inputslots = new ItemStackHandler(2);
    addContainerSlots();
    addPlayerSlots(playerInventory);
  }

  public void addContainerSlots() {
    this.addSlot(new SlotItemHandler(getHandler(), 0, 27, 47) {
      public void onSlotChanged() {
        if (this.getStack().isEmpty()) {
          GenericAnvilContainer.this.repairedItemName = "";
        }

        GenericAnvilContainer.this.updateRepairOutput();
      }
    });
    this.addSlot(new SlotItemHandler(getHandler(), 1, 76, 47) {
      public void onSlotChanged() {
        GenericAnvilContainer.this.updateRepairOutput();
      }
    });
    this.addSlot(new AnvilOutputSlot(this.outputSlot, 2, 134, 47,this));
  }

  @Override
  public ItemStackHandler getHandler() {
    return this.inputslots;
  }
}
