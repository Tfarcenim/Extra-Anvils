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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.tfar.extraanvils.ModId.*;
import static java.util.Arrays.stream;

@Mod.EventBusSubscriber
public class OreMap {

  public static Map<String, String[]> ORE_TO_MODID = new HashMap<>();
  public static Set<String> config = new HashSet<>();
  public static final String[] ores = {"aluminum","copper", "diamond", "electrum", "gold", "invar","iridium", "lead", "nickel", "platinum", "silver", "steel","stone", "tin"};

  public static void writeConfig() {

    File file = new File("config/extraanvils.json");
    if (file.exists()) return;
    JsonObject config = new JsonObject();
    for (String ore : ores) {
      config.add(ore, new JsonPrimitive(true));
    }
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(config.toString());
      writer.flush();
    } catch (IOException ugh) {
      //it be like that sometimes
      ugh.printStackTrace();
    }

  }

  public static void readConfig() {
    try {

      FileReader reader = new FileReader("config/extraanvils.json");
      JsonElement json = new JsonParser().parse(reader);

      JsonObject object = json.getAsJsonObject();

      for (String ore : ores) {
        try {
          boolean flag = object.get(ore).getAsBoolean();
          if (flag){ExtraAnvils.logger.info("registering " + ore + " anvil");config.add(ore);}
          else   {ExtraAnvils.logger.info("skipping " + ore + " anvil");
          }
          // eat the exception
        }catch (NullPointerException ignored){ExtraAnvils.logger.warn(ore + " anvil not found, skipping");}
      }
    } catch (IOException ofcourse) {
      throw new RuntimeException(ofcourse);
    }
  }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register< Block > event) {

    readConfig();
    init();
      IForgeRegistry<Block> registry = event.getRegistry();

      if (config.contains("stone"))
      for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("stone", 20, .8,.5, -1), variant));
      }

      if (config.contains("gold"))
        for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("gold", 160, 2.8,1, 4), variant));
      }

      if (config.contains("diamond"))
        for (EnumVariants variant : EnumVariants.values()) {
        registry.register(new BlockGenericAnvil(
                new AnvilProperties("diamond", Integer.MAX_VALUE, .5,10, 64,1,true), variant));
      }

      //modded material handler

      if (config.contains("aluminum") && checkModlist("aluminum")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("aluminum", 40, 0.3,10, 1,1,false), variant));
        }
      }

      if (config.contains("copper") && checkModlist("copper")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("copper", 50, 1.2,10, 2,1,false), variant));
        }
      }

      if (config.contains("electrum") && checkModlist("electrum")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("electrum", 120, 2.4,10, 3,1,false), variant));
        }
      }

      if (config.contains("invar") && checkModlist("invar")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("invar", 40, 2,10, 2,1,false), variant));
        }
      }

      if (config.contains("iridium") && checkModlist("iridium")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("iridium", 640, 2.9,10, 100,1,false), variant));
        }
      }

      if (config.contains("lead") && checkModlist("lead")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("lead", 40, 2,10, 2,1,false), variant));
        }
      }

      if (config.contains("nickel") && checkModlist("nickel")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("nickel", 50, 2.9,10, 3,1,false), variant));
        }
      }

      if (config.contains("platinum") && checkModlist("platinum")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("platinum", 320, 2.9,10, 3,1,false), variant));
        }
      }

      if (config.contains("silver") && checkModlist("silver")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("silver", 80, 2,10, 3,1,false), variant));
        }
      }

      if (config.contains("steel") && checkModlist("steel")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("steel", 60, 1.1,10, 3,1,false), variant));
        }
      }

      if (config.contains("tin") && checkModlist("tin")){
        for (EnumVariants variant : EnumVariants.values()) {
          registry.register(new BlockGenericAnvil(
                  new AnvilProperties("tin", 40, .7,10, 2,1,false), variant));
        }
      }
    }

public static boolean checkModlist(String s){
   String[] modsKnownForAddingThisOre = ORE_TO_MODID.get(s);
  return stream(modsKnownForAddingThisOre).anyMatch(Loader::isModLoaded);
}

   public static void init() {
    String[] aluminum = {IMMERSIVE_ENGINEERING,THERMAL_FOUNDATION};
    ORE_TO_MODID.put("aluminum",aluminum);

     String[] copper = {EMBERS, THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING, FORESTRY, NUCLEARCRAFT, PROJECT_RED};
     ORE_TO_MODID.put("copper", copper);

     String[] electrum = {IMMERSIVE_ENGINEERING, THERMAL_FOUNDATION};
     ORE_TO_MODID.put("electrum", electrum);

     String[] invar = {THERMAL_FOUNDATION};
     ORE_TO_MODID.put("invar", invar);

     String[] iridium = {IC2,THERMAL_FOUNDATION};
     ORE_TO_MODID.put("iridium", iridium);

     String[] lead = {THERMAL_FOUNDATION, EMBERS,NUCLEARCRAFT};
     ORE_TO_MODID.put("lead", lead);

     String[] nickel = {THERMAL_FOUNDATION};
     ORE_TO_MODID.put("nickel", nickel);

     String[] platinum = {THERMAL_FOUNDATION};
     ORE_TO_MODID.put("platinum", platinum);

     String[] silver = {THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING,NUCLEARCRAFT, PROJECT_RED};
     ORE_TO_MODID.put("silver", silver);

     String[] steel = {IMMERSIVE_ENGINEERING,NUCLEARCRAFT,THERMAL_FOUNDATION,TCONSTRUCT};
     ORE_TO_MODID.put("steel",steel);

     String[] tin = {THERMAL_FOUNDATION, FORESTRY, NUCLEARCRAFT,NUCLEARCRAFT};
     ORE_TO_MODID.put("tin", tin);
   }
  }

