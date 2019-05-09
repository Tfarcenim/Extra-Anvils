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
import java.util.Map;

@Mod(modid = ExtraAnvils.MODID,name = ExtraAnvils.NAME, version = ExtraAnvils.MODVERSION/*, dependencies = "required-after:forge@[14.23.5.2796)"*/)
public class ExtraAnvils
{
    public static final String MODID = "extraanvils";
    public static final String NAME = "Extra Anvils";
    public static final String MODVERSION = "@VERSION@";

    public static final HashMap<BlockGenericAnvil,BlockGenericAnvil> anvilDamageMap = new HashMap<>();

    public static final List<BlockGenericAnvil> anvils = new ArrayList<>();

    public static ArrayList<String> strings = new ArrayList<>();

    public static CreativeTabs creativeTab = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ObjectHolders.blockDiamondAnvil);
        }
    };

    public static Logger logger = LogManager.getLogger();

    @Mod.Instance
    public static ExtraAnvils instance;
    @SidedProxy(clientSide = "com.tfar.extraanvils.ClientProxy", serverSide = "com.tfar.extraanvils.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {

        boolean doJson = false;

        if (doJson) {
            Scripts.scripts();
        }

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
    }
}
