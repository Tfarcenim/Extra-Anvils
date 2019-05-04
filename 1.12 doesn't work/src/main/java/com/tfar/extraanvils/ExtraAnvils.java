package com.tfar.extraanvils;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExtraAnvils.MODID,name = ExtraAnvils.NAME, version = ExtraAnvils.MODVERSION/*, dependencies = "required-after:forge@[14.23.5.2796)"*/)
public class ExtraAnvils
{
    public static final String MODID = "extraanvils";
    public static final String NAME = "Extra Anvils";
    public static final String MODVERSION = "@VERSION@";

    public static CreativeTabs creativeTab = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModAnvils.blockDiamondAnvil);
        }
    };

    public static Logger logger;

    @Mod.Instance
    public static ExtraAnvils instance;
    @SidedProxy(clientSide = "com.tfar.extraanvils.ClientProxy", serverSide = "com.tfar.extraanvils.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
    }
}
