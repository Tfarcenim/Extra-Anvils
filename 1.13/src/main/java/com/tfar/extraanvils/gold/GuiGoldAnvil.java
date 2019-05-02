package com.tfar.extraanvils.gold;

import com.tfar.extraanvils.ExtraAnvils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketRenameItem;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GuiGoldAnvil extends GuiContainer implements IContainerListener {
  private static final ResourceLocation anvilResource = new ResourceLocation(ExtraAnvils.MODID,"textures/gui/gold_anvil.png");
 // public static HashMap<String, ResourceLocation> hammers = Maps.newHashMap();
  private ContainerGoldAnvil anvil;
  private GuiTextField nameField;
  public InventoryPlayer playerInventory;
  public World anvWorld;

  public GuiGoldAnvil(InventoryPlayer inventoryIn, World worldIn) {
    super(new ContainerGoldAnvil(inventoryIn, worldIn, Minecraft.getInstance().player));
    this.playerInventory = inventoryIn;
    this.anvil = (ContainerGoldAnvil) this.inventorySlots;
    this.anvWorld = worldIn;
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
    this.nameField.setMaxStringLength(50);
    this.nameField.setTextAcceptHandler(this::func_195393_a);
    this.children.add(this.nameField);
    this.inventorySlots.removeListener(this);
    this.inventorySlots.addListener(this);
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
        int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
        int k = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
        int l = 67;

        if (this.fontRenderer.getBidiFlag()) {
          drawRect(k - 3, l - 2, this.xSize - 7, l + 10, -16777216);
          drawRect(k - 2, l - 1, this.xSize - 8, l + 9, -12895429);
        } else {
          this.fontRenderer.drawString(s, k, l + 1, j);
          this.fontRenderer.drawString(s, k + 1, l, j);
          this.fontRenderer.drawString(s, k + 1, l + 1, j);
        }

        this.fontRenderer.drawString(s, k, l, i);
      }
    }

    GlStateManager.enableLighting();
  }

  private void func_195393_a(int p_195393_1_, String p_195393_2_) {
    if (!p_195393_2_.isEmpty()) {
      String s = p_195393_2_;
      Slot slot = this.anvil.getSlot(0);
      if (slot.getHasStack() && !slot.getStack().hasDisplayName() && p_195393_2_.equals(slot.getStack().getDisplayName().getString())) {
        s = "";
      }

      this.anvil.updateItemName(s);
      this.mc.player.connection.sendPacket(new CPacketRenameItem(s));
    }
  }

  /*@Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    GlStateManager.disableLighting();
    GlStateManager.disableBlend();
    this.nameField.drawTextBox();
  } */

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
  //  hammers.putIfAbsent(anvil.getName(), new ResourceLocation("moreanvils:textures/gui/"+anvil.getName().toLowerCase()+"_hammer.png"));

   // this.mc.getTextureManager().bindTexture(hammers.get(anvil.getName()));
   // drawModalRectWithCustomSizedTexture(i+25, j+7, 0, 0, 22, 22, 22, 22);
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
  public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
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
    this.nameField.drawTextField(mouseX, mouseY, partialTicks);  }

  @Override
  public void sendWindowProperty(@Nonnull Container containerIn, int varToUpdate, int newValue) {
  }

  @Override
  public void sendAllWindowProperties(@Nonnull Container containerIn, @Nonnull IInventory inventory) {
  }
}


