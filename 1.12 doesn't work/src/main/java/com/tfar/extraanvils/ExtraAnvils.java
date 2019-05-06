package com.tfar.extraanvils;

import com.tfar.extraanvils.generic.BlockGenericAnvil;
import com.tfar.extraanvils.vanilla.VanillaAnvils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

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

    public static Logger logger;

    @Mod.Instance
    public static ExtraAnvils instance;
    @SidedProxy(clientSide = "com.tfar.extraanvils.ClientProxy", serverSide = "com.tfar.extraanvils.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        proxy.preInit(event);

      //  String[] materials = new String[]{"copper","silver"};

      //  for (String material :materials)

     //   JsonObject itemBlock = new JsonObject();

   //     itemBlock.addProperty("parent", value);




    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
    }
}
