package com.tfar.extraanvils;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import static com.tfar.extraanvils.ExtraAnvils.MODID;

public class Scripts {

       public static void scripts() {

         String[] compass = {"south", "west", "north", "east"};

         try {
           for (EnumVariants damage : EnumVariants.values()) {
             for (Map.Entry<String, String[]> ore : Materials.MATERIAL_TO_MODID.entrySet()) {
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
               writer.write(Materials.prettyJson(blockstates));
               writer.flush();
               //handle itemblocks
               File file1 = new File("jsons/models/item/" + material + damage.getString() + ".json");
               JsonObject parent = new JsonObject();
               parent.addProperty("parent", MODID + ":block/" + material + damage.getString());
               FileWriter writer1 = new FileWriter(file1);
               writer1.write(Materials.prettyJson(parent));
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
               writer2.write(Materials.prettyJson(blockmodel));
               writer2.flush();
             }
           }
         } catch (Exception oof) {
           oof.printStackTrace();
         }

       }
}
