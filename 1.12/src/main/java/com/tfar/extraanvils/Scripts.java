package com.tfar.extraanvils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import static com.tfar.extraanvils.ExtraAnvils.MODID;

public class Scripts {

  private static JsonArray pattern = new JsonArray();
  private static String[] recipe =  {"III", " i ", "iii"};

      static {
        for (String line : recipe){
         pattern.add(line);
        }
      }

       public static void scripts() {

    /*     String[] compass = {"south", "west", "north", "east"};

         try {
           for (EnumVariants damage : EnumVariants.values()) {
             for (Map.Entry<String, String[]> ore : Setup.anvils.entrySet()) {
               String material = ore.getKey();

               if (material.equals("gold") || material.equals("stone") || material.equals("diamond")) continue;

               //strings.add(material);

               //handle blockstates
               File file = new File("jsons/blockstates/" + material + damage.getString() + ".json");
               JsonObject variants = new JsonObject();
               for (int i = 0; i < 4; i++) {
                 JsonObject element = new JsonObject();
                 element.addProperty("model", MODID + ":" + material + damage.getString());
                 if (i != 0) element.addProperty("y", i * 90);
                 variants.add("facing=" + compass[i], element);
               }
               JsonObject blockstates = new JsonObject();
               blockstates.add("variants", variants);
               FileWriter writer = new FileWriter(file);
               writer.write(Setup.prettyJson(blockstates));
               writer.flush();
               //handle itemblocks
               File file1 = new File("jsons/models/item/" + material + damage.getString() + ".json");
               JsonObject parent = new JsonObject();
               parent.addProperty("parent", MODID + ":block/" + material + damage.getString());
               FileWriter writer1 = new FileWriter(file1);
               writer1.write(Setup.prettyJson(parent));
               writer1.flush();


               File file2 = new File("jsons/models/block/" + material + damage.getString() + ".json");

               JsonObject textures = new JsonObject();
               textures.addProperty("particle", MODID + ":blocks/" + material + EnumVariants.NORMAL.getString() + "_top");
               textures.addProperty("body", MODID + ":blocks/" + material + EnumVariants.NORMAL.getString() + "_top");
               textures.addProperty("top", MODID + ":blocks/" + material + "_anvil_top" + damage.getString().substring(6));
               JsonObject blockmodel = new JsonObject();
               blockmodel.addProperty("parent", "block/anvil");
               blockmodel.add("textures", textures);
               FileWriter writer2 = new FileWriter(file2);
               writer2.write(Setup.prettyJson(blockmodel));
               writer2.flush();

               if (damage != EnumVariants.NORMAL)continue;

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
             }
           }
         } catch (Exception oof) {
           oof.printStackTrace();
         }
*/
       }
}
