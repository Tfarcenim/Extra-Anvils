package com.tfar.extraanvils;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class AnvilTags {
  public static final Tag<Block> GOLD_ANVIL = makeWrapperTag("gold_anvil");
  public static final Tag<Block> DIAMOND_ANVIL = makeWrapperTag("diamond_anvil");

  private static Tag<Block> makeWrapperTag(String id) {
    return new BlockTags.Wrapper(new ResourceLocation(ExtraAnvils.MODID,id));
  }
}
