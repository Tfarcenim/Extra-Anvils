package com.tfar.extraanvils;

import com.google.gson.JsonObject;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(modid = ExtraAnvils.MODID,name = ExtraAnvils.NAME, version = ExtraAnvils.MODVERSION/*, dependencies = "required-after:forge@[14.23.5.2796)"*/)
public class ExtraAnvils
{
    public static final String MODID = "extraanvils";
    public static final String NAME = "Extra Anvils";
    public static final String MODVERSION = "@VERSION@";

    public static final HashMap<BlockGenericAnvil,BlockGenericAnvil> anvilDamageMap = new HashMap<>();

    public static final List<BlockGenericAnvil> anvils = new ArrayList<>();

    public static CreativeTabs creativeTab = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(VanillaAnvils.blockDiamondAnvil);
        }
    };

    public static Logger logger = LogManager.getLogger();

    @Mod.Instance
    public static ExtraAnvils instance;
    @SidedProxy(clientSide = "com.tfar.extraanvils.ClientProxy", serverSide = "com.tfar.extraanvils.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {

        boolean doJson = true;

        if (doJson) {

            String[] compass = {"south", "west", "north", "east"};

            try {
                for (EnumVariants damage : EnumVariants.values()) {
                    for (String ore : OreMap.ores) {


                        if (ore.equals("gold") || ore.equals("stone") || ore.equals("diamond")) continue;



                        //handle blockstates
                        File file = new File("jsons/blockstates/" + ore + damage.getString() + ".json");
                        JsonObject variants = new JsonObject();
                        for (int i = 0; i < 4; i++) {
                            JsonObject element = new JsonObject();
                            element.addProperty("model", MODID + ":" + ore + damage.getString());
                            if (i != 0) element.addProperty("y", i * 90);
                            variants.add("facing=" + compass[i], element);
                        }
                        JsonObject blockstates = new JsonObject();
                        blockstates.add("variants", variants);
                        FileWriter writer = new FileWriter(file);
                        writer.write(blockstates.toString());
                        writer.flush();
                        //handle itemblocks
                        File file1 = new File("jsons/models/item/" + ore + damage.getString() + ".json");
                        JsonObject parent = new JsonObject();
                        parent.addProperty("parent",  MODID + ":block/" + ore + damage.getString());
                        FileWriter writer1 = new FileWriter(file1);
                        writer1.write(parent.toString());
                        writer1.flush();


                        File file2 = new File("jsons/models/block/" + ore + damage.getString() + ".json");

                        JsonObject textures = new JsonObject();
                        textures.addProperty("particle", MODID + ":blocks/" + ore + EnumVariants.NORMAL.getString()+"_top");
                        textures.addProperty("body", MODID + ":blocks/" + ore + EnumVariants.NORMAL.getString()+"_top");
                        textures.addProperty("top", MODID + ":blocks/" + ore + "_anvil_top"+damage.getString().substring(6));
                        JsonObject blockmodel = new JsonObject();
                        blockmodel.addProperty("parent", "block/anvil");
                        blockmodel.add("textures", textures);
                        FileWriter writer2 = new FileWriter(file2);
                        writer2.write(blockmodel.toString());
                        writer2.flush();
                    }
                }
            } catch (Exception oof) {
                oof.printStackTrace();
            }

        }


        OreMap.writeConfig();


        proxy.preInit(event);





    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
    }
}
