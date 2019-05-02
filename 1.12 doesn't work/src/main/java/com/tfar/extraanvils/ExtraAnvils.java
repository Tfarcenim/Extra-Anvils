package com.tfar.extraanvils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = ExtraAnvils.MODID)
@Mod(modid = ExtraAnvils.MODID, name = ExtraAnvils.NAME, version = ExtraAnvils.VERSION)
public class ExtraAnvils
{
    public static final String MODID = "extraanvils";
    public static final String NAME = "Extra Anvils";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @SidedProxy(clientSide = "com.tfar.extraanvils.ClientProxy", serverSide = "com.tfar.extraanvils.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs tabExtraAnvils = new TabExtraAnvils("extra_anvils");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModAnvils.init();
        }




    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }



}
