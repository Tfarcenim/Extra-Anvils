package com.tfar.extraanvils;

import javax.annotation.Nullable;

public class AnvilProperties {

  public final String color;
  public final boolean enabled;
  public final int cap;
  public final double weight;
  public final double durability;
  public final double enchantability;
  public final boolean playerDamage;
  public final String[] traits;

  public AnvilProperties(String color,int cap, double weight, double durability, double enchantability, boolean playerDamage){
    this(color,true,cap, weight, durability, enchantability, playerDamage, null);
  }

  public AnvilProperties(String color,boolean enabled, int cap, double weight, double durability, double enchantability, boolean playerDamage, @Nullable String[] traits){
    this.color = color;
    this.enabled = enabled;
    this.cap = cap;
    this.weight = weight;
    this.durability = durability;
    this.enchantability = enchantability;
    this.playerDamage = playerDamage;
    this.traits = traits;
  }

  public static final int[] BLACK = new int[] {0,0,0};

  public static int[] getRGB(String color){
    if (color.startsWith("#"))
    try {
    int raw = Integer.decode(color);
    return new int[]{raw << 16 & 0xFF, raw << 8 & 0xFF, raw & 0xFF};
    } catch (NumberFormatException | NullPointerException notANumber){
      ExtraAnvils.logger.error(notANumber);
    }
    return BLACK;
  }
}