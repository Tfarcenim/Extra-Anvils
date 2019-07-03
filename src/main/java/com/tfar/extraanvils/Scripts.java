package com.tfar.extraanvils;

import com.google.gson.*;
import com.tfar.extraanvils.generic.GenericAnvilBlock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static com.tfar.extraanvils.ExtraAnvils.MODID;

  public class Scripts {


    public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static JsonArray pattern = new JsonArray();
    private static String[] recipe = {"III", " i ", "iii"};

}/*
    static {
      for (String line : recipe) {
        pattern.add(line);
      }
    }

    public static void scripts() {

      if (true) return;

      try {
        for (GenericAnvilBlock.Variant damage : GenericAnvilBlock.Variant.values()) {
          for (Map.Entry<String, JsonElement> entry : RegistryHandler.jsonRead.entrySet()) {
            String material = entry.getKey();

            if ("diamond".equals(material) || "generic".equals(material) || "stone".equals(material)) continue;
            //handle blockstates
            blockstates(material, damage);
            block(material, damage);
            item(material, damage);


          /*
               if (damage != GenericAnvilBlock.Variant.NORMAL)continue;
               File file3 = new File("jsons/recipes/" + material + damage.getString() + ".json");
               JsonObject recipes = new JsonObject();
               JsonObject key = new JsonObject();
               JsonObject I = new JsonObject();
               I.addProperty("type","forge:ore_dict");
               I.addProperty("ore","block"+material.substring(0,1).toUpperCase()+material.substring(1));
               JsonObject i = new JsonObject();
               i.addProperty("type","forge:ore_dict");
               i.addProperty("ore","ingot"+material.substring(0,1).toUpperCase()+material.substring(1));
               key.add("I",I);
               key.add("i",i);
               recipes.add("pattern",pattern);
               recipes.addProperty("type","minecraft:crafting_shaped");
               recipes.add("key",key);
               JsonObject result = new JsonObject();
               result.addProperty("item","extraanvils:"+material+"_anvil");
               recipes.add("result",result);
               FileWriter writer3 = new FileWriter(file3);
               writer3.write(Setup.prettyJson(recipes));
               writer3.flush();
               *
          }
        }
      } catch (Exception oof) {
        oof.printStackTrace();
      }
    }

    private static void item(String material, GenericAnvilBlock.Variant damage) {
      JsonObject parent = new JsonObject();
      parent.addProperty("parent", MODID + ":block/" + material + damage.s);
      File file = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Extra Anvils\\1.12\\src\\main\\resources\\assets\\extraanvils\\models\\item\\" + material + damage.getString() + ".json");
      if (file.exists())return;
      try {

        FileWriter writer1 = new FileWriter(file);
        writer1.write(g.toJson(parent));
        writer1.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private static void block(String material, GenericAnvilBlock.Variant damage) {

      JsonObject textures = new JsonObject();
      textures.addProperty("particle", MODID + ":blocks/" + material + GenericAnvilBlock.Variant.NORMAL.s + "_base");
      textures.addProperty("body", MODID + ":blocks/" + material + GenericAnvilBlock.Variant.NORMAL.s + "_base");
      textures.addProperty("top", MODID + ":blocks/" + material + "_anvil_top" + damage.s.substring(6));
      JsonObject blockmodel = new JsonObject();
      blockmodel.addProperty("parent", "block/anvil");
      blockmodel.add("textures", textures);
      File file = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Extra Anvils\\1.12\\src\\main\\resources\\assets\\extraanvils\\models\\block\\" + material + damage.getString() + ".json");
      if (file.exists())return;
      try {
        FileWriter writer2 = new FileWriter(file);
        writer2.write(g.toJson(blockmodel));
        writer2.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public static void blockstates(String material, GenericAnvilBlock.Variant damage) {
      String[] compass = {"south", "west", "north", "east"};
      File blockstate = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Extra Anvils\\1.12\\src\\main\\resources\\assets\\extraanvils\\blockstates\\" + material + damage.getString() + ".json");
      if (blockstate.exists()) return;
      JsonObject facing = new JsonObject();
      for (int i = 0; i < 4; i++) {
        String model = MODID + ":" + material + damage.s;
        JsonObject obj = new JsonObject();
        obj.addProperty("model", model);
        if (i != 0) obj.addProperty("y", i * 90);
        facing.add("facing=" + compass[i], obj);
      }
      JsonObject blockstates = new JsonObject();
      blockstates.add("variants", facing);
      try {
        FileWriter writer = new FileWriter(blockstate);
        writer.write(g.toJson(blockstates));
        writer.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}*/
