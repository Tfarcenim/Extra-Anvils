package com.tfar.extraanvils.generic;

import javax.annotation.Nullable;

public class AnvilProperties {

  public final String name;
  public final int cap;
  public final double weight;
  public final double durability;
  public final double enchantability;
  public final boolean playerDamage;
  @Nullable public final String ore1;
  @Nullable public final String ore2;
  @Nullable public final String[] traits;


  public AnvilProperties(String name, int cap, double weight, double durability, double enchantability, boolean playerDamage) {
    this(name, cap, weight, durability, enchantability, playerDamage,null,null, null);

  }

  public AnvilProperties(String name, int cap, double weight, double durability, double enchantability, boolean playerDamage,@Nullable String ore1, @Nullable String ore2, @Nullable String[] traits){
    this.name = name;
    this.cap = cap;
    this.weight = weight;
    this.durability = durability;
    this.enchantability = enchantability;
    this.playerDamage = playerDamage;
    this.ore1 = ore1;
    this.ore2 = ore2;
    this.traits = traits;
  }
}
