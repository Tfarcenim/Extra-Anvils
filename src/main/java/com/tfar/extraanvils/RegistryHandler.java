package com.tfar.extraanvils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tfar.extraanvils.generic.GenericAnvilBlock;
import com.tfar.extraanvils.generic.GenericAnvilBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class RegistryHandler {


  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  private static boolean error = false;
  private static File configFile = new File("config/extraanvils.json");
  private static BufferedInputStream in = new BufferedInputStream(RegistryHandler.class.getResourceAsStream("/extraanvils.json"));
  private static AnvilProperties[] jsonRead;

  static {
    String s;
    try {
      s = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException("Impossible, how could this happen?", e);
    }
    jsonRead = g.fromJson(s, AnvilProperties[].class);
  }

  public static void writeConfig() {

    if (configFile.exists()) return;

    try {
      FileWriter writer = new FileWriter(configFile);
      writer.write(g.toJson(jsonRead));
      writer.flush();
    } catch (IOException ugh) {
      //I expect this from a user, but you?!
      throw new RuntimeException("Impossible, how could this happen?", ugh);
    }
  }


  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    writeConfig();
    try {
      FileReader reader = new FileReader("config/extraanvils.json");
      AnvilProperties[] anvilProperties = g.fromJson(reader, AnvilProperties[].class);
      for (AnvilProperties anvilProperties1 : anvilProperties) {
        String name = anvilProperties1.name;
        try {
          ExtraAnvils.logger.info("registering " + name + " anvil");
          for (GenericAnvilBlock.Variant variant : GenericAnvilBlock.Variant.values()) {
            Block.Properties properties = Block.Properties.from(Blocks.ANVIL);
            GenericAnvilBlock anvil = new GenericAnvilBlock(name, properties, anvilProperties1, variant);
            ExtraAnvils.anvils.add(anvil);
            ExtraAnvils.register(anvil,variant.s + name+"_anvil",event.getRegistry());
          }
          //in case of exceptions
        } catch (Exception e) {
          ExtraAnvils.logger.error("Error registering " + name + " anvil, skipping", e);
          error = true;
        }
      }
    } catch (IOException e) {
    }
  }

  @SubscribeEvent
  public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
    if (error) {
      ITextComponent component = new StringTextComponent(TextFormatting.WHITE + "[")
              .appendSibling(new TranslationTextComponent("extraanvils.name").setStyle(new Style().setColor(TextFormatting.AQUA)))
              .appendSibling(new StringTextComponent("]: ").setStyle(new Style().setColor(TextFormatting.WHITE)))
              .appendSibling(new TranslationTextComponent("extraanvils.error")).setStyle(new Style().setColor(TextFormatting.RED));
      e.getPlayer().sendMessage(component);
    }
  }

  public static void registerItems(RegistryEvent.Register<Item> event) {
    Item.Properties properties = new Item.Properties().group(ExtraAnvils.creativeTab);
    ExtraAnvils.anvils.forEach(anvil -> ExtraAnvils.register(new GenericAnvilBlockItem(anvil, properties), anvil.getRegistryName(), event.getRegistry()));
    for (GenericAnvilBlock anvil : ExtraAnvils.anvils) {
      switch (anvil.variant) {
        case NORMAL:
          ExtraAnvils.anvilDamageMap
                  .put(anvil, (GenericAnvilBlock) ForgeRegistries
                          .BLOCKS.getValue(new ResourceLocation(ExtraAnvils.MODID, "chipped_" + anvil.getRegistryName().getPath())));
          break;
        case CHIPPED:
          ExtraAnvils.anvilDamageMap
                  .put(anvil, (GenericAnvilBlock) ForgeRegistries
                          .BLOCKS.getValue(new ResourceLocation(ExtraAnvils.MODID, anvil.getRegistryName().getPath()
                                  .replace(GenericAnvilBlock.Variant.CHIPPED.s,
                                          GenericAnvilBlock.Variant.DAMAGED.s))));
          break;
        case DAMAGED:
        default:
          ExtraAnvils.anvilDamageMap.put(anvil, null);
          break;
      }
    }
  }
}

