package com.tfar.extraanvils;

import com.google.gson.*;
import com.tfar.extraanvils.generic.GenericAnvilBlock;
import com.tfar.extraanvils.generic.GenericAnvilBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

import static com.tfar.extraanvils.Scripts.g;

public class RegistryHandler {

  private static final String[] array = {"color","durability", "cap", "weight", "enchantability", "playerDamage"};

  private static boolean error = false;
  private static File configFile = new File("config/extraanvils.json");
  private static BufferedInputStream in = new BufferedInputStream(RegistryHandler.class.getResourceAsStream("/"+ExtraAnvils.MODID+".json"));
  public static JsonObject jsonRead;

  static {
    String s;
    try {
      s = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException("The default config is broken, report to mod author asap!", e);
    }
    jsonRead = g.fromJson(s, JsonObject.class).get("anvils").getAsJsonObject();
  }

  public static void writeConfig() {

    if (configFile.exists()) return;

    JsonObject jsonWrite = new JsonObject();
    JsonObject writeAnvil = new JsonObject();

    for (Map.Entry<String, JsonElement> ore : jsonRead.entrySet()) {

      JsonElement readAnvil = ore.getValue();
      JsonObject temp = new JsonObject();
      temp.add("enabled", new JsonPrimitive(true));

      for (String prop : array) {
        temp.add(prop, readAnvil.getAsJsonObject().get(prop));
      }

      JsonArray traits = (JsonArray) readAnvil.getAsJsonObject().get("traits");

      if (traits != null) temp.add("traits", traits);

      writeAnvil.add(ore.getKey(), temp);
    }

    jsonWrite.add("anvils", writeAnvil);

    try {
      FileWriter writer = new FileWriter(configFile);
      writer.write(g.toJson(jsonWrite));
      writer.flush();
    } catch (IOException ugh) {
      //I expect this from a user, but you?!
      throw new RuntimeException("The default config is broken, report to mod author asap!", ugh);
    }
  }


  public static void registerBlocks(IForgeRegistry<Block> registry) {
    writeConfig();
    try {

      FileReader reader = new FileReader(configFile);

      //config configFile
      JsonObject json = (JsonObject) new JsonParser().parse(reader).getAsJsonObject().get("anvils");
      for (Map.Entry<String, JsonElement> ore : json.entrySet()) {
        String material = ore.getKey();
        JsonElement element = ore.getValue();
        AnvilProperties entry = g.fromJson(element,AnvilProperties.class);
        boolean enabled = entry.enabled;
        try {
          if (enabled) {
            ExtraAnvils.logger.info("registering " + material + " anvil");
            for (GenericAnvilBlock.Variant variant : GenericAnvilBlock.Variant.values()) {
              Block.Properties properties = Block.Properties.create(Material.ANVIL).hardnessAndResistance(5,6000);

              GenericAnvilBlock anvil;

          //    if (entry.traits != null && Arrays.asList(entry.traits).contains("reverse"))
            //    anvil = new com.tfar.extraanvils.aether.BlockAetherAnvil(material, entry, variant);
           //   else
                anvil = new GenericAnvilBlock(material, properties, entry, variant);
              ExtraAnvils.anvils.add(anvil);
              anvil.setRegistryName(variant.s + anvil.material + "_anvil");
              registry.register(anvil);
            }
          } else {
            ExtraAnvils.logger.info("skipping " + material + " anvil because it's disabled");
          }
          //in case of exceptions
        } catch (Exception e) {
          ExtraAnvils.logger.error("Error registering " + material + " anvil, skipping", e);
          error = true;
        }
      }
    } catch (IOException ofcourse) {
      throw new RuntimeException(ofcourse);
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

  public static void registerItems(IForgeRegistry<Item> registry) {
    Item.Properties properties = new Item.Properties().group(ExtraAnvils.creativeTab);
    for (GenericAnvilBlock anvil : ExtraAnvils.anvils)
      registry.register(new GenericAnvilBlockItem(anvil,properties).setRegistryName(anvil.getRegistryName()));
    for (GenericAnvilBlock anvil : ExtraAnvils.anvils) {
      switch (anvil.variant) {
        case NORMAL:
          ExtraAnvils.anvilDamageMap
                  .put(anvil, (GenericAnvilBlock) ForgeRegistries
                          .BLOCKS.getValue(new ResourceLocation(ExtraAnvils.MODID,"chipped_"+anvil.getRegistryName().getPath())));
          break;
        case CHIPPED:
          ExtraAnvils.anvilDamageMap
                  .put(anvil, (GenericAnvilBlock) ForgeRegistries
                          .BLOCKS.getValue(new ResourceLocation(ExtraAnvils.MODID,anvil.getRegistryName().getPath()
                                  .replace(GenericAnvilBlock.Variant.CHIPPED.s,
                                          GenericAnvilBlock.Variant.DAMAGED.s))));
          break;
        case DAMAGED:default:
          ExtraAnvils.anvilDamageMap.put(anvil, null);
          break;
      }
    }
  }
}

