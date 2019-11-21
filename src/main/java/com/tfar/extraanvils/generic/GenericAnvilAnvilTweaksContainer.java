package com.tfar.extraanvils.generic;

import com.tfar.anviltweaks.AnvilTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class GenericAnvilAnvilTweaksContainer extends AbstractGenericAnvilContainer {

  /**The 2slots where you put your items in that you want to merge and/or rename.*/
  private final AnvilTile blockEntity;

  public GenericAnvilAnvilTweaksContainer(int id, PlayerInventory playerInventory, BlockPos pos) {
    super(id, playerInventory, pos);
    this.blockEntity = (AnvilTile) world.getTileEntity(pos);
    addContainerSlots();
    addPlayerSlots(playerInventory);
  }

  public void addContainerSlots() {
    this.addSlot(new SlotItemHandler(getHandler(), 0, 27, 47) {
      public void onSlotChanged() {
        if (this.getStack().isEmpty()) {
         repairedItemName = "";
        }

        updateRepairOutput();
        blockEntity.angles[0] = blockEntity.rand.nextInt(4);
      }
    });
    this.addSlot(new SlotItemHandler(getHandler(), 1, 76, 47) {
      public void onSlotChanged() {
        updateRepairOutput();
        blockEntity.angles[1] = blockEntity.rand.nextInt(4);
      }
    });
    this.addSlot(new AnvilOutputSlot(this.outputSlot, 2, 134, 47,this));
  }

  @Override
  public ItemStackHandler getHandler() {
    return blockEntity.handler;
  }
}
