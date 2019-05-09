package com.tfar.extraanvils.diamond;

import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.network.PacketAnvilRename;
import com.tfar.extraanvils.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GuiDiamondAnvil extends GuiContainer implements IContainerListener {
  private static final ResourceLocation anvilResource = new ResourceLocation(ExtraAnvils.MODID, "textures/gui/diamond_anvil.png");
  private final ContainerDiamondAnvil anvil;
  private GuiTextField nameField;
  private final InventoryPlayer playerInventory;



  public GuiDiamondAnvil(InventoryPlayer inventoryIn, World worldIn) {
    super(new ContainerDiamondAnvil(inventoryIn, worldIn, Minecraft.getInstance().player));
    this.playerInventory = inventoryIn;
    this.anvil = (ContainerDiamondAnvil) this.inventorySlots;
  }

  @Override
  public IGuiEventListener getFocused() {
    return this.nameField.isFocused() ? this.nameField : null;
  }

  @Override
  public void initGui() {
    super.initGui();
    this.mc.keyboardListener.enableRepeatEvents(true);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.nameField = new GuiTextField(0, this.fontRenderer, i + 62, j + 24, 103, 12);
    this.nameField.setTextColor(-1);
    this.nameField.setDisabledTextColour(-1);
    this.nameField.setEnableBackgroundDrawing(false);
    this.nameField.setMaxStringLength(35);
    this.nameField.setTextAcceptHandler(this::syncPacket);
    this.children.add(this.nameField);
    this.inventorySlots.removeListener(this);
    this.inventorySlots.addListener(this);
  }

  /**
   * Called when the GUI is resized in order to update the world and the resolution
   */
  @Override
  public void onResize(@Nonnull Minecraft mcIn, int w, int h) {
    String s = this.nameField.getText();
    this.setWorldAndResolution(mcIn, w, h);
    this.nameField.setText(s);
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    this.mc.keyboardListener.enableRepeatEvents(false);
    this.inventorySlots.removeListener(this);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    GlStateManager.disableLighting();
    GlStateManager.disableBlend();
    this.fontRenderer.drawString(I18n.format("container.repair"), 60, 6, 4210752);
    if (this.anvil.maximumCost > 0) {
      int i = 8453920;
      boolean flag = true;
      String s = I18n.format("container.repair.cost", this.anvil.maximumCost);
      if (this.anvil.maximumCost >= this.anvil.maximumCap && !this.mc.player.abilities.isCreativeMode) {
        s = I18n.format("container.repair.expensive");
        i = 16736352;
      } else if (!this.anvil.getSlot(2).getHasStack()) {
        flag = false;
      } else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
        i = 16736352;
      }

      if (flag) {
        int j = this.xSize - 8 - this.fontRenderer.getStringWidth(s) - 2;
        int k = 69;
        drawRect(j - 2, 67, this.xSize - 8, 79, 1325400064);
        this.fontRenderer.drawStringWithShadow(s, (float) j, 69, i);
      }
    }

    GlStateManager.enableLighting();
  }

  private void syncPacket(int unused, String name) {
    if (!name.isEmpty()) {
      String s = name;
      Slot slot = this.anvil.getSlot(0);
      if (slot.getHasStack() && !slot.getStack().hasDisplayName() && name.equals(slot.getStack().getDisplayName().getString())) {
        s = "";
      }
      this.anvil.updateItemName(s);
      Message.INSTANCE.sendToServer(new PacketAnvilRename(s));
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(anvilResource);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    this.drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

    if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack()) {
      this.drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
    }
  }

  @Override
  public void sendAllContents(@Nonnull Container containerToSend, @Nonnull NonNullList<ItemStack> itemsList) {
    this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
  }

  /**
   * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
   * contents of that slot.
   */
  @Override
  public void sendSlotContents(@Nonnull Container containerToSend, int slotInd, @Nonnull ItemStack stack) {
    if (slotInd == 0) {
      this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName().getString());
      this.nameField.setEnabled(!stack.isEmpty());
    }

  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    GlStateManager.disableLighting();
    GlStateManager.disableBlend();
    this.nameField.drawTextField(mouseX, mouseY, partialTicks);
  }

  @Override
  public void sendWindowProperty(@Nonnull Container containerIn, int varToUpdate, int newValue) {
  }

  @Override
  public void sendAllWindowProperties(@Nonnull Container containerIn, @Nonnull IInventory inventory) {
  }
}
