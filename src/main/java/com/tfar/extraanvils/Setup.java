package com.tfar.extraanvils;

import com.google.gson.*;
import com.tfar.extraanvils.generic.AnvilProperties;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

@Mod.EventBusSubscriber
public class Setup {

  private static final String[] array = {"durability", "cap", "weight", "enchantability", "playerDamage"};

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  private static boolean error = false;
  private static File configFile = new File("config/extraanvils.json");
  private static BufferedInputStream in = new BufferedInputStream(Setup.class.getResourceAsStream("/default.json"));
  //hardcoded into the mod, used for oredict, jsons and modids
  static JsonObject jsonRead;

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


  //some people say incode recipes are bad, but this is 1.12 so it doesn't matter yet
  @SubscribeEvent
  public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

    IForgeRegistry<IRecipe> registry = event.getRegistry();
    for (BlockGenericAnvil anvil : ExtraAnvils.anvils) {
      if (anvil.variant != EnumVariants.NORMAL)
        continue;

      Ores ores = g.fromJson(jsonRead.get(anvil.material), Ores.class);
      if (ores == null) continue;

      IRecipe recipe = new ShapedOreRecipe(new ResourceLocation(ExtraAnvils.MODID, anvil.material + "anvil"), new ItemStack(anvil),

              "bbb",
              " i ",
              "iii",
              'b', ores.ore1,
              'i', ores.ore2);

      recipe.setRegistryName(new ResourceLocation(ExtraAnvils.MODID, anvil.material + "anvil"));
      registry.register(recipe);
    }
  }

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    writeConfig();

    IForgeRegistry<Block> registry = event.getRegistry();
    try {

      FileReader reader = new FileReader("config/extraanvils.json");

      //config configFile
      JsonObject json = (JsonObject) new JsonParser().parse(reader).getAsJsonObject().get("anvils");
      for (Map.Entry<String, JsonElement> ore : json.entrySet()) {
        String material = ore.getKey();
        JsonElement element = ore.getValue();
        AnvilProperties entry = g.fromJson(element,AnvilProperties.class);
        boolean enabled = entry.enabled;
        JsonObject builtin = jsonRead.getAsJsonObject(material);
        String[] modids = builtin == null ? null : g.fromJson(builtin.get("modid"), String[].class);
        boolean hasMods = checkModlist(modids);
        try {
          if (hasMods && enabled) {
            ExtraAnvils.logger.info("registering " + material + " anvil");
            for (EnumVariants variant : EnumVariants.values()) {
              BlockGenericAnvil anvil;

              if (entry.traits != null && Arrays.asList(entry.traits).contains("reverse"))
                anvil = new com.tfar.extraanvils.aether.BlockAetherAnvil(material, entry, variant);
              else
                anvil = new BlockGenericAnvil(material, entry, variant);
               ExtraAnvils.anvils.add(anvil);
              anvil.setRegistryName(anvil.material + variant.getString());
              anvil.setTranslationKey(anvil.getRegistryName().toString());
              registry.register(anvil);
            }
          } else {
            ExtraAnvils.logger.info("skipping " + material + " anvil" + ((hasMods) ? "" : " due to missing mod(s)") + (enabled ? "" : " because it's disabled"));
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

  public static boolean checkModlist(String[] s) {
    return s == null || Arrays.stream(s).anyMatch(Loader::isModLoaded);
  }

  @SubscribeEvent
  public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
    if (error) {
      ITextComponent component = new TextComponentString(TextFormatting.WHITE + "[")
              .appendSibling(new TextComponentTranslation("extraanvils.name").setStyle(new Style().setColor(TextFormatting.AQUA)))
              .appendSibling(new TextComponentString("]: ").setStyle(new Style().setColor(TextFormatting.WHITE)))
              .appendSibling(new TextComponentTranslation("extraanvils.error")).setStyle(new Style().setColor(TextFormatting.RED));
      e.player.sendMessage(component);
    }
  }

  public static class Ores {
    public String ore1;
    public String ore2;
  }
}