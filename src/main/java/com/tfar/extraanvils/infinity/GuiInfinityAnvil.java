package com.tfar.extraanvils.infinity;

import com.tfar.extraanvils.generic.BlockGenericAnvil;
import com.tfar.extraanvils.network.PacketAnvilRename;
import com.tfar.extraanvils.network.PacketHandler;
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
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.tfar.extraanvils.generic.GuiGenericAnvil.anvilResource;
import static com.tfar.extraanvils.generic.GuiGenericAnvil.hammer;

@SideOnly(Side.CLIENT)
public class GuiInfinityAnvil extends GuiContainer implements IContainerListener {
  private ContainerInfinityAnvil anvil;
  private GuiTextField nameField;
  private final InventoryPlayer playerInventory;


  public GuiInfinityAnvil(InventoryPlayer inventoryIn, World worldIn, BlockGenericAnvil genericAnvil) {
    super(new ContainerInfinityAnvil(inventoryIn, worldIn, Minecraft.getMinecraft().player, genericAnvil));
    this.playerInventory = inventoryIn;
    this.anvil = (ContainerInfinityAnvil) this.inventorySlots;
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.nameField = new GuiTextField(0, this.fontRenderer, i + 62, j + 24, 103, 12);
    this.nameField.setTextColor(-1);
    this.nameField.setDisabledTextColour(-1);
    this.nameField.setEnableBackgroundDrawing(false);
    this.nameField.setMaxStringLength(Integer.MAX_VALUE);
    this.inventorySlots.removeListener(this);
    this.inventorySlots.addListener(this);
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    Keyboard.enableRepeatEvents(false);
    this.inventorySlots.removeListener(this);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    GlStateManager.disableLighting();
    GlStateManager.disableBlend();
    this.fontRenderer.drawString(I18n.format("container.repair"), 60, 6, 0x404040);

    if (this.anvil.maximumCost >= 0) {
      int i = 0x80ff20;
      boolean flag = true;
      String s = I18n.format("container.repair.cost", this.anvil.maximumCost);

      if (this.anvil.maximumCost >= this.anvil.maximumCap && !this.mc.player.capabilities.isCreativeMode) {
        s = I18n.format("container.repair.expensive");
        i = 0xff6060;
      } else if (!this.anvil.getSlot(2).getHasStack()) {
        flag = false;
      } else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
        i = 0xff6060;
      }

      if (flag) {
        int j = 0xff000000 | (i & 0xfcfcfc) >> 2 | i & 0xff000000;
        int k = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
        int l = 67;

        if (this.fontRenderer.getUnicodeFlag()) {
          drawRect(k - 3, l - 2, this.xSize - 7, l + 10, 0xff000000);
          drawRect(k - 2, l - 1, this.xSize - 8, l + 9, 0xff3b3b3b);
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

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.nameField.textboxKeyTyped(typedChar, keyCode)) {
      renameItem();
    } else {
      super.keyTyped(typedChar, keyCode);
    }
  }

  private void renameItem() {
    String s = this.nameField.getText();
    //TODO: Make sure this works
    Slot slot = this.anvil.getSlot(0);
    if (slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName())) {
      s = "";
    }
    anvil.updateItemName(s);
    PacketHandler.INSTANCE.sendToServer(new PacketAnvilRename(s));
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    renderHoveredToolTip(mouseX, mouseY);
    GlStateManager.disableLighting();
    GlStateManager.disableBlend();
    nameField.drawTextBox();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(anvilResource);
    int i = (width - xSize) / 2;
    int j = (height - ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    this.drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

    if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack()) {
      this.drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
    }

    mc.getTextureManager().bindTexture(hammer);
    EnumAnvilType.getType(anvil.name).color.setColor();
    drawModalRectWithCustomSizedTexture(i + 25, j + 7, 0, 0, 22, 22, 22, 22);
    Color.reset();
  }

  @Override
  public void sendAllContents(@Nonnull Container containerToSend, @Nonnull NonNullList<ItemStack> itemsList) {
    this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
  }

  @Override
  public void sendSlotContents(@Nonnull Container containerToSend, int slotInd, @Nonnull ItemStack stack) {
    if (slotInd == 0) {
      this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName());
      this.nameField.setEnabled(!stack.isEmpty());

      if (!stack.isEmpty()) {
        this.renameItem();
      }
    }
  }

  @Override
  public void sendWindowProperty(@Nonnull Container containerIn, int varToUpdate, int newValue) {
  }

  @Override
  public void sendAllWindowProperties(@Nonnull Container containerIn, @Nonnull IInventory inventory) {
  }

  public enum EnumAnvilType {
    //if this color is seen, this is a missed color and should be filled in asap
    ERROR(new Color(0,0,0)),

    INFINITY(new Color(255,200,200)),
    ;

    public final Color color;

    EnumAnvilType(Color color) {
      this.color = color;
    }

    public static EnumAnvilType getType(String s){
      try {
        return EnumAnvilType.valueOf(s.toUpperCase());
      } catch (IllegalArgumentException ignored){
        //it isn't there
        return ERROR;
      }
    }
  }
  public static class Color {
    public final int r,g,b,a;

    Color(int red, int green, int blue) {
      this(red, green, blue, 255);
    }

    Color(int red, int green, int blue, int alpha) {
      r = red; g = green; b = blue; a = alpha;
    }

    public void setColor() {
      GlStateManager.color(r/255f,g/255f,b/255f,a/255f);
    }
    public static void reset(){
      GlStateManager.color(1,1,1,1);
    }
  }
}