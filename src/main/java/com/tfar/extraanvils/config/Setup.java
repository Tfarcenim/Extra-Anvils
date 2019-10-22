package com.tfar.extraanvils.config;

import com.google.gson.*;
import com.tfar.extraanvils.EnumVariants;
import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.aether.BlockAetherAnvil;
import com.tfar.extraanvils.generic.AnvilProperties;
import com.tfar.extraanvils.generic.BlockGenericAnvil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

@Mod.EventBusSubscriber
public class Setup {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  private static boolean error = false;
  private static File configFile = new File("config/extraanvils.json");
  private static BufferedInputStream in = new BufferedInputStream(Setup.class.getResourceAsStream("/default.json"));
  static AnvilProperties[] jsonRead;

  static {
    String s;
    try {
      s = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException("The default config is broken, report to mod author asap!", e);
    }
    jsonRead = g.fromJson(s, AnvilProperties[].class);
  }

  public static void writeConfig() {

    if (configFile.exists()) return;

    try {
      FileWriter writer = new FileWriter(configFile);
      writer.write(g.toJson(jsonRead));
      writer.flush();
    } catch (IOException ugh) {
      //I expect this from a user, but you?!
      throw new RuntimeException("The default config is broken, report to mod author asap!", ugh);
    }
  }


  //some people say incode recipes are bad, but this is 1.12 so it doesn't matter yet
  @SubscribeEvent
  public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

    IForgeRegistry<IRecipe> registry = event.getRegistry();
    for (BlockGenericAnvil anvil : ExtraAnvils.anvils) {
      if (anvil.variant != EnumVariants.NORMAL)
        continue;

      String ore1 = anvil.properties.ore1;
      String ore2 = anvil.properties.ore2;
      if (ore1 == null || ore2 == null) continue;

      IRecipe recipe = new ShapedOreRecipe(new ResourceLocation(ExtraAnvils.MODID, anvil.properties.name + "anvil"), new ItemStack(anvil),

              "bbb",
              " i ",
              "iii",
              'b', ore1,
              'i', ore2);

      recipe.setRegistryName(new ResourceLocation(ExtraAnvils.MODID, anvil.properties.name + "anvil"));
      registry.register(recipe);
    }
  }

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    writeConfig();

    IForgeRegistry<Block> registry = event.getRegistry();
    try {

      FileReader reader = new FileReader("config/extraanvils.json");
      AnvilProperties[] anvilProperties = g.fromJson(reader, AnvilProperties[].class);
      for (AnvilProperties anvilProperties1 : anvilProperties) {
        String name = anvilProperties1.name;
        try {
          ExtraAnvils.logger.info("registering " + name + " anvil");
          for (EnumVariants variant : EnumVariants.values()) {
            BlockGenericAnvil anvil;

            if (anvilProperties1.traits != null && Arrays.asList(anvilProperties1.traits).contains("reverse"))
              anvil = new BlockAetherAnvil(anvilProperties1, variant);
            else
              anvil = new BlockGenericAnvil(anvilProperties1, variant);
            ExtraAnvils.anvils.add(anvil);
            anvil.setRegistryName(name + variant.getString());
            anvil.setTranslationKey(anvil.getRegistryName().toString());
            registry.register(anvil);
          }
          //in case of exceptions
        } catch (Exception e) {
          ExtraAnvils.logger.error("Error registering " + name + " anvil, skipping", e);
          error = true;
        }
      }
    } catch (IOException e){

    }
  }

  @SubscribeEvent
  public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
    if (error) {
      ITextComponent component = new TextComponentString(TextFormatting.WHITE + "[")
              .appendSibling(new TextComponentTranslation("extraanvils.name").setStyle(new Style().setColor(TextFormatting.AQUA)))
              .appendSibling(new TextComponentString("]: ").setStyle(new Style().setColor(TextFormatting.WHITE)))
              .appendSibling(new TextComponentTranslation("extraanvils.error")).setStyle(new Style().setColor(TextFormatting.RED));
      e.player.sendMessage(component);
    }
  }
}