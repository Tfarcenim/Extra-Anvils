package com.tfar.extraanvils;

import com.google.gson.*;
import com.tfar.extraanvils.generic.AnvilProperties;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.tfar.extraanvils.ModId.*;
import static java.util.Arrays.stream;

@Mod.EventBusSubscriber
public class Materials {

  public static Map<String, String[]> MATERIAL_TO_MODID = new HashMap<>();

  static {

    String[] vanilla = {"vanilla"};

    MATERIAL_TO_MODID.put("diamond",vanilla);

    MATERIAL_TO_MODID.put("gold",vanilla);

    MATERIAL_TO_MODID.put("stone",vanilla);

    String[] inferium = {MYSTICAL_AGRICULTURE},prudentium = {MYSTICAL_AGRICULTURE},intermedium = {MYSTICAL_AGRICULTURE},superium = {MYSTICAL_AGRICULTURE},supremium = {MYSTICAL_AGRICULTURE};

    MATERIAL_TO_MODID.put("inferium", inferium);
    MATERIAL_TO_MODID.put("intermedium",intermedium);
    MATERIAL_TO_MODID.put("prudentium", prudentium);
    MATERIAL_TO_MODID.put("superium", superium);
    MATERIAL_TO_MODID.put("supremium", supremium);

    String[] aluminum = {IMMERSIVE_ENGINEERING, THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("aluminum", aluminum);

    String[] ardite = {TCONSTRUCT};
    MATERIAL_TO_MODID.put("ardite",ardite);

    String[] bronze = {EMBERS, THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING, FORESTRY,MEKANISM,NUCLEARCRAFT, PROJECT_RED};
    MATERIAL_TO_MODID.put("bronze", bronze);

    String[] cobalt = {TCONSTRUCT};
    MATERIAL_TO_MODID.put("cobalt",cobalt);

    String[] copper = {EMBERS, THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING, FORESTRY,MEKANISM,NUCLEARCRAFT, PROJECT_RED};
    MATERIAL_TO_MODID.put("copper", copper);

    String[] electrum = {IMMERSIVE_ENGINEERING, THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("electrum", electrum);

    String[] endsteel = {ENDER_IO};
    MATERIAL_TO_MODID.put("endsteel",endsteel);

    String[] invar = {THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("invar", invar);

    String[] iridium = {IC2, THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("iridium", iridium);

    String[] lead = {THERMAL_FOUNDATION, EMBERS, NUCLEARCRAFT};
    MATERIAL_TO_MODID.put("lead", lead);

    String[] manyullyn = {TCONSTRUCT};
    MATERIAL_TO_MODID.put("manyullyn", manyullyn);

    String[] nickel = {THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("nickel", nickel);

    String[] platinum = {THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("platinum", platinum);

    String[] silver = {THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING, NUCLEARCRAFT, PROJECT_RED};
    MATERIAL_TO_MODID.put("silver", silver);

    String[] steel = {IMMERSIVE_ENGINEERING, NUCLEARCRAFT, THERMAL_FOUNDATION, TCONSTRUCT};
    MATERIAL_TO_MODID.put("steel", steel);

    String[] stellar = {ENDERGY};
    MATERIAL_TO_MODID.put("stellar",stellar);

    String[] tin = {THERMAL_FOUNDATION, FORESTRY, NUCLEARCRAFT, NUCLEARCRAFT};
    MATERIAL_TO_MODID.put("tin", tin);
  }

  public static Set<String> config = new HashSet<>();

  public static void writeConfig() {

    File file = new File("config/extraanvils.json");
    if (file.exists()) return;
    JsonObject config = new JsonObject();

    for (Map.Entry<String,String[]> ore : MATERIAL_TO_MODID.entrySet()) {
      config.add(ore.getKey(), new JsonPrimitive(true));
    }

    String json = prettyJson(config);

    try {
      FileWriter writer = new FileWriter(file);
      writer.write(json);
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

  public static void readConfig() {
    try {

      FileReader reader = new FileReader("config/extraanvils.json");
      JsonElement json = new JsonParser().parse(reader);

      JsonObject object = json.getAsJsonObject();

      for (Map.Entry<String,String[]> ore : MATERIAL_TO_MODID.entrySet()) {
        String material = ore.getKey();
        try {
          boolean flag = object.get(material).getAsBoolean();
          if (flag) {
            ExtraAnvils.logger.info("registering " + material + " anvil");
            config.add(material);
          } else {
            ExtraAnvils.logger.info("skipping " + material + " anvil");
          }
          // eat the exception
        } catch (NullPointerException ignored) {
          ExtraAnvils.logger.warn(material + " anvil not found, skipping");
        }
      }
    } catch (IOException ofcourse) {
      throw new RuntimeException(ofcourse);
    }
  }

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    writeConfig();
    readConfig();
    IForgeRegistry<Block> registry = event.getRegistry();

    if (config.contains("stone"))
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("stone", 20, .8, .5, -1), variant));
      }

    if (config.contains("gold"))
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("gold", 160, 2.8, 1, 4), variant));
      }

    if (config.contains("diamond"))
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("diamond", Integer.MAX_VALUE, .5, 10, 64, 1, true), variant));
      }

    //modded material handler

    if (config.contains("aluminum") && checkModlist("aluminum")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("aluminum", 40, 0.3, 3, 1, 1, false), variant));
      }
    }

    if (config.contains("ardite") && checkModlist("ardite")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("ardite", 480, 2.4, 3, 6, 1, false), variant));
      }
    }

    if (config.contains("cobalt") && checkModlist("cobalt")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("cobalt", 320, 2, 4, 10, 1, false), variant));
      }
    }

    if (config.contains("bronze") && checkModlist("bronze")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("bronze", 60, 1.2, 1, 1.2, 1, false), variant));
      }
    }

    if (config.contains("copper") && checkModlist("copper")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("copper", 40, 1.2, 1, 2, 1, false), variant));
      }
    }

    if (config.contains("electrum") && checkModlist("electrum")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("electrum", 120, 2.4, 10, 3, 1, false), variant));
      }
    }

    if (config.contains("endsteel") && checkModlist("endsteel")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("endsteel", 200, 2, 4, 25, 1, false), variant));
      }
    }

    if (config.contains("inferium") && checkModlist("inferium")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("inferium", 120, 2, 10, 3, 1, false), variant));
      }
    }

    if (config.contains("prudentium") && checkModlist("prudentium")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("prudentium", 240, 2, 10, 9, 1, false), variant));
      }
    }

    if (config.contains("intermedium") && checkModlist("intermedium")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("intermedium", 480, 2, 10, 18, 1, false), variant));
      }
    }

    if (config.contains("superium") && checkModlist("superium")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("superium", 960, 2, 10, 36, 1, false), variant));
      }
    }

    if (config.contains("supremium") && checkModlist("supremium")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("supremium", 1920, 2, 10, 108, 1, false), variant));
      }
    }

    if (config.contains("invar") && checkModlist("invar")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("invar", 40, 2, 10, 2, 1, false), variant));
      }
    }

    if (config.contains("iridium") && checkModlist("iridium")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("iridium", 640, 2.9, 10, 100, 1, false), variant));
      }
    }

    if (config.contains("lead") && checkModlist("lead")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("lead", 40, 2, 10, 2, 1, false), variant));
      }
    }

    if (config.contains("manyullyn") && checkModlist("manyullyn")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("manyullyn", 300, 2, 6, 32, 1, false), variant));
      }
    }

    if (config.contains("nickel") && checkModlist("nickel")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("nickel", 50, 2.9, 10, 3, 1, false), variant));
      }
    }

    if (config.contains("platinum") && checkModlist("platinum")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("platinum", 320, 2.9, 10, 3, 1, false), variant));
      }
    }

    if (config.contains("silver") && checkModlist("silver")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("silver", 80, 2, 10, 3, 1, false), variant));
      }
    }

    if (config.contains("steel") && checkModlist("steel")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("steel", 80, 1.1, 10, 3, 1, false), variant));
      }
    }

    if (config.contains("stellar") && checkModlist("stellar")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("stellar", 1000, 2, 10, 250, 1, false), variant));
      }
    }

    if (config.contains("tin") && checkModlist("tin")) {
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("tin", 40, .7, 10, 2, 1, false), variant));
      }
    }
  }

  public static boolean checkModlist(String s) {
    return stream(MATERIAL_TO_MODID.get(s)).anyMatch(Loader::isModLoaded);
  }
}

