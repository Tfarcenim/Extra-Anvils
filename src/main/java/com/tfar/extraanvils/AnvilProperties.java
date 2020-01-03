package com.tfar.extraanvils;

import javax.annotation.Nullable;

public class AnvilProperties {

  public final String color;
  public final int cap;
  public final double weight;
  public final double durability;
  public final double enchantability;
  public final boolean playerDamage;
  public final String[] traits;
  public final String name;

  public AnvilProperties(String name, String color,int cap, double weight, double durability, double enchantability, boolean playerDamage){
    this(name,color, cap, weight, durability, enchantability, playerDamage, null);
  }

  public AnvilProperties(String name, String color, int cap, double weight, double durability, double enchantability, boolean playerDamage, @Nullable String[] traits){
    this.name = name;
    this.color = color;
    this.cap = cap;
    this.weight = weight;
    this.durability = durability;
    this.enchantability = enchantability;
    this.playerDamage = playerDamage;
    this.traits = traits;
  }
}