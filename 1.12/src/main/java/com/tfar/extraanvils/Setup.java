package com.tfar.extraanvils;

import com.google.gson.*;
import com.tfar.extraanvils.generic.AnvilProperties;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static com.tfar.extraanvils.ModId.*;
import static java.util.Arrays.stream;

@Mod.EventBusSubscriber
public class Setup {

  public static Map<String, String[]> MATERIAL_TO_MODID = new LinkedHashMap<>();

  static {

    String[] minecraft = {"minecraft"};

    MATERIAL_TO_MODID.put("diamond", minecraft);

    MATERIAL_TO_MODID.put("gold", minecraft);

    MATERIAL_TO_MODID.put("stone", minecraft);

    String[] inferium = {MYSTICAL_AGRICULTURE}, prudentium = {MYSTICAL_AGRICULTURE}, intermedium = {MYSTICAL_AGRICULTURE}, superium = {MYSTICAL_AGRICULTURE}, supremium = {MYSTICAL_AGRICULTURE};

    MATERIAL_TO_MODID.put("inferium", inferium);
    MATERIAL_TO_MODID.put("intermedium", intermedium);
    MATERIAL_TO_MODID.put("prudentium", prudentium);
    MATERIAL_TO_MODID.put("superium", superium);
    MATERIAL_TO_MODID.put("supremium", supremium);

    String[] aluminum = {IMMERSIVE_ENGINEERING, THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("aluminum", aluminum);

    String[] ardite = {TCONSTRUCT};
    MATERIAL_TO_MODID.put("ardite", ardite);

    String[] bronze = {EMBERS, THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING, FORESTRY, MEKANISM, NUCLEARCRAFT, PROJECT_RED};
    MATERIAL_TO_MODID.put("bronze", bronze);

    String[] cobalt = {TCONSTRUCT};
    MATERIAL_TO_MODID.put("cobalt", cobalt);

    String[] copper = {EMBERS, THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING, FORESTRY, MEKANISM, NUCLEARCRAFT, PROJECT_RED};
    MATERIAL_TO_MODID.put("copper", copper);

    String[] electrum = {IMMERSIVE_ENGINEERING, THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("electrum", electrum);

    String[] enderium = {THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("enderium", enderium);

    String[] endsteel = {ENDER_IO};
    MATERIAL_TO_MODID.put("endsteel", endsteel);

    String[] energetic_alloy = {ENDER_IO};
    MATERIAL_TO_MODID.put("energetic_alloy", energetic_alloy);

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

    String[] signalum = {THERMAL_FOUNDATION};
    MATERIAL_TO_MODID.put("signalum", signalum);

    String[] silver = {THERMAL_FOUNDATION, IMMERSIVE_ENGINEERING, NUCLEARCRAFT, PROJECT_RED};
    MATERIAL_TO_MODID.put("silver", silver);

    String[] steel = {IMMERSIVE_ENGINEERING, NUCLEARCRAFT, THERMAL_FOUNDATION, TCONSTRUCT};
    MATERIAL_TO_MODID.put("steel", steel);

    String[] stellar = {ENDERGY};
    MATERIAL_TO_MODID.put("stellar", stellar);

    String[] tin = {THERMAL_FOUNDATION, FORESTRY, NUCLEARCRAFT, NUCLEARCRAFT};
    MATERIAL_TO_MODID.put("tin", tin);

    String[] vibrant_alloy = {ENDER_IO};
    MATERIAL_TO_MODID.put("vibrant_alloy", vibrant_alloy);
  }

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

      recipe.setRegistryName(new ResourceLocation(ExtraAnvils.MODID,anvil.properties.material + "anvil"));
      registry.register(recipe);
    }
  }


  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    writeConfig();


    IForgeRegistry<Block> registry = event.getRegistry();
    try {

      FileReader reader = new FileReader("config/extraanvils.json");
      JsonElement json = new JsonParser().parse(reader);
      JsonObject object = json.getAsJsonObject();
      for (Map.Entry<String, String[]> ore : MATERIAL_TO_MODID.entrySet()) {
        String material = ore.getKey();

        try {
          JsonObject obj = object.getAsJsonObject(material);
          boolean flag = obj.get("enabled").getAsBoolean();

          if (flag && checkModlist(material)) {
            ExtraAnvils.logger.info("registering " + material + " anvil");
            for (EnumVariants variant : EnumVariants.values()) {
              registry.register(new BlockGenericAnvil(new AnvilProperties(material, obj.get("level cap").getAsInt(), obj.get("weight").getAsDouble(), 1, obj.get("durability multiplier").getAsDouble(), obj.get("enchantability").getAsDouble(), obj.get("player damage").getAsBoolean()), variant));

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

  public static boolean checkModlist(String s) {
    return stream(MATERIAL_TO_MODID.get(s)).anyMatch(Loader::isModLoaded);
  }
}