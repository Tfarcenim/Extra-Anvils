package com.tfar.extraanvils.generic;

import com.tfar.extraanvils.ExtraAnvils;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiGenericAnvil extends GuiContainer implements IContainerListener {
  private static final ResourceLocation anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
  private ContainerGenericAnvil anvil;
  private GuiTextField nameField;
  private final InventoryPlayer playerInventory;
  private static final ResourceLocation hammer = new ResourceLocation(ExtraAnvils.MODID, "textures/gui/hammer.png");


  public GuiGenericAnvil(InventoryPlayer inventoryIn, World worldIn, BlockGenericAnvil genericAnvil) {
    super(new ContainerGenericAnvil(inventoryIn, worldIn, Minecraft.getMinecraft().player, genericAnvil));
    this.playerInventory = inventoryIn;
    this.anvil = (ContainerGenericAnvil) this.inventorySlots;
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
        i = 16736352;
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

    ALUMINUM(new Color(220,220,220)),
    ARDITE(new Color(255,95,0)),
    BRONZE(new Color(0,12,255)),
    COBALT(new Color(0,12,255)),
    COPPER(new Color(255,127,0)),
    DIAMOND(new Color(0,255,225)),
    ELECTRUM(new Color(255,255,155)),
    ENDSTEEL(new Color(255,255,200)),
    GOLD(new Color(255,245,0)),
    INFERIUM(new Color(50,255,10)),
    INTERMEDIUM(new Color(255,120,0)),
    INVAR(new Color(150,150,150)),
    IRIDIUM(new Color(194,192,216)),
    LEAD(new Color(124,138,181)),
    MANYULLYN(new Color(165,0,255)),
    NICKEL(new Color(255,255,185)),
    PLATINUM(new Color(10, 220, 255)),
    PRUDENTIUM(new Color(0,255,0)),
    STELLAR(new Color(250,250,250)),
    SILVER(new Color(185,235,255)),
    STEEL(new Color(60,60,60)),
    STONE(new Color(130,130,130)),
    SUPERIUM(new Color(0,0,255)),
    SUPREMIUM(new Color(255,0,0)),
    TIN(new Color(171,192,201)),

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