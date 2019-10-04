package com.tfar.extraanvils.compat;

import com.google.common.collect.Sets;
import com.tfar.anviltweaks.AnvilTile;
import com.tfar.anviltweaks.AnvilTileSpecialRenderer;
import com.tfar.anviltweaks.AnvilTweaks;
import com.tfar.extraanvils.ExtraAnvils;
import com.tfar.extraanvils.generic.GenericAnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.Set;

public class AnvilTweaksCompat extends Compat {
  public static TileEntity getAnvilTweaksTile(GenericAnvilBlock block) {
    return new GenericAnvilAnvilTweaksCompatTile();
  }
  public static void bindTESR(){
    ClientRegistry.bindTileEntitySpecialRenderer(GenericAnvilAnvilTweaksCompatTile.class, new AnvilTileSpecialRenderer());
  }

  public static void hax(final RegistryEvent.Register<TileEntityType<?>> event){
    Set<Block> valid = Sets.newHashSet(Blocks.ANVIL,Blocks.CHIPPED_ANVIL,Blocks.DAMAGED_ANVIL);
    valid.addAll(ExtraAnvils.anvils);
    ExtraAnvils.register(TileEntityType.Builder.create(() -> new AnvilTile(AnvilTweaks.Stuff.anvil_tile), valid.toArray(new Block[0])).build(null),"anviltweaks","anvil_tile",event.getRegistry());
  }
}
