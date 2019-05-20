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

  public static final String[] array = {"durability multiplier","level cap","weight","enchantability","player damage"};


  public static void writeConfig() {

    //this writes the entire file, I need to fix that

    File file = new File("config/extraanvils.json");
    if (file.exists()) return;

    BufferedInputStream in = new BufferedInputStream(Setup.class.
            getResourceAsStream("/default.json"));
    String s;
    try {
      s = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException("The default config is broken, report to mod author asap!",e);
    }

    JsonObject convertedObject = new Gson().fromJson(s, JsonObject.class);
    JsonObject jsonRead = (JsonObject) convertedObject.get("anvils");
    JsonObject jsonWrite = new JsonObject();
    JsonObject writeAnvil = new JsonObject();

    for (Map.Entry<String,JsonElement> ore: jsonRead.entrySet()) {

      JsonElement readAnvil = ore.getValue();
      JsonObject temp = new JsonObject();
      temp.add("enabled",new JsonPrimitive(true));

      for (String prop : array){
       temp.add(prop,readAnvil.getAsJsonObject().get(prop));
      }
      writeAnvil.add(ore.getKey(),temp);
    }

    jsonWrite.add("anvils",writeAnvil);

      String s1 = prettyJson(jsonWrite);

    try {
      FileWriter writer = new FileWriter(file);
      writer.write(s1);
      writer.flush();
    } catch (IOException ugh) {
      //I expect this from a user, but you?!
      throw new RuntimeException("The default config is broken, report to mod author asap!",ugh);
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

      BufferedInputStream in = new BufferedInputStream(Setup.class.
              getResourceAsStream("/default.json"));
      String s;
      try {
        s = IOUtils.toString(in, Charset.defaultCharset());
      } catch (IOException e) {
        throw new RuntimeException("The default config is broken, report to mod author asap!",e);
      }

      JsonObject convertedObject = new Gson().fromJson(s, JsonObject.class);
      JsonObject jsonRead = (JsonObject) convertedObject.get("anvils");

      JsonObject object = (JsonObject) new JsonParser().parse(reader);

      JsonObject json = (JsonObject)object.get("anvils");

      for (Map.Entry<String,JsonElement> ore: json.entrySet()) {
        String material = ore.getKey();

        JsonObject modids = jsonRead.getAsJsonObject(material);
        boolean flag = checkModlist(modids);
        JsonObject entry = (JsonObject)ore.getValue();
          JsonElement enabled = entry.get("enabled");
          String s1 = enabled == null ? "custom ":"";
          boolean flag2 = enabled == null || enabled.getAsBoolean();
        try {
          if (flag && flag2) {
            ExtraAnvils.logger.info("registering "+s1+ material + " anvil");
            for (EnumVariants variant : EnumVariants.values()) {
              BlockGenericAnvil anvil;
              if ("zanite".equals(material))
                anvil = new BlockAetherAnvil(new AnvilProperties(material, entry.get("level cap").getAsInt(), entry.get("weight").getAsDouble(), 1, entry.get("durability multiplier").getAsDouble(), entry.get("enchantability").getAsDouble(), entry.get("player damage").getAsBoolean()), variant);
              else
                anvil = new BlockGenericAnvil(new AnvilProperties(material, entry.get("level cap").getAsInt(), entry.get("weight").getAsDouble(), 1, entry.get("durability multiplier").getAsDouble(), entry.get("enchantability").getAsDouble(), entry.get("player damage").getAsBoolean()), variant);

              ExtraAnvils.anvils.add(anvil);
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

  public static boolean checkModlist(JsonObject s) {
    if (s == null) return true;

    JsonArray array = s.getAsJsonArray("modid");

    if (array ==  null)return true;

    for (JsonElement mod : array) {
      if (Loader.isModLoaded(mod.getAsString())) return true;
    }
    return false;
  }
}