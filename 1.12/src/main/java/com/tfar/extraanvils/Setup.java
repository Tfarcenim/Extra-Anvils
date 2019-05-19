package com.tfar.extraanvils;

import com.google.gson.*;
import com.tfar.extraanvils.generic.AnvilProperties;
import com.tfar.extraanvils.generic.BlockAetherAnvil;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

@Mod.EventBusSubscriber
public class Setup {

  public static void writeConfig() {

    File file = new File("config/extraanvils.json");
    if (file.exists()) return;

    BufferedInputStream in = new BufferedInputStream(Setup.class.
            getResourceAsStream("/default.json"));
    String s = null;
    try {
      s = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
    }

    JsonObject convertedObject = new Gson().fromJson(s, JsonObject.class);

    String s1 = prettyJson(convertedObject);

    try {
      FileWriter writer = new FileWriter(file);
      writer.write(s1);
      writer.flush();
    } catch (IOException ugh) {
      //it be like that sometimes
      ugh.printStackTrace();
    }
  }

  public static String prettyJson(JsonObject j) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(j);
  }

  @SubscribeEvent
  public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
    IForgeRegistry<IRecipe> registry = event.getRegistry();

    for (BlockGenericAnvil anvil : ExtraAnvils.anvils) {
      if (anvil.variant != EnumVariants.NORMAL || anvil.properties.material.equals("vibrant_alloy") || anvil.properties.material.equals("energetic_alloy"))
        continue;
      IRecipe recipe = new ShapedOreRecipe(new ResourceLocation(ExtraAnvils.MODID, anvil.properties.material + "anvil"), new ItemStack(anvil),

              "bbb",
              " i ",
              "iii", 'b', "block" + anvil.properties.material.substring(0, 1).toUpperCase() + anvil.properties.material.substring(1),
              'i', "ingot" + anvil.properties.material.substring(0, 1).toUpperCase() + anvil.properties.material.substring(1));

      recipe.setRegistryName(new ResourceLocation(ExtraAnvils.MODID, anvil.properties.material + "anvil"));
      registry.register(recipe);
    }
  }


  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    writeConfig();


    IForgeRegistry<Block> registry = event.getRegistry();
    try {

      FileReader reader = new FileReader("config/extraanvils.json");
      JsonObject object = (JsonObject) new JsonParser().parse(reader);

      JsonObject json = (JsonObject)object.get("anvils");

      for (Map.Entry<String,JsonElement> ore: json.entrySet()) {
        String material = ore.getKey();

        JsonObject entry = (JsonObject)ore.getValue();
        try {
          boolean flag = entry.get("enabled").getAsBoolean();

          if (flag && checkModlist(entry.getAsJsonArray("modid"))) {
            ExtraAnvils.logger.info("registering " + material + " anvil");
            for (EnumVariants variant : EnumVariants.values()) {
              BlockGenericAnvil anvil;
              if ("zanite".equals(material))
                anvil = new BlockAetherAnvil(new AnvilProperties(material, entry.get("level cap").getAsInt(), entry.get("weight").getAsDouble(), 1, entry.get("durability multiplier").getAsDouble(), entry.get("enchantability").getAsDouble(), entry.get("player damage").getAsBoolean()), variant);
              else
                anvil = new BlockGenericAnvil(new AnvilProperties(material, entry.get("level cap").getAsInt(), entry.get("weight").getAsDouble(), 1, entry.get("durability multiplier").getAsDouble(), entry.get("enchantability").getAsDouble(), entry.get("player damage").getAsBoolean()), variant);

              anvil.setRegistryName(anvil.properties.material + variant.getString());
              anvil.setTranslationKey(anvil.getRegistryName().toString());
              registry.register(anvil);
            }
          } else {
            ExtraAnvils.logger.info("skipping " + material + " anvil");
          }
          // eat the exception
        } catch (Exception ignored) {
          ExtraAnvils.logger.error("Error registering " + material + ", skipping");
        }
      }
    } catch (IOException ofcourse) {
      throw new RuntimeException(ofcourse);
    }
  }

  public static boolean checkModlist(JsonArray s) {

    if (s == null) return true;
    for (JsonElement mod : s) {
      if (Loader.isModLoaded(mod.getAsString())) return true;
    }
    return false;
  }
}