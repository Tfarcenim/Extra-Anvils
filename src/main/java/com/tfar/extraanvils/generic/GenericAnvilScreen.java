package com.tfar.extraanvils.generic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.network.Message;
import com.tfar.extraanvils.network.PacketAnvilRename;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class GenericAnvilScreen extends ContainerScreen<AbstractGenericAnvilContainer> implements IContainerListener {
  private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation("textures/gui/container/anvil.png");
  private static final ResourceLocation hammer = new ResourceLocation(ExtraAnvils.MODID, "textures/gui/hammer.png");
  private TextFieldWidget nameField;

  public GenericAnvilScreen(AbstractGenericAnvilContainer container, PlayerInventory inventoryIn, ITextComponent text) {
    super(container,inventoryIn, text);
    this.titleX = 60;
  }

  @Override
  public void init() {
    super.init();
    this.minecraft.keyboardListener.enableRepeatEvents(true);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.nameField = new TextFieldWidget(this.font, i + 62, j + 24, 103, 12,new TranslationTextComponent("container.repair"));
    this.nameField.setCanLoseFocus(false);
    this.nameField.changeFocus(true);
    this.nameField.setTextColor(-1);
    this.nameField.setDisabledTextColour(-1);
    this.nameField.setEnableBackgroundDrawing(false);
    this.nameField.setMaxStringLength(35);
    this.nameField.setResponder(this::syncPacket);
    this.children.add(this.nameField);
    this.container.addListener(this);
  }

  /**
   * Called when the GUI is resized in order to update the world and the resolution
   */
  @Override
  public void resize(@Nonnull Minecraft mc, int w, int h) {
    String s = this.nameField.getText();
    this.init(mc, w, h);
    this.nameField.setText(s);
  }

  @Override
  public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
    if (p_keyPressed_1_ == 256) {
      this.minecraft.player.closeScreen();
    }
    return this.nameField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) || this.nameField.getVisible() || super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);  }

  /**
   * Draw the foreground layer for the GuiContainer (everything in front of the items)
   */
  protected void drawGuiContainerForegroundLayer(MatrixStack stack,int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
    RenderSystem.disableBlend();
    int i = this.container.getMaxCost();
    if (i > 0 || this.container.getSlot(2).canTakeStack(this.playerInventory.player)) {
      int j = 0x80ff20;
      boolean flag = true;
      String s = I18n.format("container.repair.cost", i);
      if (i >= container.maximumCap && !this.minecraft.player.abilities.isCreativeMode) {
        s = I18n.format("container.repair.expensive");
        j = 0xff6060;
      } else if (!this.container.getSlot(2).getHasStack()) {
        flag = false;
      } else if (!this.container.getSlot(2).canTakeStack(this.playerInventory.player)) {
        j = 0xff6060;
      }

      if (flag) {
        int k = this.xSize - 8 - this.font.getStringWidth(s) - 2;
        int l = 69;
        fill(stack,k - 2, 67, this.xSize - 8, 79, 0x4f000000);
        this.font.drawStringWithShadow(stack,s, (float)k, 69, j);
      }
    }
  }

  private void syncPacket(String name) {
    if (!name.isEmpty()) {
      String s = name;
      Slot slot = this.container.getSlot(0);
      if (slot.getHasStack() && !slot.getStack().hasDisplayName() && name.equals(slot.getStack().getDisplayName().getString())) {
        s = "";
      }
      this.container.updateItemName(s);
      Message.INSTANCE.sendToServer(new PacketAnvilRename(s));
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack stack,float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1, 1, 1, 1);
    this.minecraft.getTextureManager().bindTexture(ANVIL_RESOURCE);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.blit(stack,i, j, 0, 0, this.xSize, this.ySize);
    this.blit(stack,i + 59, j + 20, 0, this.ySize + (this.container.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

    if ((this.container.getSlot(0).getHasStack() || this.container.getSlot(1).getHasStack()) && !this.container.getSlot(2).getHasStack()) {
      this.blit(stack,i + 99, j + 45, this.xSize, 0, 28, 21);
    }
    this.minecraft.getTextureManager().bindTexture(hammer);

    Block block = playerInventory.player.world.getBlockState(container.pos).getBlock();
    String color = "#FFFFFF";

    if (block instanceof GenericAnvilBlock)
      color = ((GenericAnvilBlock)playerInventory.player.world.getBlockState(container.pos).getBlock()).anvilProperties.color;

    int raw = 0;

      try {
        raw = Integer.decode(color);
      } catch (NumberFormatException | NullPointerException notANumber){
        ExtraAnvils.logger.error(notANumber);
      }
    RenderSystem.color3f((raw >> 16 & 0xFF)/255f, (raw >> 8 & 0xFF)/255f, (raw & 0xFF)/255f);
    blit(stack,i + 25, j + 7, 0, 0, 22, 22, 22, 22);
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
  public void render(MatrixStack stack,int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(stack);
    super.render(stack,mouseX, mouseY, partialTicks);
    RenderSystem.disableBlend();
    this.nameField.render(stack,mouseX, mouseY, partialTicks);
    this.func_230459_a_(stack,mouseX, mouseY);
  }

  @Override
  public void sendWindowProperty(@Nonnull Container containerIn, int varToUpdate, int newValue) {
  }

}